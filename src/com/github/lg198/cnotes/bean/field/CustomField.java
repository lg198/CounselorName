package com.github.lg198.cnotes.bean.field;

import com.alee.laf.panel.WebPanel;

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

}
