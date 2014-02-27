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
import storybook.toolkit.swing.htmleditor.HtmlEditor;
import storybook.ui.dialog.chooser.DateFixed;
import storybook.ui.dialog.chooser.DateRelative;
import static storybook.ui.dialog.edit.DlgErrorMessage.mandatoryField;

/**
 *
 * @author favdb
 */
public class EditScene extends javax.swing.JPanel {

	private Editor parent;
	private final CardLayout card = new CardLayout(0, 0);
	private DateFixed dateFixed;
	private DateRelative dateRelative;
	private Scene scene;
	private final CardLayout cardText = new CardLayout(0, 0);
	private final CardLayout cardNotes = new CardLayout(0, 0);
	private final HtmlEditor notes = new HtmlEditor();
	private final HtmlEditor text = new HtmlEditor();

	/**
	 * Creates new form EditScene
	 */
	public EditScene() {
		initComponents();
	}

	public EditScene(Editor m, Scene s) {
		parent = m;
		scene = s;
		dateFixed = new DateFixed(this);
		dateRelative = new DateRelative(this);
		initComponents();
		paneDate.setLayout(card);
		paneDate.add(new JPanel(), "none");
		paneDate.add(dateFixed, "dateFixed");
		paneDate.add(dateRelative, "dateRelative");
		notes.setMaxLength(150);
		paneNotes.setLayout(cardNotes);
		paneNotes.add(notes);
		cardNotes.show(paneNotes, "notes");
		text.setMaxLength(150);
		paneText.setLayout(cardText);
		paneText.add(text);
		cardText.show(paneText, "text");
		initUI();
	}

	private void initUI() {
		if (scene == null)
			scene = createNewScene();
		txID.setText(Long.toString(scene.getId()));
		txNumber.setText(scene.getChapterSceneNo());
		txTitle.setText(scene.getTitle());
		CommonBox.loadCbChapters(parent.parent, cbChapters, scene);
		CommonBox.loadCbStatus(cbStatus, scene);
		CommonBox.loadCbLocations(parent.parent, cbLocations, scene);
		CommonBox.loadLbStrands(parent.parent, lbStrands, scene);
		CommonBox.loadLbPersons(parent.parent, lbPersons, scene);
		CommonBox.loadLbItems(parent.parent, lbItems, scene);
		//TODO date fixed or relative
		notes.setText(scene.getNotes());
		text.setText(scene.getText());
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
        txID = new javax.swing.JTextField();
        lbChapter = new javax.swing.JLabel();
        cbChapters = new javax.swing.JComboBox();
        lbNumber = new javax.swing.JLabel();
        txNumber = new javax.swing.JTextField();
        lbStatus = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox();
        lbTitle = new javax.swing.JLabel();
        txTitle = new javax.swing.JTextField();
        lbLocation = new javax.swing.JLabel();
        cbLocations = new javax.swing.JComboBox();
        lbDate = new javax.swing.JLabel();
        rbNone = new javax.swing.JRadioButton();
        rbFixed = new javax.swing.JRadioButton();
        rbRelative = new javax.swing.JRadioButton();
        paneDate = new javax.swing.JPanel();
        paneStrands = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lbStrands = new javax.swing.JList();
        panePersons = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lbPersons = new javax.swing.JList();
        paneItems = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        lbItems = new javax.swing.JList();
        bAddStrand = new javax.swing.JButton();
        bAddPersons = new javax.swing.JButton();
        bAddItem = new javax.swing.JButton();
        bAddLocation = new javax.swing.JButton();
        btClearChapter = new javax.swing.JButton();
        btClearLocation = new javax.swing.JButton();
        paneNotes = new javax.swing.JPanel();
        paneText = new javax.swing.JPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("storybook/resources/messages"); // NOI18N
        lbId.setText(bundle.getString("msg.common.id")); // NOI18N

        txID.setEditable(false);
        txID.setText(" ");

        lbChapter.setText(bundle.getString("msg.common.chapter")); // NOI18N

        cbChapters.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbNumber.setText(bundle.getString("msg.common.number")); // NOI18N

        txNumber.setText(" ");

        lbStatus.setText(bundle.getString("msg.common.status")); // NOI18N

        cbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbTitle.setText(bundle.getString("msg.common.title")); // NOI18N

        lbLocation.setText(bundle.getString("msg.common.location")); // NOI18N

        cbLocations.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lbDate.setText(bundle.getString("msg.common.date")); // NOI18N

        buttonGroup1.add(rbNone);
        rbNone.setSelected(true);
        rbNone.setText(bundle.getString("msg.common.none")); // NOI18N
        rbNone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbNoneActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbFixed);
        rbFixed.setText(bundle.getString("msg.common.fixed")); // NOI18N
        rbFixed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbFixedActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbRelative);
        rbRelative.setText(bundle.getString("msg.common.relative")); // NOI18N
        rbRelative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbRelativeActionPerformed(evt);
            }
        });

        paneDate.setBackground(javax.swing.UIManager.getDefaults().getColor("Button.highlight"));
        paneDate.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout paneDateLayout = new javax.swing.GroupLayout(paneDate);
        paneDate.setLayout(paneDateLayout);
        paneDateLayout.setHorizontalGroup(
            paneDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        paneDateLayout.setVerticalGroup(
            paneDateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 68, Short.MAX_VALUE)
        );

        paneStrands.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("msg.common.strands"))); // NOI18N

        lbStrands.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lbStrands.setMaximumSize(new java.awt.Dimension(32000, 32000));
        jScrollPane1.setViewportView(lbStrands);

        javax.swing.GroupLayout paneStrandsLayout = new javax.swing.GroupLayout(paneStrands);
        paneStrands.setLayout(paneStrandsLayout);
        paneStrandsLayout.setHorizontalGroup(
            paneStrandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
        );
        paneStrandsLayout.setVerticalGroup(
            paneStrandsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        );

        panePersons.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("msg.common.persons"))); // NOI18N

        lbPersons.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lbPersons);

        javax.swing.GroupLayout panePersonsLayout = new javax.swing.GroupLayout(panePersons);
        panePersons.setLayout(panePersonsLayout);
        panePersonsLayout.setHorizontalGroup(
            panePersonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        panePersonsLayout.setVerticalGroup(
            panePersonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        paneItems.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("msg.common.items"))); // NOI18N

        lbItems.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lbItems.setToolTipText("");
        jScrollPane3.setViewportView(lbItems);

        javax.swing.GroupLayout paneItemsLayout = new javax.swing.GroupLayout(paneItems);
        paneItems.setLayout(paneItemsLayout);
        paneItemsLayout.setHorizontalGroup(
            paneItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
        );
        paneItemsLayout.setVerticalGroup(
            paneItemsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
        );

        bAddStrand.setIcon(new javax.swing.ImageIcon(getClass().getResource("/storybook/resources/icons/16x16/plus.png"))); // NOI18N
        bAddStrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddStrandActionPerformed(evt);
            }
        });

        bAddPersons.setIcon(new javax.swing.ImageIcon(getClass().getResource("/storybook/resources/icons/16x16/plus.png"))); // NOI18N

        bAddItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/storybook/resources/icons/16x16/plus.png"))); // NOI18N
        bAddItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddItemActionPerformed(evt);
            }
        });

        bAddLocation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/storybook/resources/icons/16x16/plus.png"))); // NOI18N
        bAddLocation.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        bAddLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAddLocationActionPerformed(evt);
            }
        });

        btClearChapter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/storybook/resources/icons/16x16/clear.png"))); // NOI18N
        btClearChapter.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btClearChapter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearChapterActionPerformed(evt);
            }
        });

        btClearLocation.setIcon(new javax.swing.ImageIcon(getClass().getResource("/storybook/resources/icons/16x16/clear.png"))); // NOI18N
        btClearLocation.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btClearLocation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btClearLocationActionPerformed(evt);
            }
        });

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
                            .addComponent(lbId)
                            .addComponent(lbLocation))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txTitle, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, paneCommonLayout.createSequentialGroup()
                                .addComponent(cbLocations, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btClearLocation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bAddLocation))
                            .addGroup(paneCommonLayout.createSequentialGroup()
                                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txID, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                                    .addComponent(txNumber))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbStatus)
                                    .addComponent(lbChapter))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(paneCommonLayout.createSequentialGroup()
                                        .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 88, Short.MAX_VALUE))
                                    .addComponent(cbChapters, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btClearChapter))))
                    .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(paneCommonLayout.createSequentialGroup()
                            .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(paneStrands, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bAddStrand, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(bAddPersons, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                .addComponent(panePersons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(paneItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bAddItem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(paneCommonLayout.createSequentialGroup()
                            .addComponent(lbDate)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(rbNone)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(paneCommonLayout.createSequentialGroup()
                                    .addComponent(rbFixed)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(rbRelative)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(paneDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        paneCommonLayout.setVerticalGroup(
            paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneCommonLayout.createSequentialGroup()
                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(paneCommonLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbChapters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btClearChapter))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbNumber)
                            .addComponent(txNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbStatus)
                            .addComponent(cbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbTitle)
                            .addComponent(txTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(cbLocations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btClearLocation)
                            .addComponent(bAddLocation))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbDate)
                            .addComponent(rbNone)
                            .addComponent(rbFixed)
                            .addComponent(rbRelative))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(paneDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(paneItems, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(paneStrands, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panePersons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(paneCommonLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lbChapter))
                    .addGroup(paneCommonLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(txID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(paneCommonLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lbId))
                    .addGroup(paneCommonLayout.createSequentialGroup()
                        .addGap(107, 107, 107)
                        .addComponent(lbLocation)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneCommonLayout.createSequentialGroup()
                        .addComponent(bAddStrand)
                        .addGap(23, 23, 23))
                    .addGroup(paneCommonLayout.createSequentialGroup()
                        .addGroup(paneCommonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bAddPersons)
                            .addComponent(bAddItem))
                        .addContainerGap())))
        );

        paneStrands.getAccessibleContext().setAccessibleName(bundle.getString("msg.common.strand")); // NOI18N

        jTabbedPane1.addTab(bundle.getString("msg.common"), paneCommon); // NOI18N

        javax.swing.GroupLayout paneNotesLayout = new javax.swing.GroupLayout(paneNotes);
        paneNotes.setLayout(paneNotesLayout);
        paneNotesLayout.setHorizontalGroup(
            paneNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 438, Short.MAX_VALUE)
        );
        paneNotesLayout.setVerticalGroup(
            paneNotesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(bundle.getString("msg.common.notes"), paneNotes); // NOI18N

        javax.swing.GroupLayout paneTextLayout = new javax.swing.GroupLayout(paneText);
        paneText.setLayout(paneTextLayout);
        paneTextLayout.setHorizontalGroup(
            paneTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 438, Short.MAX_VALUE)
        );
        paneTextLayout.setVerticalGroup(
            paneTextLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 488, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(bundle.getString("msg.common.text"), paneText); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 522, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void rbNoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbNoneActionPerformed
		card.show(paneDate, "none");
    }//GEN-LAST:event_rbNoneActionPerformed

    private void rbFixedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbFixedActionPerformed
		card.show(paneDate, "dateFixed");
    }//GEN-LAST:event_rbFixedActionPerformed

    private void rbRelativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbRelativeActionPerformed
		card.show(paneDate, "dateRelative");
    }//GEN-LAST:event_rbRelativeActionPerformed

    private void bAddStrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddStrandActionPerformed
		// TODO bAddStrandActionPerformed
    }//GEN-LAST:event_bAddStrandActionPerformed

    private void bAddItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddItemActionPerformed
		// TODO bAddItemActionPerformed
    }//GEN-LAST:event_bAddItemActionPerformed

    private void bAddLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAddLocationActionPerformed
		// TODO bAddLocationActionPerformed
    }//GEN-LAST:event_bAddLocationActionPerformed

    private void btClearChapterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearChapterActionPerformed
        // TODO btClearChapterActionPerformed
    }//GEN-LAST:event_btClearChapterActionPerformed

    private void btClearLocationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btClearLocationActionPerformed
        // TODO btClearChapterActionPerformed
    }//GEN-LAST:event_btClearLocationActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAddItem;
    private javax.swing.JButton bAddLocation;
    private javax.swing.JButton bAddPersons;
    private javax.swing.JButton bAddStrand;
    private javax.swing.JButton btClearChapter;
    private javax.swing.JButton btClearLocation;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbChapters;
    private javax.swing.JComboBox cbLocations;
    private javax.swing.JComboBox cbStatus;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lbChapter;
    private javax.swing.JLabel lbDate;
    private javax.swing.JLabel lbId;
    private javax.swing.JList lbItems;
    private javax.swing.JLabel lbLocation;
    private javax.swing.JLabel lbNumber;
    private javax.swing.JList lbPersons;
    private javax.swing.JLabel lbStatus;
    private javax.swing.JList lbStrands;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JPanel paneCommon;
    private javax.swing.JPanel paneDate;
    private javax.swing.JPanel paneItems;
    private javax.swing.JPanel paneNotes;
    private javax.swing.JPanel panePersons;
    private javax.swing.JPanel paneStrands;
    private javax.swing.JPanel paneText;
    private javax.swing.JRadioButton rbFixed;
    private javax.swing.JRadioButton rbNone;
    private javax.swing.JRadioButton rbRelative;
    private javax.swing.JTextField txID;
    private javax.swing.JTextField txNumber;
    private javax.swing.JTextField txTitle;
    // End of variables declaration//GEN-END:variables

	void set(Scene scene) {
		this.scene = scene;
		initUI();
	}

	public Scene createNewScene() {
		Scene s = new Scene();
		s.setStatus(0);
		return s;
	}

	public Scene addNewScene(Scene orig) {
		Scene s = new Scene();
		if (orig.getStrand() != null)
			s.setStrand(orig.getStrand());
		if (orig.getSceneTs() != null)
			s.setSceneTs(orig.getSceneTs());
		return s;
	}

	boolean isModified() {
		if (!scene.getChapterSceneNo().equals(txNumber.getText()))
			return (true);
		if (!scene.getTitle().equals(txTitle.getText()))
			return (true);
		return (false);
	}

	public String saveData() {
		String rt = ctrlData();
		if ("".equals(rt)) {
			// TODO EditScene.saveData
		}
		return (rt);
	}

	private String ctrlData() {
		if ("".equals(txNumber.getText()))
			return (mandatoryField("msg.common.number"));
		if ("".equals(txTitle.getText()))
			return (mandatoryField("msg.common.title"));
		// TODO EditScene.ctrlData date
		return ("");
	}
}