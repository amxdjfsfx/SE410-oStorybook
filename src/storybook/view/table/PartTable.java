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

package storybook.view.table;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.hibernate.Session;
import storybook.SbConstants.ViewName;
import storybook.controller.DocumentController;
import storybook.model.DocumentModel;
import storybook.model.hbn.dao.PartDAOImpl;
import storybook.model.hbn.entity.AbstractEntity;
import storybook.model.hbn.entity.Part;
import storybook.view.MainFrame;
import storybook.view.SbColumnFactory;

/**
 * @author martin
 *
 */
@SuppressWarnings("serial")
public class PartTable extends AbstractTable {

	public PartTable(MainFrame mainFrame) {
		super(mainFrame);
		allowMultiDelete = false;
	}

	@Override
	public void init() {
		columns = SbColumnFactory.getInstance().getPartColumns();
	}

	protected void modelPropertyChangeLocal(PropertyChangeEvent evt) {
		try {
			String propName = evt.getPropertyName();
			if (DocumentController.PartProps.INIT.check(propName)) {
				initTableModel(evt);
			} else if (DocumentController.PartProps.UPDATE.check(propName)) {
				updateEntity(evt);
			} else if (DocumentController.PartProps.NEW.check(propName)) {
				newEntity(evt);
			} else if (DocumentController.PartProps.DELETE.check(propName)) {
				deleteEntity(evt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void sendSetEntityToEdit(int row) {
		if (row == -1) {
			return;
		}
		Part part = (Part) getEntityFromRow(row);
		ctrl.setPartToEdit(part);
		mainFrame.showView(ViewName.EDITOR);
	}

	@Override
	protected void sendSetNewEntityToEdit(AbstractEntity entity) {
		ctrl.setPartToEdit((Part) entity);
		mainFrame.showView(ViewName.EDITOR);
	}

	@Override
	protected synchronized void sendDeleteEntity(int row) {
		Part part = (Part) getEntityFromRow(row);
		ctrl.deletePart(part);
	}

	@Override
	protected synchronized void sendDeleteEntities(int[] rows) {
		ArrayList<Long> ids = new ArrayList<Long>();
		for (int row : rows) {
			Part part = (Part) getEntityFromRow(row);
			ids.add(part.getId());
		}
		ctrl.deleteMultiParts(ids);
	}

	@Override
	protected AbstractEntity getEntity(Long id) {
		DocumentModel model = mainFrame.getDocumentModel();
		Session session = model.beginTransaction();
		PartDAOImpl dao = new PartDAOImpl(session);
		Part part = dao.find(id);
		model.commit();
		return part;
	}

	@Override
	protected AbstractEntity getNewEntity() {
		return new Part();
	}
}
