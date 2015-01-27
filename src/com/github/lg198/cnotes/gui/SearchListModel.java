//Author: Layne Gustafson
//Date created: Jan 25, 2015
package com.github.lg198.cnotes.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import com.github.lg198.cnotes.bean.Student;

public class SearchListModel implements ListModel {
	
	private List<ListDataListener> listeners = new ArrayList<ListDataListener>();
	private List<Student> names = new ArrayList<Student>();
	
	public void setNames(List<Student> l) {
		names = l;
		for (ListDataListener ldl : listeners) {
			ldl.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, names.size()));
		}
	}

	@Override
	public void addListDataListener(ListDataListener ldl) {
		listeners.add(ldl);
	}

	@Override
	public Object getElementAt(int i) {
		return names.get(i).getName();
	}
	
	public String getIdAt(int i) {
		return names.get(i).getId();
	}
	
	public Student getStudentAt(int i) {
		return names.get(i);
	}

	@Override
	public int getSize() {
		return names.size();
	}

	@Override
	public void removeListDataListener(ListDataListener ldl) {
		listeners.remove(ldl);
		
	}

}
