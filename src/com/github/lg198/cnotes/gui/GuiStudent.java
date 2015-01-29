//Author: Layne Gustafson
//Date created: Jan 26, 2015
package com.github.lg198.cnotes.gui;

import java.sql.SQLException;

import com.alee.extended.panel.GridPanel;
import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.database.DatabaseManager;

public class GuiStudent {
	
	public Student student;
	
	private GridPanel grid;
	
	public GuiStudent(Student s) {
		student = s;
		init();
	}
	
	public void init() {
		setUpGrid();
	}
	
	private void setUpGrid() {
		try {
			int numberOfFields = (DatabaseManager.countCustomFields() + 2) * 2 - 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
