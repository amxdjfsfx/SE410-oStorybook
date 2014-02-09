/*
 * SbApp: Open Source software for novelists and authors.
 * Original idea 2008 - 2012 Martin Mustun
 * Copyrigth (C) Favdb
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package storybook.ui.dialog.edit;

import java.awt.CardLayout;
import javax.swing.JPanel;
import storybook.model.hbn.entity.Scene;
import storybook.ui.dialog.chooser.DateFixed;
import storybook.ui.dialog.chooser.DateRelative;
import storybook.ui.dialog.CommonBox;

/**
 *
 * @author favdb
 */
public class EditScene extends javax.swing.JPanel {
	private Editor parent;
	private CardLayout card = new CardLayout(0, 0);
	private DateFixed dateChooser;
	private DateRelative dateRelative;
	private Scene scene;

	/**
	 * Creates new form EditScene
	 */
	public EditScene() {
		initComponents();
	}

	public EditScene(Editor m, Scene s) {
		parent=m;
		dateChooser=new DateFixed(this);
		dateRelative=new DateRelative(this);
		initComponents();
		jPanel1.setLayout(card);
		jPanel1.add(new JPanel(),"none");
		jPanel1.add(dateChooser,"dateFixed");
		jPanel1.add(dateRelative,"dateRelative");
		scene=s;
		if ((scene==null) || (scene.getId()==-1L)) {
			card.show(jPanel1, "none");
		} else {
			initUI();
		}
	}
	private void initUI() {
		if (scene!=null) {
			txtID.setText(Long.toString(scene.getId()));
			txtNumber.setText(scene.getChapterSceneNo());
			txtTitle.setText(scene.getTitle());
		}
		CommonBox.loadCbChapters(parent.parent,cbChapters, scene);
		CommonBox.loadCbStatus(cbStatus, scene);
		CommonBox.loadLbStrands(lbStrands, scene);
		CommonBox.loadLbPersons(lbPersons, scene);
		CommonBox.loadLbLocations(lbLocations, scene);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        paneCommon = new javax.swing.JPanel();
        lbId = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        lbNumber = new javax.swing.JLabel();
        txtNumber = new javax.swing.JTextField();
        lbTitle = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        lbStatus = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox();
        lbDate = new javax.swing.JLabel();
        rbNone = new javax.swing.JRadioButton();
        rbFixed = new javax.swing.JRadioButton();
        rbRelative = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lbStrands = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lbPersons = new javax.swing.JList();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lbLocations = new javax.swing.JList();
        lbChapter = new javax.swing.JLabel();
        cbChapters = new javax.swing.JComboBox();
        panNotes = new javax.swing.JPanel();
        paneText = new javax.swing.JPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/storybook/i18n/messages"); // NOI18N
        lbId.setText(bundle.getString("ID")); // NOI18N

        txtID.setEditable(false);
        txtID.setText(" ");

        lbNumber.setText(bundle.getString("NUMBER")); // NOI18N

        txtNumber.setText(" ");

        lbTitle.setText(bundle.getString("TITLE")); // NOI18N

        lbStatus.setText(bundle.getString("STATUS")); // NOI18N

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbDate.setText(bundle.getString("DATE")); // NOI18N

        buttonGroup1.add(rbNone);
        rbNone.setSelected(true);
        rbNone.setText(bundle.getString("NONE")); // NOI18N
        rbNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNoneActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbFixed);
        rbFixed.setText(bundle.getString("DATE_FIXED")); // NOI18N
        rbFixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFixedActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbRelative);
        rbRelative.setText(bundle.getString("DATE_RELATIVE")); // NOI18N
        rbRelative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRelativeActionPerformed(evt);
            }
        });

        jPanel1.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.highlight"));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 88, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("STRANDS"))); // NOI18N

        lbStrands.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lbStrands);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("CHARACTERS"))); // NOI18N

        lbPersons.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lbPersons);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("LOCATION"))); // NOI18N

        lbLocations.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lbLocations.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(lbLocations);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );

        lbChapter.setText(bundle.getString("CHAPTER")); // NOI18N

        cbChapters.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout paneCommonLayout = new javax.swing.GroupLayout(paneCommon);
        paneCommon.setLayout(paneCommonLayout);
        paneCommonLayout.setHorizontalGroup(
            paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneCommonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneCommonLayout.createSequentialGroup()
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbNumber)
                            .addComponent(lbTitle)
                            .addComponent(lbId))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneCommonLayout.createSequentialGroup()
                                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtID, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                    .addComponent(txtNumber))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbStatus)
                                    .addComponent(lbChapter))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneCommonLayout.createSequentialGroup()
                                        .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(cbChapters, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(txtTitle)))
                    .addGroup(paneCommonLayout.createSequentialGroup()
                        .addComponent(lbDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(paneCommonLayout.createSequentialGroup()
                                .addComponent(rbNone)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbFixed)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(rbRelative)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(paneCommonLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        paneCommonLayout.setVerticalGroup(
            paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneCommonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbId)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbChapter)
                    .addComponent(cbChapters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbNumber)
                    .addComponent(txtNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbStatus)
                    .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbTitle)
                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbDate)
                    .addComponent(rbNone)
                    .addComponent(rbFixed)
                    .addComponent(rbRelative))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("COMMON"), paneCommon); // NOI18N

        javax.swing.GroupLayout panNotesLayout = new javax.swing.GroupLayout(panNotes);
        panNotes.setLayout(panNotesLayout);
        panNotesLayout.setHorizontalGroup(
            panNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        panNotesLayout.setVerticalGroup(
            panNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(bundle.getString("NOTES"), panNotes); // NOI18N

        javax.swing.GroupLayout paneTextLayout = new javax.swing.GroupLayout(paneText);
        paneText.setLayout(paneTextLayout);
        paneTextLayout.setHorizontalGroup(
            paneTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        paneTextLayout.setVerticalGroup(
            paneTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 472, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(bundle.getString("TEXT"), paneText); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rbNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNoneActionPerformed
        card.show(jPanel1, "none");
    }//GEN-LAST:event_rbNoneActionPerformed

    private void rbFixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFixedActionPerformed
        card.show(jPanel1, "dateFixed");
    }//GEN-LAST:event_rbFixedActionPerformed

    private void rbRelativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRelativeActionPerformed
        card.show(jPanel1,"dateRelative");
    }//GEN-LAST:event_rbRelativeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbChapters;
    private javax.swing.JComboBox cbStatus;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbChapter;
    private javax.swing.JLabel lbDate;
    private javax.swing.JLabel lbId;
    private javax.swing.JList lbLocations;
    private javax.swing.JLabel lbNumber;
    private javax.swing.JList lbPersons;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JList lbStrands;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel panNotes;
    private javax.swing.JPanel paneCommon;
    private javax.swing.JPanel paneText;
    private javax.swing.JRadioButton rbFixed;
    private javax.swing.JRadioButton rbNone;
    private javax.swing.JRadioButton rbRelative;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtNumber;
    private javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables
}
