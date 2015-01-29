package com.github.lg198.cnotes.bean.field;

import com.alee.laf.panel.WebPanel;

public enum CustomFieldType implements CustomField {
	
	TEXT {

		@Override
		public WebPanel getPanel(String fieldName, String fieldValue) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}, 
	
	NUMBER {

		@Override
		public WebPanel getPanel(String fieldName, String fieldValue) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}, 
	
	OPTIONS {

		@Override
		public WebPanel getPanel(String fieldName, String fieldValue) {
			// TODO Auto-generated method stub
			return null;
		}
		
	};



}
