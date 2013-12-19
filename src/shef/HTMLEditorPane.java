package shef;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.undo.UndoManager;

import shef.ui.DefaultAction;
import shef.ui.UIUtils;
import shef.ui.text.CompoundUndoManager;
import shef.ui.text.Entities;
import shef.ui.text.HTMLUtils;
import shef.ui.text.IndentationFilter;
import shef.ui.text.SourceCodeEditor;
import shef.ui.text.WysiwygHTMLEditorKit;
import shef.ui.text.actions.ClearStylesAction;
import shef.ui.text.actions.FindReplaceAction;
import shef.ui.text.actions.HTMLEditorActionFactory;
import shef.ui.text.actions.HTMLElementPropertiesAction;
import shef.ui.text.actions.HTMLFontAction;
import shef.ui.text.actions.HTMLFontColorAction;
import shef.ui.text.actions.HTMLHorizontalRuleAction;
import shef.ui.text.actions.HTMLImageAction;
import shef.ui.text.actions.HTMLInlineAction;
import shef.ui.text.actions.HTMLLineBreakAction;
import shef.ui.text.actions.HTMLLinkAction;
import shef.ui.text.actions.HTMLTableAction;
import shef.ui.text.actions.HTMLTextEditAction;
import shef.ui.text.actions.SpecialCharAction;


import novaworx.syntax.SyntaxFactory;
import novaworx.textpane.SyntaxDocument;
import novaworx.textpane.SyntaxGutter;
import novaworx.textpane.SyntaxGutterBase;

import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;
import shef.ui.DefaultAction;
import shef.ui.UIUtils;

/**
 *
 * @author Bob Tantlinger
 */
public class HTMLEditorPane extends JPanel
{
	 /**
     *
     */
    private static final long serialVersionUID = 1L;

    private static final HtmlEditorI18n i18n = HtmlEditorI18n.getInstance("shef");

    private static final String INVALID_TAGS[] = {"html", "head", "body", "title"};

    private JEditorPane wysEditor;
    private SourceCodeEditor srcEditor;
    private JEditorPane focusedEditor;
    private JComboBox fontFamilyCombo;
    private JComboBox paragraphCombo;
    private JTabbedPane tabs;
    //private JMenuBar menuBar;
    private JToolBar formatToolBar;

    private JMenu editMenu;
    private JMenu formatMenu;
    private JMenu insertMenu;

    private JPopupMenu wysPopupMenu, srcPopupMenu;

    private ActionList actionList;

    private FocusListener focusHandler = new FocusHandler();
    private DocumentListener textChangedHandler = new TextChangedHandler();
    private ActionListener fontChangeHandler = new FontChangeHandler();
    private ActionListener paragraphComboHandler = new ParagraphComboHandler();
    private CaretListener caretHandler = new CaretHandler();
    private MouseListener popupHandler = new PopupHandler();

    private boolean isWysTextChanged;


    public HTMLEditorPane()
    {
    	initUI();
    }

    public void setCaretPosition(int pos)
    {
    	if(tabs.getSelectedIndex() == 0)
    	{
    		wysEditor.setCaretPosition(pos);
    		wysEditor.requestFocusInWindow();
    	}
    	else if(tabs.getSelectedIndex() == 1)
    	{
    		srcEditor.setCaretPosition(pos);
    		srcEditor.requestFocusInWindow();
    	}
    }

    public void setSelectedTab(int i)
    {
    	tabs.setSelectedIndex(i);
    }

    private void initUI()
    {
        createEditorTabs();
        createEditorActions();
        setLayout(new BorderLayout());
        add(formatToolBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

    }


    public JMenu getEditMenu()
    {
        return editMenu;
    }

    public JMenu getFormatMenu()
    {
        return formatMenu;
    }

    public JMenu getInsertMenu()
    {
        return insertMenu;
    }


    private void createEditorActions()
    {
        actionList = new ActionList("editor-actions");

        ActionList paraActions = new ActionList("paraActions");
        ActionList fontSizeActions = new ActionList("fontSizeActions");
        ActionList editActions = HTMLEditorActionFactory.createEditActionList();
        Action objectPropertiesAction = new HTMLElementPropertiesAction();

        //create editor popupmenus
        wysPopupMenu = ActionUIFactory.getInstance().createPopupMenu(editActions);
        wysPopupMenu.addSeparator();
        wysPopupMenu.add(objectPropertiesAction);
        srcPopupMenu = ActionUIFactory.getInstance().createPopupMenu(editActions);

        // create file menu
        JMenu fileMenu = new JMenu(i18n.str("file"));

        // create edit menu
        ActionList lst = new ActionList("edits");
        Action act = new ChangeTabAction(0);
        lst.add(act);
        act = new ChangeTabAction(1);
        lst.add(act);
        lst.add(null);//separator
        lst.addAll(editActions);
        lst.add(null);
        lst.add(new FindReplaceAction(false));
        actionList.addAll(lst);
        editMenu = ActionUIFactory.getInstance().createMenu(lst);
        editMenu.setText(i18n.str("edit"));


        //create format menu
        formatMenu = new JMenu(i18n.str("format"));
        lst = HTMLEditorActionFactory.createFontSizeActionList();//HTMLEditorActionFactory.createInlineActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, i18n.str("size")));
        fontSizeActions.addAll(lst);

        lst = HTMLEditorActionFactory.createInlineActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, i18n.str("style")));

        act = new HTMLFontColorAction();
        actionList.add(act);
        formatMenu.add(act);

        act = new HTMLFontAction();
        actionList.add(act);
        formatMenu.add(act);

        act = new ClearStylesAction();
        actionList.add(act);
        formatMenu.add(act);
        formatMenu.addSeparator();

        lst = HTMLEditorActionFactory.createBlockElementActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, i18n.str("paragraph")));
        paraActions.addAll(lst);

        lst = HTMLEditorActionFactory.createListElementActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, i18n.str("list")));
        formatMenu.addSeparator();
        paraActions.addAll(lst);

        lst = HTMLEditorActionFactory.createAlignActionList();
        actionList.addAll(lst);
        formatMenu.add(createMenu(lst, i18n.str("align")));

        JMenu tableMenu = new JMenu(i18n.str("table"));
        lst = HTMLEditorActionFactory.createInsertTableElementActionList();
        actionList.addAll(lst);
        tableMenu.add(createMenu(lst, i18n.str("insert")));

        lst = HTMLEditorActionFactory.createDeleteTableElementActionList();
        actionList.addAll(lst);
        tableMenu.add(createMenu(lst, i18n.str("delete")));
        formatMenu.add(tableMenu);
        formatMenu.addSeparator();

        actionList.add(objectPropertiesAction);
        formatMenu.add(objectPropertiesAction);



        //create insert menu
        insertMenu = new JMenu(i18n.str("insert"));
        act = new HTMLLinkAction();
        actionList.add(act);
        insertMenu.add(act);

        act = new HTMLImageAction();
        actionList.add(act);
        insertMenu.add(act);

        act = new HTMLTableAction();
        actionList.add(act);
        insertMenu.add(act);
        insertMenu.addSeparator();

        act = new HTMLLineBreakAction();
        actionList.add(act);
        insertMenu.add(act);

        act = new HTMLHorizontalRuleAction();
        actionList.add(act);
        insertMenu.add(act);

        act = new SpecialCharAction();
        actionList.add(act);
        insertMenu.add(act);


        createFormatToolBar(paraActions, fontSizeActions);
    }

    private void createFormatToolBar(ActionList blockActs, ActionList fontSizeActs)
    {
        formatToolBar = new JToolBar();
        formatToolBar.setFloatable(false);
        formatToolBar.setFocusable(false);

        Font comboFont = new Font("Dialog", Font.PLAIN, 12);
        PropertyChangeListener propLst = new PropertyChangeListener()
        {
            public void propertyChange(PropertyChangeEvent evt)
            {
                if(evt.getPropertyName().equals("selected"))
                {
                    if(evt.getNewValue().equals(Boolean.TRUE))
                    {
                        paragraphCombo.removeActionListener(paragraphComboHandler);
                        paragraphCombo.setSelectedItem(evt.getSource());
                        paragraphCombo.addActionListener(paragraphComboHandler);
                    }
                }
            }
        };
        for(Iterator it = blockActs.iterator(); it.hasNext();)
        {
            Object o = it.next();
            if(o instanceof DefaultAction) {
				((DefaultAction)o).addPropertyChangeListener(propLst);
			}
        }
        paragraphCombo = new JComboBox(toArray(blockActs));
        paragraphCombo.setPreferredSize(new Dimension(120, 22));
        paragraphCombo.setMinimumSize(new Dimension(120, 22));
        paragraphCombo.setMaximumSize(new Dimension(120, 22));
        paragraphCombo.setFont(comboFont);
        paragraphCombo.addActionListener(paragraphComboHandler);
        paragraphCombo.setRenderer(new ParagraphComboRenderer());
        formatToolBar.add(paragraphCombo);
        formatToolBar.addSeparator();

        Vector fonts = new Vector();
        fonts.add("Default");
        fonts.add("serif");
        fonts.add("sans-serif");
        fonts.add("monospaced");
        GraphicsEnvironment gEnv =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        fonts.addAll(Arrays.asList(gEnv.getAvailableFontFamilyNames()));

        fontFamilyCombo = new JComboBox(fonts);
        fontFamilyCombo.setPreferredSize(new Dimension(150, 22));
        fontFamilyCombo.setMinimumSize(new Dimension(150, 22));
        fontFamilyCombo.setMaximumSize(new Dimension(150, 22));
        fontFamilyCombo.setFont(comboFont);
        fontFamilyCombo.addActionListener(fontChangeHandler);
        formatToolBar.add(fontFamilyCombo);

        final JButton fontSizeButton = new JButton(UIUtils.getIcon(UIUtils.X16, "fontsize.png"));
        final JPopupMenu sizePopup = ActionUIFactory.getInstance().createPopupMenu(fontSizeActs);
        ActionListener al = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                sizePopup.show(fontSizeButton, 0, fontSizeButton.getHeight());
            }
        };
        fontSizeButton.addActionListener(al);
        configToolbarButton(fontSizeButton);
        formatToolBar.add(fontSizeButton);

        Action act = new HTMLFontColorAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);
        formatToolBar.addSeparator();

        act = new HTMLInlineAction(HTMLInlineAction.BOLD);
        act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLInlineAction(HTMLInlineAction.ITALIC);
        act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLInlineAction(HTMLInlineAction.UNDERLINE);
        act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
        actionList.add(act);
        addToToolBar(formatToolBar, act);
        formatToolBar.addSeparator();

        List alst = HTMLEditorActionFactory.createListElementActionList();
        for(Iterator it = alst.iterator(); it.hasNext();)
        {
            act = (Action)it.next();
            act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
            actionList.add(act);
            addToToolBar(formatToolBar, act);
        }
        formatToolBar.addSeparator();

        alst = HTMLEditorActionFactory.createAlignActionList();
        for(Iterator it = alst.iterator(); it.hasNext();)
        {
            act = (Action)it.next();
            act.putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_TOGGLE);
            actionList.add(act);
            addToToolBar(formatToolBar, act);
        }
        formatToolBar.addSeparator();

        act = new HTMLLinkAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLImageAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);

        act = new HTMLTableAction();
        actionList.add(act);
        addToToolBar(formatToolBar, act);

    }

    private void addToToolBar(JToolBar toolbar, Action act)
    {
        AbstractButton button = ActionUIFactory.getInstance().createButton(act);
        configToolbarButton(button);
        toolbar.add(button);
    }

    /**
     * Converts an action list to an array.
     * Any of the null "separators" or sub ActionLists are ommited from the array.
     * @param lst
     * @return
     */
    private Action[] toArray(ActionList lst)
    {
        List acts = new ArrayList();
        for(Iterator it = lst.iterator(); it.hasNext();)
        {
            Object v = it.next();
            if(v != null && v instanceof Action) {
				acts.add(v);
			}
        }

        return (Action[])acts.toArray(new Action[acts.size()]);
    }

    private void configToolbarButton(AbstractButton button)
    {
        button.setText(null);
        button.setMnemonic(0);
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setMaximumSize(new Dimension(22, 22));
        button.setMinimumSize(new Dimension(22, 22));
        button.setPreferredSize(new Dimension(22, 22));
        button.setFocusable(false);
        button.setFocusPainted(false);
        //button.setBorder(plainBorder);
        Action a = button.getAction();
        if(a != null) {
			button.setToolTipText(a.getValue(Action.NAME).toString());
		}
    }

    private JMenu createMenu(ActionList lst, String menuName)
    {
        JMenu m = ActionUIFactory.getInstance().createMenu(lst);
        m.setText(menuName);
        return m;
    }

    private void createEditorTabs()
    {
        tabs = new JTabbedPane(SwingConstants.BOTTOM);
        wysEditor = createWysiwygEditor();
        srcEditor = createSourceEditor();

        tabs.addTab("Edit", new JScrollPane(wysEditor));

        JScrollPane scrollPane = new JScrollPane(srcEditor);
        SyntaxGutter gutter = new SyntaxGutter(srcEditor);
        SyntaxGutterBase gutterBase = new SyntaxGutterBase(gutter);
        scrollPane.setRowHeaderView(gutter);
        scrollPane.setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, gutterBase);

        tabs.addTab("HTML", scrollPane);
        tabs.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                updateEditView();
            }
        });
    }

    private SourceCodeEditor createSourceEditor()
    {
        SourceCodeEditor ed = new SourceCodeEditor();
        SyntaxDocument doc = new SyntaxDocument();
        doc.setSyntax(SyntaxFactory.getSyntax("html"));
        CompoundUndoManager cuh = new CompoundUndoManager(doc, new UndoManager());

        doc.addUndoableEditListener(cuh);
        doc.setDocumentFilter(new IndentationFilter());
        doc.addDocumentListener(textChangedHandler);
        ed.setDocument(doc);
        ed.addFocusListener(focusHandler);
        ed.addCaretListener(caretHandler);
        ed.addMouseListener(popupHandler);

        return ed;
    }

    private JEditorPane createWysiwygEditor()
    {
        JEditorPane ed = new JEditorPane();
        ed.setEditorKitForContentType("text/html", new WysiwygHTMLEditorKit());

        ed.setContentType("text/html");

        insertHTML(ed, "<p></p>", 0);

        ed.addCaretListener(caretHandler);
        ed.addFocusListener(focusHandler);
        ed.addMouseListener(popupHandler);


        HTMLDocument document = (HTMLDocument)ed.getDocument();
        CompoundUndoManager cuh = new CompoundUndoManager(document, new UndoManager());
        document.addUndoableEditListener(cuh);
        document.addDocumentListener(textChangedHandler);

        return ed;
    }

    //  inserts html into the wysiwyg editor TODO remove JEditorPane parameter
    private void insertHTML(JEditorPane editor, String html, int location)
    {
        try
        {
            HTMLEditorKit kit = (HTMLEditorKit) editor.getEditorKit();
            Document doc = editor.getDocument();
            StringReader reader = new StringReader(HTMLUtils.jEditorPaneizeHTML(html));
            kit.read(reader, doc, location);
        }
        catch (IOException | BadLocationException ex)
        {
        }
    }

    // called when changing tabs
    private void updateEditView()
    {
        if(tabs.getSelectedIndex() == 0)
        {
            String topText = removeInvalidTags(srcEditor.getText());
            wysEditor.setText("");
            insertHTML(wysEditor, topText, 0);
            CompoundUndoManager.discardAllEdits(wysEditor.getDocument());

        }
        else
        {
            String topText = removeInvalidTags(wysEditor.getText());
            if(isWysTextChanged || srcEditor.getText().equals(""))
            {
                String t = deIndent(removeInvalidTags(topText));
                t = Entities.HTML40.unescapeUnknownEntities(t);
                srcEditor.setText(t);
            }
            CompoundUndoManager.discardAllEdits(srcEditor.getDocument());
        }

        isWysTextChanged = false;
        paragraphCombo.setEnabled(tabs.getSelectedIndex() == 0);
        fontFamilyCombo.setEnabled(tabs.getSelectedIndex() == 0);
        updateState();
    }

    public void setText(String text)
    {
    	String topText = removeInvalidTags(text);

        if(tabs.getSelectedIndex() == 0)
        {

            wysEditor.setText("");
            insertHTML(wysEditor, topText, 0);
            CompoundUndoManager.discardAllEdits(wysEditor.getDocument());

        }
        else
        {
            {
                String t = deIndent(removeInvalidTags(topText));
                t = Entities.HTML40.unescapeUnknownEntities(t);
                srcEditor.setText(t);
            }
            CompoundUndoManager.discardAllEdits(srcEditor.getDocument());
        }
    }

    public String getText()
    {
    	String topText;
    	if(tabs.getSelectedIndex() == 0)
        {
           topText = removeInvalidTags(wysEditor.getText());

        }
        else
        {
            topText = removeInvalidTags(srcEditor.getText());
            topText = deIndent(removeInvalidTags(topText));
            topText = Entities.HTML40.unescapeUnknownEntities(topText);
        }

    	return topText;
    }


    /* *******************************************************************
     *  Methods for dealing with HTML between wysiwyg and source editors
     * ******************************************************************/
    private String deIndent(String html)
    {
        String ws = "\n    ";
        StringBuilder sb = new StringBuilder(html);

        while(sb.indexOf(ws) != -1)
        {
            int s = sb.indexOf(ws);
            int e = s + ws.length();
            sb.delete(s, e);
            sb.insert(s, "\n");
        }

        return sb.toString();
    }

    private String removeInvalidTags(String html)
    {
        for(int i = 0; i < INVALID_TAGS.length; i++)
        {
            html = deleteOccurance(html, '<' + INVALID_TAGS[i] + '>');
            html = deleteOccurance(html, "</" + INVALID_TAGS[i] + '>');
        }

        return html.trim();
    }

    private String deleteOccurance(String text, String word)
    {
        //if(text == null)return "";
        StringBuilder sb = new StringBuilder(text);
        int p;
        while((p = sb.toString().toLowerCase().indexOf(word.toLowerCase())) != -1)
        {
            sb.delete(p, p + word.length());
        }
        return sb.toString();
    }
    /* ************************************* */

    private void updateState()
    {
        if(focusedEditor == wysEditor)
        {
            fontFamilyCombo.removeActionListener(fontChangeHandler);
            String fontName = HTMLUtils.getFontFamily(wysEditor);
            if(fontName == null) {
				fontFamilyCombo.setSelectedIndex(0);
			}
            else {
				fontFamilyCombo.setSelectedItem(fontName);
			}
            fontFamilyCombo.addActionListener(fontChangeHandler);
        }

        actionList.putContextValueForAll(HTMLTextEditAction.EDITOR, focusedEditor);
        actionList.updateEnabledForAll();
    }






    private class CaretHandler implements CaretListener
    {
        /* (non-Javadoc)
         * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
         */
        public void caretUpdate(CaretEvent e)
        {
            updateState();
        }
    }

    private class PopupHandler extends MouseAdapter
    {
		@Override
        public void mousePressed(MouseEvent e)
        { checkForPopupTrigger(e); }

		@Override
        public void mouseReleased(MouseEvent e)
        { checkForPopupTrigger(e); }

        private void checkForPopupTrigger(MouseEvent e)
        {
            if(e.isPopupTrigger())
            {
                JPopupMenu p = null;
                if(e.getSource() == wysEditor) {
					p =  wysPopupMenu;
				}
                else if(e.getSource() == srcEditor) {
					p = srcPopupMenu;
				}
                else {
					return;
				}
                p.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private class FocusHandler implements FocusListener
    {
        public void focusGained(FocusEvent e)
        {
            if(e.getComponent() instanceof JEditorPane)
            {
                JEditorPane ed = (JEditorPane)e.getComponent();
                CompoundUndoManager.updateUndo(ed.getDocument());
                focusedEditor = ed;

                updateState();
               // updateEnabledStates();
            }
        }

        public void focusLost(FocusEvent e)
        {

            if(e.getComponent() instanceof JEditorPane)
            {
                //focusedEditor = null;
                //wysiwygUpdated();
            }
        }
    }

    private class TextChangedHandler implements DocumentListener
    {
        public void insertUpdate(DocumentEvent e)
        {
            textChanged();
        }

        public void removeUpdate(DocumentEvent e)
        {
            textChanged();
        }

        public void changedUpdate(DocumentEvent e)
        {
            textChanged();
        }

        private void textChanged()
        {
            if(tabs.getSelectedIndex() == 0) {
				isWysTextChanged = true;
			}
        }
    }

    private class ChangeTabAction extends DefaultAction
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;
        int tab;
        public ChangeTabAction(int tab)
        {
            super((tab == 0) ? i18n.str("rich_text") :
                i18n.str("source"));
            this.tab = tab;
            putValue(ActionManager.BUTTON_TYPE, ActionManager.BUTTON_TYPE_VALUE_RADIO);
        }

		@Override
        protected void execute(ActionEvent e)
        {
            tabs.setSelectedIndex(tab);
            setSelected(true);
        }

		@Override
        protected void contextChanged()
        {
            setSelected(tabs.getSelectedIndex() == tab);
        }
    }

    private class ParagraphComboHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == paragraphCombo)
            {
                Action a = (Action)(paragraphCombo.getSelectedItem());
                a.actionPerformed(e);
            }
        }
    }

    private class ParagraphComboRenderer extends DefaultListCellRenderer
    {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

		@Override
        public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus)
        {
            if(value instanceof Action)
            {
                value = ((Action)value).getValue(Action.NAME);
            }

            return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        }
    }

    private class FontChangeHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource() == fontFamilyCombo && focusedEditor == wysEditor )
            {
                //MutableAttributeSet tagAttrs = new SimpleAttributeSet();
                HTMLDocument document = (HTMLDocument)focusedEditor.getDocument();
                CompoundUndoManager.beginCompoundEdit(document);

                if(fontFamilyCombo.getSelectedIndex() != 0)
                {
                    HTMLUtils.setFontFamily(wysEditor, fontFamilyCombo.getSelectedItem().toString());

                }
                else
                {
                    HTMLUtils.setFontFamily(wysEditor, null);
                }
                CompoundUndoManager.endCompoundEdit(document);
            }
        }

        /* (non-Javadoc)
         * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
         */
        public void itemStateChanged(ItemEvent e)
        {


        }
    }
}