/*
Storybook: Open Source software for novelists and authors.
Copyright (C) 2008 - 2012 Martin Mustun

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package storybook.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.View;
import net.infonode.docking.ViewSerializer;
import net.infonode.docking.WindowBar;
import net.infonode.docking.properties.RootWindowProperties;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.MixedViewHandler;
import net.infonode.docking.util.StringViewMap;
import net.infonode.util.Direction;
import net.miginfocom.swing.MigLayout;

import org.hibernate.Session;
import storybook.SbConstants;
import storybook.StorybookApp;
import storybook.SbConstants.InternalKey;
import storybook.SbConstants.PreferenceKey;
import storybook.SbConstants.Storybook;
import storybook.SbConstants.ViewName;
import storybook.action.ActionHandler;
import storybook.action.SbActionManager;
import storybook.controller.DocumentController;
import storybook.model.BlankModel;
import storybook.model.DbFile;
import storybook.model.DocumentModel;
import storybook.model.hbn.dao.PartDAOImpl;
import storybook.model.hbn.entity.Internal;
import storybook.model.hbn.entity.Part;
import storybook.model.hbn.entity.Preference;
import storybook.toolkit.DockingWindowUtil;
import storybook.toolkit.DocumentUtil;
import storybook.toolkit.I18N;
import storybook.toolkit.PrefUtil;
import storybook.toolkit.SpellCheckerUtil;
import storybook.toolkit.net.NetUtil;
import storybook.toolkit.swing.FontUtil;
import storybook.toolkit.swing.SwingUtil;
import storybook.view.edit.EntityEditor;
import storybook.view.interfaces.IPaintable;
import storybook.view.net.BrowserPanel;

/**
 * @author martin
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame implements IPaintable {

	private DocumentModel documentModel;
	private DocumentController documentController;

	private SbActionManager sbActionManager;
	private ViewFactory viewFactory;

	private JToolBar mainToolBar;
	private RootWindow rootWindow;
	private StatusBarPanel statusBar;

	private HashMap<Integer, JComponent> dynamicViews = new HashMap<Integer, JComponent>();

	private DbFile dbFile;
	private Part currentPart;

	public MainFrame() {
		FontUtil.setDefaultFont(new Font("Arial", Font.PLAIN, 12));
	}

	@Override
	public void init() {
		dbFile = null;
		viewFactory = new ViewFactory(this);
		sbActionManager = new SbActionManager(this);
		sbActionManager.init();
		documentController = new DocumentController();
		BlankModel model = new BlankModel();
		documentController.attachModel(model);
		setIconImage(I18N.getIconImage("icon.sb"));
		addWindowListener(new MainFrameWindowAdaptor());
	}

	public void init(DbFile dbFile) {
		try {
			this.dbFile = dbFile;

			viewFactory = new ViewFactory(this);
			sbActionManager = new SbActionManager(this);
			sbActionManager.init();

			// model and controller
			documentController = new DocumentController();
			documentModel = new DocumentModel();
			if (!dbFile.getDbName().isEmpty()) {
				documentModel.initSession(dbFile.getDbName());
			}
			documentController.attachModel(documentModel);

			Preference pref = PrefUtil.get(PreferenceKey.GOOGLE_MAPS_URL,
					SbConstants.DEFAULT_GOOGLE_MAPS_URL);
			NetUtil.setGoogleMapUrl(pref.getStringValue());

			// spell checker
			SpellCheckerUtil.registerDictionaries();

			addWindowListener(new MainFrameWindowAdaptor());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initUi() {
		setLayout(new MigLayout("flowy,fill,ins 0,gap 0", "", "[grow]"));
		setIconImage(I18N.getIconImage("icon.sb"));
		setTitle();

		// restore dimension, screen location, maximized
		int w = PrefUtil.get(PreferenceKey.SIZE_WIDTH,
				SbConstants.DEFAULT_SIZE_WIDTH).getIntegerValue();
		int h = PrefUtil.get(PreferenceKey.SIZE_HEIGHT,
				SbConstants.DEFAULT_SIZE_HEIGHT).getIntegerValue();
		setPreferredSize(new Dimension(w, h));
		int x = PrefUtil.get(PreferenceKey.POS_X, SbConstants.DEFAULT_POS_X)
				.getIntegerValue();
		int y = PrefUtil.get(PreferenceKey.POS_Y, SbConstants.DEFAULT_POS_Y)
				.getIntegerValue();
		setLocation(x, y);
		boolean maximized = PrefUtil.get(PreferenceKey.MAXIMIZED, false)
				.getBooleanValue();
		if (maximized) {
			setMaximized();
		}

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		StorybookApp.getInstance().resetUiFont();
		sbActionManager.reloadMenuToolbar();
		initRootWindow();
		setDefaultLayout();
		/* SB5 suppress Pro
		if (!StorybookApp.getInstance().isProVersion()) {
			JPanel panel = new JPanel(new MigLayout("flowx,ins 0,gap 0"));
			panel.add(createTeaser(), "growy,w 240!");
			panel.add(rootWindow, "grow");
			add(panel, "grow");
		} else {
			add(rootWindow, "grow");
		}*/
		// always consider pro version is true
		add(rootWindow, "grow");

		statusBar = new StatusBarPanel(this);
		add(statusBar, "growx");
		documentController.attachView(statusBar);

		pack();
		setVisible(true);

		initAfterPack();

		JMenuBar menubar = getJMenuBar();
		documentController.detachView(menubar);
		documentController.attachView(menubar);

		// load last used layout
		DockingWindowUtil.loadLayout(this,
				SbConstants.InternalKey.LAST_USED_LAYOUT.toString());

		// always hide the editor
		hideEditor();

		// restore last used part
		try {
			Internal internal = DocumentUtil.restoreInternal(this,
					InternalKey.LAST_USED_PART.toString(), 1);
			Part part = null;
			if (internal != null && internal.getIntegerValue() != null) {
				Session session = documentModel.beginTransaction();
				PartDAOImpl dao = new PartDAOImpl(session);
				part = dao.find((long) internal.getIntegerValue());
				documentModel.commit();
				if (part == null) {
					part = getCurrentPart();
				}
			} else {
				part = getCurrentPart();
			}
			sbActionManager.getActionHandler().handleChangePart(part);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//		documentController.attachView(this);
	}

//	public void modelPropertyChange(PropertyChangeEvent evt) {
//		Object oldValue = evt.getOldValue();
//		Object newValue = evt.getNewValue();
//		String propName = evt.getPropertyName();
//	}

	public void setTitle() {
		String prodFullTitle = Storybook.PRODUCT_FULL_NAME.toString();
		/* SB5 suppress Pro
		if (StorybookApp.getInstance().isProVersion()) {
			prodFullTitle = Storybook.PRODUCT_PRO_FULL_NAME.toString();
		}
		*/
		if (dbFile != null) {
			Part part = getCurrentPart();
			String partName = "";
			if (part != null) {
				partName = part.getNumberName();
			}

			String title = dbFile.getName();
			Internal internal = DocumentUtil.restoreInternal(this,
					InternalKey.TITLE, "");
			if (internal != null && !internal.getStringValue().isEmpty()) {
				title = internal.getStringValue();
			}
			setTitle(title + " [" + I18N.getMsg("msg.common.part") + " "
					+ partName + "]" + " - " + prodFullTitle);
		} else {
			setTitle(prodFullTitle);
		}
	}

	private void initRootWindow() {
		StringViewMap viewMap = viewFactory.getViewMap();
		MixedViewHandler handler = new MixedViewHandler(viewMap,
				new ViewSerializer() {
					public void writeView(View view, ObjectOutputStream out)
							throws IOException {
						out.writeInt(((DynamicView) view).getId());
					}

					public View readView(ObjectInputStream in)
							throws IOException {
						return getDynamicView(in.readInt());
					}
				});
		rootWindow = DockingUtil.createRootWindow(viewMap, handler, true);
		rootWindow.setName("rootWindow");
		rootWindow.setPreferredSize(new Dimension(4096, 2048));

		// not needed
		// documentModel.initDefault();

		SbView editorView = viewFactory.getEditorView();
		documentController.attachView(editorView.getComponent());

		// set theme
		DockingWindowsTheme currentTheme = new ShapedGradientDockingTheme();
		RootWindowProperties properties = new RootWindowProperties();
		properties.addSuperObject(currentTheme.getRootWindowProperties());

		// Our properties object is the super object of the root window
		// properties object, so all property values of the
		// theme and in our property object will be used by the root window
		rootWindow.getRootWindowProperties().addSuperObject(properties);

		rootWindow.setBorder(BorderFactory
				.createEtchedBorder(EtchedBorder.LOWERED));
	}

	public void setDefaultLayout() {
		SbView scenesView = getView(ViewName.SCENES);
		SbView chaptersView = getView(ViewName.CHAPTERS);
		SbView partsView = getView(ViewName.PARTS);
		SbView locationsView = getView(ViewName.LOCATIONS);
		SbView personsView = getView(ViewName.PERSONS);
		SbView gendersView = getView(ViewName.GENDERS);
		SbView categoriesView = getView(ViewName.CATEGORIES);
		SbView strandsView = getView(ViewName.STRANDS);
		SbView ideasView = getView(ViewName.IDEAS);
		SbView tagsView = getView(ViewName.TAGS);
		SbView itemsView = getView(ViewName.ITEMS);
		SbView tagLinksView = getView(ViewName.TAGLINKS);
		SbView itemLinksView = getView(ViewName.ITEMLINKS);
		SbView internalsView = getView(ViewName.INTERNALS);

		SbView chronoView = getView(ViewName.CHRONO);
		SbView bookView = getView(ViewName.BOOK);
		SbView manageView = getView(ViewName.MANAGE);
		SbView readingView = getView(ViewName.READING);
		SbView memoriaView = getView(ViewName.MEMORIA);

		SbView chartPersonsByDate = getView(ViewName.CHART_PERSONS_BY_DATE);
		SbView chartPersonsByScene = getView(ViewName.CHART_PERSONS_BY_SCENE);
		SbView chartWiWW = getView(ViewName.CHART_WiWW);
		SbView chartStrandsByDate = getView(ViewName.CHART_STRANDS_BY_DATE);
		SbView chartOccurrenceOfPersons = getView(ViewName.CHART_OCCURRENCE_OF_PERSONS);
		SbView chartOccurrenceOfLocations = getView(ViewName.CHART_OCCURRENCE_OF_LOCATIONS);
		SbView chartGantt = getView(ViewName.CHART_GANTT);

		SbView editorView = getView(ViewName.EDITOR);

		SbView treeView = getView(ViewName.TREE);
		SbView infoView = getView(ViewName.INFO);
		SbView navigationView = getView(ViewName.NAVIGATION);

		TabWindow tabInfoNavi = new TabWindow(new SbView[] { infoView,
				navigationView });
		tabInfoNavi.setName("tabInfoNaviWindow");
		SplitWindow swTreeInfo = new SplitWindow(false, 0.6f, treeView,
				tabInfoNavi);
		swTreeInfo.setName("swTreeInfo");

//		TabWindow tabWindow = new TabWindow(new SbView[] { scenesView,
//				chaptersView, partsView });

		TabWindow tabWindow = new TabWindow(new SbView[] { chronoView,
				bookView, manageView, readingView, memoriaView, scenesView,
				personsView, locationsView, chaptersView, gendersView,
				categoriesView, partsView, strandsView, ideasView, tagsView,
				itemsView, tagLinksView, itemLinksView, internalsView,
				chartPersonsByDate, chartPersonsByScene, chartWiWW,
				chartStrandsByDate, chartOccurrenceOfPersons,
				chartOccurrenceOfLocations, chartGantt });
		tabWindow.setName("tabWindow");
		SplitWindow swTabWinEditor = new SplitWindow(true, 0.60f, tabWindow,
				editorView);
		swTabWinEditor.setName("swTabWinEditor");

		SplitWindow swMain = new SplitWindow(true, 0.20f, swTreeInfo,
				swTabWinEditor);
		swMain.setName("swMain");
		rootWindow.setWindow(swMain);

		bookView.close();
		manageView.close();
		readingView.close();
		memoriaView.close();

		chaptersView.close();
		partsView.close();
		gendersView.close();
		categoriesView.close();
		strandsView.close();
		ideasView.close();
		tagsView.close();
		tagLinksView.close();
		itemsView.close();
		itemLinksView.close();
		internalsView.close();

		chartPersonsByDate.close();
		chartPersonsByScene.close();
		chartWiWW.close();
		chartStrandsByDate.close();
		chartOccurrenceOfPersons.close();
		chartOccurrenceOfLocations.close();
		chartGantt.close();

		editorView.minimize(Direction.RIGHT);
		WindowBar windowBar = rootWindow.getWindowBar(Direction.RIGHT);
		windowBar.setContentPanelSize(EntityEditor.MINIMUM_SIZE.width + 20);

		infoView.restoreFocus();
		chronoView.restoreFocus();

		rootWindow.getWindowBar(Direction.RIGHT).setEnabled(true);

		DockingWindowUtil.setRespectMinimumSize(this);

		// WindowBar windowBar = rootWindow.getWindowBar(Direction.DOWN);
		// while (windowBar.getChildWindowCount() > 0) {
		// windowBar.getChildWindow(0).close();
		// }
		// windowBar.addTab(views[3]);
	}

	private void initAfterPack() {
		SbView scenesView = getView(ViewName.SCENES);
		SbView locationsView = getView(ViewName.LOCATIONS);
		SbView personsView = getView(ViewName.PERSONS);
		SbView chronoView = getView(ViewName.CHRONO);
		SbView treeView = getView(ViewName.TREE);
		SbView quickInfoView = getView(ViewName.INFO);
		SbView navigationView = getView(ViewName.NAVIGATION);

		// add docking window adapter to all views (except editor)
		MainDockingWindowAdapter dockingAdapter = new MainDockingWindowAdapter();
		for (int i = 0; i < viewFactory.getViewMap().getViewCount(); ++i) {
			View view = viewFactory.getViewMap().getViewAtIndex(i);
			if (view.getName().equals(ViewName.EDITOR.toString())) {
				continue;
			}
			view.addListener(dockingAdapter);
		}

		// load initially shown views here
		SbView[] views2 = { scenesView, personsView, locationsView, chronoView,
				treeView, quickInfoView, navigationView };
		for (SbView view : views2) {
			viewFactory.loadView(view);
			documentController.attachView(view.getComponent());
			documentModel.fireAgain(view);
		}
		quickInfoView.restoreFocus();
		chronoView.restoreFocus();
	}

	public JScrollPane createTeaser() {
		final int w = 600;
		final int h = 160;
		BrowserPanel teaser;
		try {
			String locale = Locale.getDefault().toString();
			URL url = new URL(SbConstants.URL.TEASER_URL.toString()
					+ "/?locale=" + locale);
			teaser = new BrowserPanel(url.toString(), w, h);
			teaser.setBackground(Color.white);
			JScrollPane scroller = new JScrollPane(teaser);
			scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			scroller.setMinimumSize(new Dimension(w, h));
			return new JScrollPane(teaser);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return new JScrollPane();
	}

	public SbView getView(String viewName) {
		return viewFactory.getView(viewName);
	}

	public SbView getView(ViewName viewName) {
		return viewFactory.getView(viewName);
	}

	public void showView(ViewName viewName) {
		setWaitingCursor();
		SbView view = getView(viewName);
		if (view.getRootWindow() != null) {
			view.restoreFocus();
		} else {
			DockingUtil.addWindow(view, rootWindow);
		}
		view.requestFocusInWindow();
		DockingWindowUtil.setRespectMinimumSize(this);
		setDefaultCursor();
	}

	public void closeView(ViewName viewName) {
		SbView view = getView(viewName);
		view.close();
	}

	public void refresh() {
		setWaitingCursor();
		for (int i = 0; i < viewFactory.getViewMap().getViewCount(); ++i) {
			SbView view = (SbView) viewFactory.getViewMap().getViewAtIndex(i);
			getDocumentController().refresh(view);
		}
		setDefaultCursor();
	}

	public void refreshStatusBar() {
		statusBar.refresh();
	}

	public void showEditor() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				SbView editorView = getView(ViewName.EDITOR);
				editorView.cleverRestoreFocus();
			}
		});
	}

	public void hideEditor() {
		Timer timer = new Timer(200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				View editorView = getView(ViewName.EDITOR);
				if (!editorView.isShowing()) {
					return;
				}
				if (editorView.isMinimized()) {
					WindowBar bar = rootWindow.getWindowBar(Direction.RIGHT);
					bar.setSelectedTab(-1);
				} else {
					editorView.close();
				}
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	public void initBlankUi() {
		dbFile = null;
		setTitle(Storybook.PRODUCT_FULL_NAME.toString());

		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension screenSize = toolkit.getScreenSize();
		setLocation(screenSize.width / 2 - 450, screenSize.height / 2 - 320);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		StorybookApp.getInstance().resetUiFont();
		sbActionManager.reloadMenuToolbar();

		BlankPanel blankPanel = new BlankPanel(this);
		blankPanel.initAll();
		add(blankPanel);

		pack();
		setVisible(true);
	}

	public void setDefaultCursor() {
		SwingUtil.setDefaultCursor(this);
	}

	public void setWaitingCursor() {
		SwingUtil.setWaitingCursor(this);
	}

	public DbFile getDbFile(){
		return dbFile;
	}

	public boolean isBlank() {
		return dbFile == null;
	}

	public DocumentController getDocumentController() {
		return documentController;
	}

	public DocumentModel getDocumentModel() {
		return documentModel;
	}

	public RootWindow getRootWindow() {
		return rootWindow;
	}

	public SbActionManager getSbActionManager() {
		return sbActionManager;
	}

	public ActionHandler getActionController() {
		return sbActionManager.getActionController();
	}

	public ViewFactory getViewFactory() {
		return viewFactory;
	}

	private MainFrame getThis() {
		return this;
	}

	public boolean isMaximized() {
		return (getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH;
	}

	public void setMaximized() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	public void close() {
		if (!isBlank()) {
			Preference pref = PrefUtil.get(PreferenceKey.CONFIRM_EXIT, true);
			if (pref.getBooleanValue()) {
				int n = JOptionPane.showConfirmDialog(getThis(),
						I18N.getMsg("msg.common.want.close"),
						I18N.getMsg("msg.common.close"),
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.NO_OPTION
						|| n == JOptionPane.CLOSED_OPTION) {
					return;
				}
			}

			// save
			getSbActionManager().getActionHandler().handleSave();

			// save dimension, location, maximized
			Dimension dim = getSize();
			PrefUtil.set(PreferenceKey.SIZE_WIDTH, dim.width);
			PrefUtil.set(PreferenceKey.SIZE_HEIGHT, dim.height);
			PrefUtil.set(PreferenceKey.POS_X, getLocationOnScreen().x);
			PrefUtil.set(PreferenceKey.POS_Y, getLocationOnScreen().y);
			if (isMaximized()) {
				PrefUtil.set(PreferenceKey.MAXIMIZED, true);
			} else {
				PrefUtil.set(PreferenceKey.MAXIMIZED, false);
			}

			// save layout
			DockingWindowUtil.saveLayout(this,
					SbConstants.InternalKey.LAST_USED_LAYOUT.toString());

			// save last used part
			DocumentUtil.storeInternal(this,
					InternalKey.LAST_USED_PART.toString(),
					(Integer) ((int) (long) getCurrentPart().getId()));
		}

		StorybookApp app = StorybookApp.getInstance();
		app.removeMainFrame(this);
		dispose();
		if (app.getMainFrames().size() == 0) {
			app.exit();
		}
	}

	private View getDynamicView(int id) {
		View view = (View) dynamicViews.get(new Integer(id));
		if (view == null) {
			view = new DynamicView("Dynamic View " + id, null,
					createDummyViewComponent("Dynamic View " + id), id);
		}
		return view;
	}

	private static JComponent createDummyViewComponent(String text) {
		StringBuffer sb = new StringBuffer();
		for (int j = 0; j < 100; j++){
			sb.append(text + ". This is line " + j + "\n");
		}
		return new JScrollPane(new JTextArea(sb.toString()));
	}

	private static class DynamicView extends View {
		private int id;

		DynamicView(String title, Icon icon, Component component, int id) {
			super(title, icon, component);
			this.id = id;
		}

		public int getId() {
			return id;
		}
	}

	private class MainFrameWindowAdaptor extends WindowAdapter {
		public void windowClosing(WindowEvent evt) {
			close();
		}
	}

	private class MainDockingWindowAdapter extends DockingWindowAdapter {
		@Override
		public void windowAdded(DockingWindow addedToWindow,
				DockingWindow addedWindow) {
			if (addedWindow != null && addedWindow instanceof SbView) {
				SbView view = (SbView) addedWindow;
				if (!view.isLoaded()) {
					viewFactory.loadView(view);
					documentController.attachView(view.getComponent());
					documentModel.fireAgain(view);
				}
			}
		}

		@Override
		public void windowClosed(DockingWindow window) {
			if (window != null && window instanceof SbView) {
				SbView view = (SbView) window;
				if (ViewName.EDITOR.toString().equals(view.getName())) {
					// don't detach / unload the editor
					return;
				}
				if (!view.isLoaded()) {
					return;
				}
				documentController.detachView((AbstractPanel) view
						.getComponent());
				viewFactory.unloadView(view);
				StorybookApp.getInstance().runGC();
			}
		}
	}

	public Part getCurrentPart() {
		try {
			Session session = documentModel.beginTransaction();
			if (currentPart == null) {
				PartDAOImpl dao = new PartDAOImpl(session);
				currentPart = dao.findFirst();
			} else {
				session.refresh(currentPart);
			}
			documentModel.commit();
			return currentPart;
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setCurrentPart(Part currentPart) {
		if (currentPart != null) {
			this.currentPart = currentPart;
		}
	}

	public boolean hasCurrentPart() {
		return currentPart != null;
	}

	public void setMainToolBar(JToolBar toolBar) {
		if (mainToolBar != null) {
			SwingUtil.unfloatToolBar(mainToolBar);
			getContentPane().remove(mainToolBar);
		}
		this.mainToolBar = toolBar;
		getContentPane().add(mainToolBar, BorderLayout.NORTH);
	}

	public JToolBar getMainToolBar() {
		return mainToolBar;
	}
}