//Author: Layne Gustafson
//Date created: Jan 23, 2015
package com.github.lg198.cnotes.bean;

import java.sql.SQLException;

import com.github.lg198.cnotes.database.DatabaseManager;

public class Student {
	
	private String firstName;
	private String id;
	private String lastName;
	
	public Student(String i, String fn, String ln) {
		firstName = fn;
		lastName = ln;
		id = i;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return getFirstName() + " " + getLastName();
	}
	
	public void setFirstName(String fn) throws SQLException {
		String rollback = getFirstName();
		firstName = fn;
		try {
			DatabaseManager.updateStudent(this);
		} catch (SQLException e) {
			firstName = rollback;
			throw e;
		}
	}
	
	public void setLastName(String ln) throws SQLException {
		String rollback = getLastName();
		lastName = ln;
		try {
			DatabaseManager.updateStudent(this);
		} catch (SQLException e) {
			lastName = rollback;
			throw e;
		}
	}

}
