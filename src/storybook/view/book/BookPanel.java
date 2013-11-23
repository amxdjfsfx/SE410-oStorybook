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

package storybook.view.book;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.infonode.docking.View;
import net.miginfocom.swing.MigLayout;

import org.hibernate.Session;
import storybook.SbConstants;
import storybook.SbConstants.InternalKey;
import storybook.SbConstants.ViewName;
import storybook.controller.DocumentController;
import storybook.model.DocumentModel;
import storybook.model.hbn.dao.ChapterDAOImpl;
import storybook.model.hbn.entity.Chapter;
import storybook.model.hbn.entity.Internal;
import storybook.model.hbn.entity.Part;
import storybook.model.hbn.entity.Scene;
import storybook.toolkit.DocumentUtil;
import storybook.toolkit.I18N;
import storybook.toolkit.ViewUtil;
import storybook.toolkit.swing.SwingUtil;
import storybook.view.AbstractScrollPanel;
import storybook.view.MainFrame;
import storybook.view.SbView;
import storybook.view.options.BookOptionsDialog;

/**
 * @author martin
 *
 */
@SuppressWarnings("serial")
public class BookPanel extends AbstractScrollPanel {

	public BookPanel(MainFrame mainFrame) {
		// don't call super constructor here!
		this.mainFrame = mainFrame;
	}

	protected void setZoomValue(int val){
		DocumentUtil.storeInternal(mainFrame, InternalKey.BOOK_ZOOM, val);
		mainFrame.getDocumentController().bookSetZoom(val);
	}

	protected int getZoomValue(){
		Internal internal = DocumentUtil.restoreInternal(mainFrame,
				InternalKey.BOOK_ZOOM, SbConstants.DEFAULT_BOOK_ZOOM);
		return internal.getIntegerValue();
	}

	protected int getMinZoomValue() {
		return SbConstants.MIN_BOOK_ZOOM;
	}

	protected int getMaxZoomValue() {
		return SbConstants.MAX_BOOK_ZOOM;
	}

	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
		Object newValue = evt.getNewValue();
		Object oldValue = evt.getOldValue();

		if (DocumentController.SceneProps.INIT.check(propName)) {
			refresh();
			return;
		}

		if (DocumentController.CommonProps.REFRESH.check(propName)) {
			SbView newView = (SbView) evt.getNewValue();
			SbView view = (SbView) getParent().getParent();
			if (view == newView) {
				refresh();
			}
			return;
		}

		if (DocumentController.CommonProps.SHOW_OPTIONS.check(propName)) {
			View view = (View) evt.getNewValue();
			if (!view.getName().equals(ViewName.BOOK.toString())) {
				return;
			}
			BookOptionsDialog dlg = new BookOptionsDialog(mainFrame);
			SwingUtil.showModalDialog(dlg, this);
			return;
		}

		if (DocumentController.BookViewProps.SHOW_ENTITY.check(propName)) {
			if (newValue instanceof Scene) {
				Scene scene = (Scene) newValue;
				ViewUtil.scrollToScene(this, panel, scene);
				return;
			}
			if (newValue instanceof Chapter) {
				Chapter chapter = (Chapter) newValue;
				ViewUtil.scrollToChapter(this, panel, chapter);
				return;
			}
		}

		if (DocumentController.SceneProps.UPDATE.check(propName)) {
			Scene oldScene = (Scene) oldValue;
			Scene newScene = (Scene) newValue;
			if (oldScene.getId() != newScene.getId()) {
				return;
			}
			if (!oldScene.getChapterSceneNo().equals(
					newScene.getChapterSceneNo())) {
				refresh();
				return;
			}
		}

		if (DocumentController.StrandProps.DELETE.check(propName)) {
			refresh();
			return;
		}

		if (DocumentController.PartProps.CHANGE.check(propName)) {
			refresh();
			ViewUtil.scrollToTop(scroller);
			return;
		}

		dispatchToBookInfoPanels(this, evt);
		dispatchToBookTextPanels(this, evt);
	}

	@Override
	public void init() {
	}

	@Override
	public void initUi() {
		setLayout(new MigLayout("flowy, ins 0"));

		MigLayout layout  = new MigLayout(
				"flowy",
				"[grow,center]",
				""
				);
		panel = new JPanel(layout);
		panel.setBackground(SwingUtil.getBackgroundColor());
		scroller = new JScrollPane(panel);

		SwingUtil.setUnitIncrement(scroller);
		SwingUtil.setMaxPreferredSize(scroller);
		add(scroller, "grow");

		refresh();
		ViewUtil.scrollToTop(scroller, 800);

		registerKeyboardAction();
		panel.addMouseWheelListener(this);

		revalidate();
		repaint();
	}

	@Override
	public void refresh() {
		Part currentPart = mainFrame.getCurrentPart();
		DocumentModel model = mainFrame.getDocumentModel();
		Session session = model.beginTransaction();
		ChapterDAOImpl dao = new ChapterDAOImpl(session);
		List<Chapter> chapters = dao
				.findAllOrderByChapterNoAndSceneNo(currentPart);
		model.commit();

		panel.removeAll();
		for (Chapter chapter : chapters) {
			session = model.beginTransaction();
			dao = new ChapterDAOImpl(session);
			List<Scene> scenes = dao.findScenes(chapter);
			model.commit();
			for (Scene scene : scenes) {
				BookScenePanel scenePanel = new BookScenePanel(mainFrame, scene);
				panel.add(scenePanel, "sgx");
			}
		}
		if (panel.getComponentCount() == 0) {
			panel.add(new JLabel(I18N.getMsg("msg.warning.no.scenes")));
		}
		panel.revalidate();
	}

	private static void dispatchToBookInfoPanels(Container cont,
			PropertyChangeEvent evt) {
		List<Component> ret = new ArrayList<Component>();
		SwingUtil.findComponentsByClass(cont, BookInfoPanel.class, ret);
		for (Component comp : ret) {
			BookInfoPanel panel = (BookInfoPanel) comp;
			panel.modelPropertyChange(evt);
		}
	}

	private static void dispatchToBookTextPanels(Container cont,
			PropertyChangeEvent evt) {
		List<Component> ret = new ArrayList<Component>();
		SwingUtil.findComponentsByClass(cont, BookTextPanel.class, ret);
		for (Component comp : ret) {
			BookTextPanel panel = (BookTextPanel) comp;
			panel.modelPropertyChange(evt);
		}
	}

	public JPanel getPanel(){
		return panel;
	}
}
