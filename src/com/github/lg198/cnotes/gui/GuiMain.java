//Author: Layne Gustafson
//Date created: Jan 25, 2015
package com.github.lg198.cnotes.gui;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;
import java.util.List;

import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.tab.DocumentData;
import com.alee.extended.tab.DocumentListener;
import com.alee.extended.tab.PaneData;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.filechooser.WebFileChooser;
import com.alee.laf.label.WebLabel;
import com.alee.laf.list.WebList;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.splitpane.WebSplitPane;
import com.alee.laf.text.WebTextField;
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
	private WebMenuBar menuBar = new WebMenuBar();
	
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
		
		loadMenuBar(wf);
		wf.setJMenuBar(menuBar);
		
		wf.getContentPane().add(split);
		
		wf.setExtendedState(wf.getExtendedState() | WebFrame.MAXIMIZED_BOTH);
		wf.setDefaultCloseOperation(WebFrame.EXIT_ON_CLOSE);
		wf.setMinimumSize(new Dimension(640, 480));
		wf.setVisible(true);
		
		WebLookAndFeel.setDecorateDialogs(true);
		loginDialog = new WebDialog(wf, Givens.fullName(), Dialog.ModalityType.DOCUMENT_MODAL);
		new GuiLogin(loginDialog);
		loginDialog.setVisible(true);
		WebLookAndFeel.setDecorateDialogs(false);
	}
	
	@SuppressWarnings("unchecked")
	public void initComponents() {
		searchResults.setModel(searchModel);
		searchField.setInputPrompt("Search Students...");
		searchField.setLeadingComponent(new WebLabel(IconLoader.getIcon("search_blue", 16, 16)).setMargin(0, 2, 0, 3));
		searchField.setRound(4);
		searchField.setFontSize(14);
		searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			
			private void search() {
				updateSearch();
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
				if (searchResults.getSelectedIndex() > -1 && searchModel.getSize() > 0) {
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
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchModel.getSize() > 0 && searchResults.getSelectedIndex() > -1) {
					Student selected = searchModel.getStudentAt(searchResults.getSelectedIndex());
					try {
						DatabaseManager.deleteStudent(selected);
						List<Student> l = searchModel.getStudents();
						l.remove(selected);
						searchModel.setNames(l);
						if (studentPane.getDocument("student." + selected.getId()) != null) {
							studentPane.closeDocument("student." + selected.getId());
						}
					} catch (SQLException e1) {
						WebOptionPane.showMessageDialog(null, "A database error has occurred: student could not be deleted!", "Error", 
								WebOptionPane.ERROR_MESSAGE, null);
					}
				}
			}
		});
		
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
	
	public void loadMenuBar(final WebFrame wf) {
		WebMenu fileMenu = new WebMenu("File");
		WebMenuItem importItem = new WebMenuItem("Import student names...");
		importItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final WebFileChooser chooser = new WebFileChooser();
				chooser.setMultiSelectionEnabled(false);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.addChoosableFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
				if(chooser.showOpenDialog(wf) == WebFileChooser.APPROVE_OPTION) {
					final GuiImport gimp = new GuiImport(wf);
					new Thread() {
						@Override
						public void run() {
							try {
								DatabaseManager.loadNames(new FileReader(chooser.getSelectedFile()));
								gimp.wd.dispose();
							} catch (FileNotFoundException e) {
							}
						}
					}.start();
				}
			}
		});
		fileMenu.add(importItem);
		menuBar.add(fileMenu);
	}
	
	public void openStudent(Student st) {
		String id = "student." + st.getId();
		studentPane.openDocument(new DocumentData(id, IconLoader.getIcon("student_blue", 16), (String)st.getName(), null));
		studentPane.setSelected(id);
	}
	
	public void updateSearch() {
		try {
			List<Student> l = DatabaseManager.searchStudents(searchField.getText());
			searchModel.setNames(l);
			removeButton.setEnabled(false);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
