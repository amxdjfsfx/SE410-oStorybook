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

package storybook.view.dialog.file;

import storybook.model.DbFile;
import storybook.toolkit.I18N;
import storybook.view.MainFrame;

@SuppressWarnings("serial")
public class SaveAsFileDialog extends AbstractFileDialog {

	public SaveAsFileDialog(MainFrame mainFrame) {
		super(mainFrame);
		setTitle(I18N.getMsg("msg.file.save.as"));
		DbFile dbFile = mainFrame.getDbFile();
		file = dbFile.getFile();
		setDir(file.getParent());
		setFilename(dbFile.getName() + " (" + I18N.getMsg("msg.common.copy")
				+ ")");
	}
}
