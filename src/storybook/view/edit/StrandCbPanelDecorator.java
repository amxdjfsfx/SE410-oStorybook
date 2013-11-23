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

package storybook.view.edit;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import storybook.model.hbn.entity.AbstractEntity;
import storybook.model.hbn.entity.Strand;

/**
 * @author martin
 *
 */
public class StrandCbPanelDecorator extends CbPanelDecorator {

	public StrandCbPanelDecorator() {
	}

	@Override
	public void decorateBeforeEntity(AbstractEntity entity) {
	}

	@Override
	public void decorateEntity(JCheckBox cb, AbstractEntity entity) {
		Strand strand = (Strand) entity;
		panel.add(new JLabel(strand.getColorIcon()), "split 2");
		panel.add(cb);
	}

	@Override
	public void decorateAfterEntity(AbstractEntity entity) {
	}
}