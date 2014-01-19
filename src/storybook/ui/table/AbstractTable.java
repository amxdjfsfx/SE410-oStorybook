package storybook.ui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.DefaultRowSorter;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.RowSorter;
import javax.swing.RowSorter.SortKey;
import javax.swing.SortOrder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import net.infonode.docking.View;
import net.miginfocom.swing.MigLayout;

import org.hibernate.Session;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.TableColumnExt;
import storybook.SbConstants;
import storybook.SbConstants.ActionCommand;
import storybook.SbConstants.ClientPropertyName;
import storybook.SbConstants.ComponentName;
import storybook.action.DeleteEntityAction;
import storybook.controller.DocumentController;
import storybook.model.DocumentModel;
import storybook.model.EntityUtil;
import storybook.model.handler.AbstractEntityHandler;
import storybook.model.hbn.dao.SbGenericDAOImpl;
import storybook.model.hbn.entity.AbstractEntity;
import storybook.model.hbn.entity.Category;
import storybook.model.hbn.entity.Chapter;
import storybook.model.hbn.entity.Gender;
import storybook.model.hbn.entity.Part;
import storybook.model.hbn.entity.Person;
import storybook.model.hbn.entity.Strand;
import storybook.model.state.IdeaState;
import storybook.model.state.SceneState;
import storybook.toolkit.I18N;
import storybook.toolkit.comparator.SafeDateComparator;
import storybook.toolkit.comparator.StringIntegerComparator;
import storybook.toolkit.swing.IconButton;
import storybook.toolkit.swing.SwingUtil;
import storybook.toolkit.swing.verifier.IntegerVerifier;
import storybook.toolkit.swing.verifier.VerifierGroup;
import storybook.ui.panel.AbstractPanel;
import storybook.ui.MainFrame;
import storybook.ui.table.SbColumn.InputType;
import storybook.ui.dialog.ConfirmMultiDeletionDlg;
import storybook.ui.table.renderer.ColorTableCellRenderer;
import storybook.ui.table.renderer.DateTableCellRenderer;

@SuppressWarnings("serial")
public abstract class AbstractTable extends AbstractPanel implements
		ActionListener, ListSelectionListener {

	private boolean trace=false;

	protected Vector<SbColumn> columns;

//	protected MainFrame mainFrame;
	protected DocumentController ctrl;

	protected JPanel optionsPanel;

	protected JXTable table;
	protected DefaultTableModel tableModel;

	protected boolean hasOrder = false;
	protected boolean allowMultiDelete = true;

	private JButton btNew;
	private JButton btDelete;
	private JButton btEdit;
	private JButton btCopy;
	private IconButton btOrderUp;
	private IconButton btOrderDown;

	public AbstractTable(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		ctrl = mainFrame.getDocumentController();
	}

	abstract protected AbstractEntity getNewEntity();

	abstract protected AbstractEntity getEntity(Long id);

	abstract protected void sendSetEntityToEdit(int row);

	abstract protected void sendSetNewEntityToEdit(AbstractEntity entity);

	abstract protected void sendDeleteEntity(int row);

	abstract protected void sendDeleteEntities(int[] rows);

	abstract protected void modelPropertyChangeLocal(PropertyChangeEvent evt);

	protected void sendOrderUpEntity(int row) {
	}

	protected void sendOrderDownEntity(int row) {
	}

	protected void initOptionsPanel() {
	}

	@SuppressWarnings("unchecked")
	protected List<AbstractEntity> getAllEntities() {
		AbstractEntityHandler handler = EntityUtil.getEntityHandler(mainFrame,
				getNewEntity());
		DocumentModel model = mainFrame.getDocumentModel();
		Session session = model.beginTransaction();
		SbGenericDAOImpl<?, ?> dao = handler.createDAO();
		dao.setSession(session);
		List<AbstractEntity> ret = (List<AbstractEntity>) dao.findAll();
		model.commit();
		return ret;
	}

	@Override
	public void modelPropertyChange(PropertyChangeEvent evt) {
		String propName = evt.getPropertyName();
		if (DocumentController.CommonProps.REFRESH.check(propName)) {
			View newView = (View) evt.getNewValue();
			View view = (View) getParent().getParent();
			if (view == newView) {
				HashMap<Integer, Boolean> visible = new HashMap<Integer, Boolean>();
				for (SbColumn col : getColumns()) {
					TableColumnExt ext = table.getColumnExt(col.toString());
					if (ext != null) {
						visible.put(col.getColId(), ext.isVisible());
					}
				}
				refresh();
				for (SbColumn col : getColumns()) {
					TableColumnExt ext = table.getColumnExt(col.toString());
					if (ext == null) {
						continue;
					}
					int key = col.getColId();
					if (visible.containsKey(key)) {
						if (visible.get(key)) {
							ext.setVisible(true);
						} else {
							ext.setVisible(false);
						}
					}
				}
			}
		}

		modelPropertyChangeLocal(evt);

		SwingUtil.forceRevalidate(this);
	}

	@Override
	public void initUi() {
		setLayout(new MigLayout("fill,wrap"));

		optionsPanel = new JPanel(new MigLayout("flowx"));
		initOptionsPanel();

		Vector<String> colNames = getColumnNames();
		tableModel = new DefaultTableModel(colNames, 0);
		table = new JXTable();
		table.setModel(tableModel);

		// renderer and comparators
		for (SbColumn col : getColumns()) {
			TableColumn tcol = table.getColumn(col.toString());
			TableCellRenderer renderer = null;
			if (col.getInputType() == InputType.DATE) {
				renderer = new DateTableCellRenderer();
			} else if (col.getInputType() == InputType.COLOR) {
				renderer = new ColorTableCellRenderer();
			} else if (col.hasTableCellRenderer()) {
				renderer = col.getTableCellRenderer();
			}
			if (renderer != null) {
				tcol.setCellRenderer(renderer);
			}
			TableColumnExt ext = table.getColumnExt(col.toString());
			if (ext != null) {
				// comparators
				if (col.hasComparator()) {
					ext.setComparator(col.getComparator());
				} else {
					if (col.getInputType() == InputType.DATE) {
						ext.setComparator(new SafeDateComparator());
					} else if (col.hasVerifier()) {
						if (col.getVerifier() instanceof IntegerVerifier) {
							ext.setComparator(new StringIntegerComparator());
						} else if (col.getVerifier() instanceof VerifierGroup) {
							VerifierGroup gr = (VerifierGroup) col
									.getVerifier();
							if (gr.isInteger()) {
								ext.setComparator(new StringIntegerComparator());
							}
						}
					}
				}
				// visible on start
				if (col.isHideOnStart()) {
					ext.setVisible(false);
				}
			}
			if (col.isDefaultSort()) {
				table.setSortOrder(col.toString(), SortOrder.ASCENDING);
			}
			if (col.getInputType() == InputType.TEXTAREA) {
				tcol.setPreferredWidth(400);
			}
			tcol.setMaxWidth(800);
		}

		table.setColumnControlVisible(true);
		table.setShowGrid(false, false);
		table.setHorizontalScrollEnabled(true);

		table.addHighlighter(HighlighterFactory.createSimpleStriping());

		table.setEditable(false);
		table.addMouseListener(new MyMouseAdapter());
		table.getSelectionModel().addListSelectionListener(this);

		// hot keys
		table.registerKeyboardAction(this,
				SbConstants.ActionCommand.EDIT.toString(),
				SwingUtil.getKeyStrokeEnter(), JComponent.WHEN_FOCUSED);
		table.registerKeyboardAction(this,
				SbConstants.ActionCommand.NEW.toString(),
				SwingUtil.getKeyStrokeInsert(), JComponent.WHEN_FOCUSED);
		table.registerKeyboardAction(this,
				SbConstants.ActionCommand.COPY.toString(),
				SwingUtil.getKeyStrokeCopy(), JComponent.WHEN_FOCUSED);
		table.registerKeyboardAction(this,
				SbConstants.ActionCommand.DELETE.toString(),
				SwingUtil.getKeyStrokeDelete(), JComponent.WHEN_FOCUSED);

		// column widths
		for (SbColumn col : getColumns()) {
			try {
				TableColumn tcol = table.getColumn(col.toString());
				tcol.setPreferredWidth(col.getWidth());
				tcol.setMinWidth(40);
			} catch (IllegalArgumentException e) {
				// ignore;
			}
		}

		JScrollPane scroller = new JScrollPane(table);
		scroller.setPreferredSize(new Dimension(Integer.MAX_VALUE,
				Integer.MAX_VALUE));

		btNew = new JButton(I18N.getMsg("msg.common.new"));
		btNew.setIcon(I18N.getIcon("icon.small.new"));
		btNew.setName(ComponentName.BT_NEW.toString());
		btNew.addActionListener(this);

		btDelete = new JButton(I18N.getMsg("msg.common.delete"));
		btDelete.setIcon(I18N.getIcon("icon.small.delete"));
		btDelete.setName(ComponentName.BT_DELETE.toString());
		btDelete.addActionListener(this);
		btDelete.setEnabled(false);

		btEdit = new JButton(I18N.getMsg("msg.common.edit"));
		btEdit.setIcon(I18N.getIcon("icon.small.edit"));
		btEdit.setName(ComponentName.BT_EDIT.toString());
		btEdit.addActionListener(this);
		btEdit.setEnabled(false);

		btCopy = new JButton(I18N.getMsg("msg.common.copy"));
		btCopy.setIcon(I18N.getIcon("icon.small.copy"));
		btCopy.setName(ComponentName.BT_COPY.toString());
		btCopy.addActionListener(this);
		btCopy.setEnabled(false);

		if (hasOrder) {
			btOrderUp = new IconButton("icon.small.arrow.up");
			btOrderUp.setToolTipText(I18N.getMsg("msg.order.up"));
			btOrderUp.setName(ComponentName.BT_ORDER_UP.toString());
			btOrderUp.addActionListener(this);

			btOrderDown = new IconButton("icon.small.arrow.down");
			btOrderDown.setToolTipText(I18N.getMsg("msg.order.down"));
			btOrderDown.setName(ComponentName.BT_ORDER_DOWN.toString());
			btOrderDown.addActionListener(this);
		}

		// layout
		if (optionsPanel.getComponentCount() > 0) {
			add(optionsPanel, "growx");
		}
		add(scroller, "grow");
		String split = "split 4";
		if (hasOrder) {
			split = "split 6";
		}
		add(btNew, "span," + split + ",sg");
		add(btEdit, "sg");
		add(btCopy, "sg");
		add(btDelete, "sg");
		if (hasOrder) {
			add(btOrderUp, "gap push,sg 2");
			add(btOrderDown, "sg 2");
		}
	}

	protected void initTableModel(PropertyChangeEvent evt) {
		table.putClientProperty(ClientPropertyName.MAIN_FRAME.toString(),
				mainFrame);

		// clear table
		for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
			tableModel.removeRow(i);
		}
		// fill in data
		try {
			// old
			// @SuppressWarnings("unchecked")
			// List<AbstractEntity> entities = (List<AbstractEntity>) evt
			// .getNewValue();
			List<AbstractEntity> entities = getAllEntities();

			for (AbstractEntity entity : entities) {
				// show only scenes from current part
				// if (entity instanceof Scene) {
				// Part currentPart = mainFrame.getCurrentPart();
				// Scene scene = (Scene) entity;
				// if (scene.hasChapter()) {
				// if (scene.getChapter().getPart().getId() != currentPart
				// .getId()) {
				// continue;
				// }
				// }
				// }
				Vector<Object> cols = getRow(entity);
				tableModel.addRow(cols);
			}
		} catch (ClassCastException e) {
		}

		table.packAll();
	}

	protected List<SbColumn> getColumns() {
		return columns;
	}

	private Vector<String> getColumnNames() {
		Vector<String> cols = new Vector<String>();
		for (SbColumn col : getColumns()) {
			cols.add(col.toString());
		}
		return cols;
	}

	protected Vector<Object> getRow(AbstractEntity entity) {
		DocumentModel model = mainFrame.getDocumentModel();
		Session session = model.beginTransaction();
		session.refresh(entity);
		Vector<Object> cols = new Vector<Object>();
		for (SbColumn col : getColumns()) {
			try {
				String methodName = "get" + col.getMethodName();
				Method method = entity.getClass().getMethod(methodName);
				Object ret = method.invoke(entity);
				if (ret == null) {
					cols.add("");
				} else if (ret instanceof Timestamp) {
					Timestamp ts = (Timestamp) ret;
					Date date = new Date(ts.getTime());
					cols.add(date);
				} else if (ret instanceof Date || ret instanceof Color
						|| ret instanceof SceneState
						|| ret instanceof IdeaState || ret instanceof Strand
						|| ret instanceof Chapter || ret instanceof Part
						|| ret instanceof Gender || ret instanceof Category
						|| ret instanceof Person || ret instanceof Icon) {
					cols.add(ret);
				} else if (ret instanceof List) {
					@SuppressWarnings("unchecked")
					List<AbstractEntity> list = (List<AbstractEntity>) ret;
					cols.add(list);
				} else if (ret instanceof Long) {
					if (col.hasTableCellRenderer()) {
						cols.add(ret);
					} else {
						cols.add(ret.toString());
					}
				} else {
					cols.add(ret.toString());
				}
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			}
		}
		model.commit();
		return cols;
	}

	protected void updateEntity(PropertyChangeEvent evt) {
		AbstractEntity entity = (AbstractEntity) evt.getNewValue();
		for (int row = 0; row < tableModel.getRowCount(); ++row) {
			Long id = Long.parseLong((String) tableModel.getValueAt(row, 0));
			if (!id.equals(entity.getId())) {
				continue;
			}
			Vector<Object> rowVector = getRow(entity);
			int col = 0;
			for (Object val : rowVector) {
				tableModel.setValueAt(val, row, col);
				++col;
			}
		}
	}

	protected void orderUpEntity(PropertyChangeEvent evt) {
	}

	protected void orderDownEntity(PropertyChangeEvent evt) {
	}

	protected void sortByColumn(int col) {
		DefaultRowSorter<?, ?> sorter = ((DefaultRowSorter<?, ?>) table
				.getRowSorter());
		ArrayList<SortKey> list = new ArrayList<SortKey>();
		list.add(new RowSorter.SortKey(col, SortOrder.ASCENDING));
		sorter.setSortKeys(list);
		sorter.sort();
	}

	protected void newEntity(PropertyChangeEvent evt) {
		AbstractEntity entity = (AbstractEntity) evt.getNewValue();
		Vector<Object> cols = getRow(entity);
		tableModel.addRow(cols);
		tableModel.fireTableDataChanged();
	}

	protected synchronized void deleteEntity(PropertyChangeEvent evt) {
		AbstractEntity entity = (AbstractEntity) evt.getOldValue();
		for (int row = 0; row < tableModel.getRowCount(); ++row) {
			Long id = Long.parseLong((String) tableModel.getValueAt(row, 0));
			if (id.equals(entity.getId())) {
				tableModel.removeRow(row);
				break;
			}
		}
		tableModel.fireTableDataChanged();
	}

	protected synchronized AbstractEntity getEntityFromRow(int row) {
		if (row == -1) {
			return null;
		}
		try {
			int modelIndex = table.getRowSorter().convertRowIndexToModel(row);
			@SuppressWarnings("unchecked")
			Vector<Vector<String>> dataVector = tableModel.getDataVector();
			Long id = Long.parseLong(dataVector.get(modelIndex).get(0));
			return getEntity(id);
		} catch (Exception ex) {
		}
		return null;
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e) {
		if (trace) {System.out.println("AbstractTable.actionPerformed("+e.toString());}
		String actCmd = e.getActionCommand();
		Component comp = (Component) e.getSource();
		String compName = comp.getName();
		int row = table.getSelectedRow();
		if (trace) {
			System.out.println("actCmd="+actCmd
				+",comp="+compName
				+",row="+row);}

		if (ComponentName.BT_EDIT.check(compName)
				|| ActionCommand.EDIT.check(actCmd)) {
			sendSetEntityToEdit(row);
			return;
		}

		if (ComponentName.BT_NEW.check(compName)
				|| ActionCommand.NEW.check(actCmd)) {
			table.clearSelection();
			sendSetNewEntityToEdit(getNewEntity());
			return;
		}

		if (ComponentName.BT_COPY.check(compName)
				|| ActionCommand.COPY.check(actCmd)) {
			AbstractEntity entity = (AbstractEntity) getEntityFromRow(row);
			EntityUtil.copyEntity(mainFrame, entity);
			return;
		}

		if (ComponentName.BT_DELETE.check(compName)
				|| ActionCommand.DELETE.check(actCmd)) {
			if (table.getSelectedRowCount() == 1) {
				// delete one entity
				AbstractEntity entity = (AbstractEntity) getEntityFromRow(row);
				DeleteEntityAction act = new DeleteEntityAction(mainFrame,
						entity);
				act.actionPerformed(null);
				return;
			}
			if (table.getSelectedRowCount() > 1) {
				// delete multiple entities
				List<AbstractEntity> entities = new ArrayList<AbstractEntity>();
				int[] rows = table.getSelectedRows();
				for (int row2 : rows) {
					AbstractEntity entity = getEntityFromRow(row2);
					List<Long> readOnlyIds = EntityUtil.getReadOnlyIds(entity);
					if (!readOnlyIds.contains(entity.getId())) {
						entities.add(entity);
					}
				}
				ConfirmMultiDeletionDlg dlg = new ConfirmMultiDeletionDlg(
						mainFrame, entities);
				SwingUtil.showModalDialog(dlg, mainFrame);
				if (dlg.isCanceled()) {
					return;
				}
				sendDeleteEntities(rows);
				return;
			}
		}

		if (ComponentName.BT_ORDER_UP.check(compName)) {
			sendOrderUpEntity(row);
			return;
		}

		if (ComponentName.BT_ORDER_DOWN.check(compName)) {
			sendOrderDownEntity(row);
//			return;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) e
				.getSource();
		int count = selectionModel.getMaxSelectionIndex()
				- selectionModel.getMinSelectionIndex() + 1;

		if (count > 1) {
			btEdit.setEnabled(false);
			btCopy.setEnabled(false);
			btDelete.setEnabled(allowMultiDelete);
			if (hasOrder) {
				btOrderUp.setEnabled(false);
				btOrderDown.setEnabled(false);
			}
			return;
		}

		int row = selectionModel.getMinSelectionIndex();
		AbstractEntity entity = getEntityFromRow(row);
		if (entity == null) {
			btEdit.setEnabled(false);
			btCopy.setEnabled(false);
			btDelete.setEnabled(false);
			if (hasOrder) {
				btOrderUp.setEnabled(false);
				btOrderDown.setEnabled(false);
			}
			return;
		}

		btEdit.setEnabled(true);
		btCopy.setEnabled(true);
		btDelete.setEnabled(true);
		if (hasOrder) {
			btOrderUp.setEnabled(true);
			btOrderDown.setEnabled(true);
		}
	}

	private class MyMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) table
						.getSelectionModel();
				int count = selectionModel.getMaxSelectionIndex()
						- selectionModel.getMinSelectionIndex() + 1;
				if (count > 1) {
					return;
				}
				int row = selectionModel.getMinSelectionIndex();
				sendSetEntityToEdit(row);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopup(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger()) {
				showPopup(e);
			}
		}

		private void showPopup(MouseEvent e) {
			if (!(e.getSource() instanceof JXTable)) {
				return;
			}
			JXTable source = (JXTable) e.getSource();
			int row = source.rowAtPoint(e.getPoint());
			int column = source.columnAtPoint(e.getPoint());
			if (!source.isRowSelected(row)) {
				source.changeSelection(row, column, false, false);
			}
			AbstractEntity entity = getEntityFromRow(row);
			if (entity == null) {
				return;
			}
			JPopupMenu popup = EntityUtil.createPopupMenu(mainFrame, entity);
			popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}
}