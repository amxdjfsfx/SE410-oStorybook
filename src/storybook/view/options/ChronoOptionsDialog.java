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

package storybook.view.options;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import storybook.SbConstants;
import storybook.SbConstants.InternalKey;
import storybook.model.hbn.entity.Internal;
import storybook.toolkit.DocumentUtil;
import storybook.toolkit.I18N;
import storybook.view.MainFrame;

/**
 * @author martin
 *
 */
@SuppressWarnings("serial")
public class ChronoOptionsDialog extends AbstractOptionsDialog implements
		ItemListener {

	private final String CN_LAYOUT_DIRECTION = "CbLayoutDirection";
	private final String CN_DATE_DIFFERENCE = "CbDateDifference";

	private boolean layoutDirection;
	private boolean showDateDifference;

	public ChronoOptionsDialog(MainFrame mainFrame) {
		super(mainFrame);
	}

	public ChronoOptionsDialog(MainFrame mainFrame, boolean hasZoom) {
		super(mainFrame, hasZoom);
	}

	@Override
	public void init() {
		setZoomMinValue(SbConstants.MIN_CHRONO_ZOOM);
		setZoomMaxValue(SbConstants.MAX_CHRONO_ZOOM);
		try {
			Internal internal = DocumentUtil.restoreInternal(mainFrame,
					InternalKey.CHRONO_ZOOM, SbConstants.DEFAULT_CHRONO_ZOOM);
			zoomValue = internal.getIntegerValue();
			internal = DocumentUtil.restoreInternal(mainFrame,
					InternalKey.CHRONO_LAYOUT_DIRECTION,
					SbConstants.DEFAULT_CHRONO_LAYOUT_DIRECTION);
			layoutDirection = internal.getBooleanValue();
			internal = DocumentUtil.restoreInternal(mainFrame,
					InternalKey.CHRONO_SHOW_DATE_DIFFERENCE,
					SbConstants.DEFAULT_CHRONO_SHOW_DATE_DIFFERENCE);
			showDateDifference = internal.getBooleanValue();
		} catch (Exception e) {
			e.printStackTrace();
			zoomValue = SbConstants.DEFAULT_CHRONO_ZOOM;
			layoutDirection = SbConstants.DEFAULT_CHRONO_LAYOUT_DIRECTION;
			showDateDifference = SbConstants.DEFAULT_CHRONO_SHOW_DATE_DIFFERENCE;
		}
	}

	@Override
	public void initUi() {
		// layout direction
		JLabel lbLayoutDirection = new JLabel(
				I18N.getMsgColon("msg.statusbar.change.layout.direction"));
		JCheckBox cbLayoutDirection = new JCheckBox();
		cbLayoutDirection.setName(CN_LAYOUT_DIRECTION);
		cbLayoutDirection.addItemListener(this);
		cbLayoutDirection
				.setText(I18N.getMsg("msg.common.horizontal.vertical"));
		cbLayoutDirection.setOpaque(false);
		cbLayoutDirection.setSelected(layoutDirection);

		// show date difference
		JLabel lbDateDiff = new JLabel(I18N.getMsgColon("msg.pref.datediff"));
		JCheckBox cbDateDiff = new JCheckBox();
		cbDateDiff.setName(CN_DATE_DIFFERENCE);
		cbDateDiff.addItemListener(this);
		cbDateDiff.setText(I18N.getMsg("msg.pref.datediff.show"));
		cbDateDiff.setOpaque(false);
		cbDateDiff.setSelected(showDateDifference);

		// layout
		panel.add(lbLayoutDirection);
		panel.add(cbLayoutDirection);
		panel.add(lbDateDiff);
		panel.add(cbDateDiff);
	}

	@Override
	protected void zoom(int val) {
		DocumentUtil.storeInternal(mainFrame, InternalKey.CHRONO_ZOOM, val);
		mainFrame.getDocumentController().chronoSetZoom(val);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		JCheckBox cb = (JCheckBox) e.getSource();
		boolean val = cb.isSelected();

		if (cb.getName().equals(CN_LAYOUT_DIRECTION)) {
			mainFrame.getDocumentController().chronoSetLayoutDirection(val);
			DocumentUtil.storeInternal(mainFrame, InternalKey.CHRONO_LAYOUT_DIRECTION,
					val);
			return;
		}

		if (cb.getName().equals(CN_DATE_DIFFERENCE)) {
			mainFrame.getDocumentController().chronoSetShowDateDifference(
					cb.isSelected());
			DocumentUtil.storeInternal(mainFrame,
					InternalKey.CHRONO_SHOW_DATE_DIFFERENCE, val);
			return;
		}
	}
}
