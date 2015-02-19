//Author: Layne Gustafson
//Date created: Jan 26, 2015
package com.github.lg198.cnotes.gui;

import java.awt.Component;
import java.sql.SQLException;
import java.util.List;

import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.separator.WebSeparator;
import com.esotericsoftware.tablelayout.swing.Table;
import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.database.DatabaseManager;

public class GuiStudent {
	
	public Student student;
	
	private Table table;
	
	public GuiStudent(Student s) {
		student = s;
		init();
	}
	
	public void init() {
		setUpGrid();
	}
	
	private void setUpGrid() {
		try {
			List<CustomField> fields = DatabaseManager.getCustomFields(student);
			table = new Table();
			table.left().top();
			
			table.addCell(new WebSeparator());
			table.row();
			for (CustomField cf : fields) {
				table.addCell(new WebLabel(cf.getName() + ":").setFontSize(16)).spaceRight(10);
				table.addCell(cf.getType().getPanel(cf, student));
				table.row();
				table.addCell(new WebSeparator());
			}
			table.row();
			SuperEditorPane sep = new SuperEditorPane();
			//wep.setOpaque(false);
			table.addCell(new GroupPanel(sep.getToolbar()));
			table.row();
			table.addCell(new WebScrollPane(sep.getPane())).fillX();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public Component getPanel() {
		return table;
	}

}
