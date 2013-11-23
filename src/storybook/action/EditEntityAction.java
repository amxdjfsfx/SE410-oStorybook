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

package storybook.action;

import java.awt.event.ActionEvent;

import storybook.SbConstants.ViewName;
import storybook.controller.DocumentController;
import storybook.model.hbn.entity.AbstractEntity;
import storybook.toolkit.I18N;
import storybook.view.MainFrame;

/**
 * @author martin
 *
 */
@SuppressWarnings("serial")
public class EditEntityAction extends AbstractEntityAction {

	public EditEntityAction(MainFrame mainFrame, AbstractEntity entity) {
		super(mainFrame, entity, I18N.getMsg("msg.common.edit"), I18N
				.getIcon("icon.small.edit"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DocumentController ctrl = mainFrame.getDocumentController();
		ctrl.setEntityToEdit(entity);
		mainFrame.showView(ViewName.EDITOR);
	}
}