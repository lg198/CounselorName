//Author: Layne Gustafson
//Date created: Jan 25, 2015
package com.github.lg198.cnotes.gui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;

import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.tab.DocumentData;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextField;
import com.alee.utils.WindowUtils;
import com.alee.utils.swing.WebTimer;
import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.core.Givens;
import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.gui.util.IconLoader;

public class GuiMain {
	private WebTextField searchField = new WebTextField();
	private WebList searchResults = new WebList();
	private SearchListModel searchModel = new SearchListModel();
	private WebDocumentPane studentPane = new WebDocumentPane();
	private WebSplitPane split = new WebSplitPane();
	private WebScrollPane scroll = new WebScrollPane(searchResults);
	
	private WebButton addButton = new WebButton("Add", IconLoader.getIcon("add_green", 14, 14));
	private WebButton removeButton = new WebButton("Remove", IconLoader.getIcon("remove_red", 14, 14));
	
	private WebDialog loginDialog = new WebDialog();
	
	public GuiMain() {
		init();
	}
	
	public void init() {
		WebFrame wf = new WebFrame(Givens.fullName());
		
		initComponents();
		GroupPanel left = new GroupPanel(GroupingType.fillMiddle, 5, false, 
				searchField, scroll, 
				new GroupPanel(false,
						new GroupPanel(GroupingType.fillAll, 2, 
								addButton,
								removeButton)));
		GroupPanel right = new GroupPanel(GroupingType.fillFirst, 10, false, studentPane);
		split.setLeftComponent(left);
		split.setRightComponent(right);
		split.setMargin(10);
		split.setDividerLocation(200);
		
		
		wf.getContentPane().add(split);
		
		wf.setExtendedState(wf.getExtendedState() | WebFrame.MAXIMIZED_BOTH);
		wf.setDefaultCloseOperation(WebFrame.EXIT_ON_CLOSE);
		wf.setVisible(true);
		
		WebLookAndFeel.setDecorateDialogs(true);
		loginDialog = new WebDialog(wf, Givens.fullName(), Dialog.ModalityType.DOCUMENT_MODAL);
		loginDialog.add(new WebButton("hi"));
		WindowUtils.packAndCenter(loginDialog);
		loginDialog.setLocationRelativeTo(null);
		loginDialog.setVisible(true);
		WebLookAndFeel.setDecorateDialogs(false);
	}
	
	@SuppressWarnings("unchecked")
	public void initComponents() {
		searchResults.setModel(searchModel);
		searchField.setInputPrompt("Search Students...");
		searchField.setLeadingComponent(new WebLabel(IconLoader.getIcon("search_blue", 16, 16)).setMargin(0, 0, 0, 3));
		searchField.setRound(4);
		searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			
			private void search() {
				try {
					List<Student> l = DatabaseManager.searchStudents(searchField.getText());
					searchModel.setNames(l);
					removeButton.setEnabled(false);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				search();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				search();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				search();
			}
		});
		
		searchResults.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				if (searchResults.getSelectedIndex() > -1) {
					removeButton.setEnabled(true);
				}
			}

			@Override
			public void focusLost(FocusEvent e) {		
			}
			
		});
		searchResults.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		addButton.setRolloverDecoratedOnly(true);
		removeButton.setRolloverDecoratedOnly(true);
		removeButton.setEnabled(false);
		
		addButton.addActionListener(new ActionListener() {
			WebTimer wt;

			@Override
			public void actionPerformed(ActionEvent e) {
				new GuiAddStudent(GuiMain.this);
			}
			
		});
		
		searchResults.setDragEnabled(false);
		searchResults.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int idx = searchResults.locationToIndex(e.getPoint());
					if (idx >= 0) {
						openStudent(searchModel.getStudentAt(idx));
					}
				}
			}
		});
	}
	
	public void openStudent(Student st) {
		studentPane.openDocument(new DocumentData("student." + st.getId(), null, (String)st.getName(), null));
	}

}
