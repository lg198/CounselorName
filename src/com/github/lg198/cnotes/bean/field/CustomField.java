package com.github.lg198.cnotes.bean.field;

import java.sql.SQLException;

import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.database.DatabaseManager;

public class CustomField {
	
	private String value;
	private String name;
	private String id;
	private CustomFieldType type;
	
	public CustomField(String i, String n, String val, CustomFieldType t) {
		id = i;
		name = n;
		value = val;
		type = t;
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	
	public CustomFieldType getType() {
		return type;
	}
	
	public void setValue(Student s, String v) throws SQLException {
		value = v;
		DatabaseManager.updateCustomField(s, this);
	}

}
