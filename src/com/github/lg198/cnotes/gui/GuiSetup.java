package com.github.lg198.cnotes.gui;

import java.awt.Color;
import java.awt.Dimension;

import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.separator.WebSeparator;
import com.alee.laf.text.WebPasswordField;
import com.esotericsoftware.tablelayout.swing.Table;

public class GuiSetup {
	
	private WebButton submit = new WebButton("Get Started");
	
	public GuiSetup(WebDialog wd) {
		init(wd);
	}

	private void init(WebDialog wd) {
		Table t = new Table();
		t.defaults().left();
		
		initPasswordSection(t);
		initBackupSection(t);
		
		submit.setFontSize(14);
		submit.setForeground(Color.blue);
		t.row().padTop(15);
		WebSeparator ws = WebSeparator.createHorizontal();
		ws.setPreferredSize(new Dimension(400, 1));
		t.addCell(ws).expandX();
		t.addCell(submit).right().bottom();
		
		wd.setContentPane(new GroupPanel(t).setMargin(15));
		t.top().left();
		
		wd.setResizable(false);
		wd.pack();
		wd.setLocationRelativeTo(null);
		wd.setDefaultCloseOperation(WebDialog.DO_NOTHING_ON_CLOSE);
		wd.setVisible(true);
	}
	
	private WebLabel heading(String s) {
		WebLabel wl = new WebLabel(s);
		wl.setForeground(Color.blue);
		wl.setFontSize(16);
		wl.setBoldFont();
		return wl;
	}
	
	private WebPasswordField p1 = new WebPasswordField(40), p2 = new WebPasswordField(40);
	
	private void initPasswordSection(Table t) {
		p1.setFontSize(14);
		p1.setInputPrompt("Create Password");
		p2.setFontSize(14);
		p2.setInputPrompt("Confirm Password");
		
		t.row();
		t.addCell(heading("Password"));
		t.addCell(WebSeparator.createHorizontal()).width(200);
		t.row().padLeft(20).padTop(5);
		t.addCell(p1).colspan(2);
		t.row().padLeft(20);
		t.addCell(p2).colspan(2);
	}
	
	private WebComboBox comboBox;
	
	private void initBackupSection(Table t) {
		comboBox = new WebComboBox(new String[]{"Every hour", "Every 12 hours", "Every day", "Every other day", "Every 5 days", "Every week", "Every month"});
		comboBox.setFontSize(13);
		
		t.row().padTop(5);
		t.addCell(heading("Backup Settings"));
		t.addCell(WebSeparator.createHorizontal()).width(200);
		t.row().padLeft(20).padTop(5);
		t.addCell(new WebLabel("Backup Schedule:").setFontSize(14));
		t.addCell(comboBox);
	}

}
