package com.github.lg198.cnotes.gui;

import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.alee.extended.breadcrumb.WebBreadcrumbLabel;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebPasswordField;
import com.esotericsoftware.tablelayout.swing.Table;

public class GuiSetup {
	
	WebBreadcrumb breadcrumb = new WebBreadcrumb();
	WebBreadcrumbLabel plabel = new WebBreadcrumbLabel("Set Password");
	
	public GuiSetup(WebDialog wd) {
		init(wd);
	}
	
	private void init(WebDialog wd)  {
		wd.setLayout(new VerticalFlowLayout());
		
		breadcrumb.add(plabel);
		plabel.setShowProgress(true);
		plabel.setProgress(1f);
		
		ptable.addCell(new WebLabel("Set your password:").setFontSize(16)).center().left();
		ptable.row();
		
		WebPasswordField p1 = new WebPasswordField();
		p1.setInputPrompt("Password");
		ptable.addCell(p1).left().fillX();
		ptable.row();
		
		WebPasswordField p2 = new WebPasswordField();
		p2.setInputPrompt("Confirm Password");
		ptable.addCell(p2).left().fillX();		
		ptable.left();
		ptable.top();
		
		wd.getContentPane().add(breadcrumb);
		wd.getContentPane().add(new GroupPanel(ptable).setMargin(10));
		wd.setSize(300, 300);
		wd.setLocationRelativeTo(null);
		wd.setDefaultCloseOperation(WebDialog.DO_NOTHING_ON_CLOSE);
		wd.setVisible(true);
	}

}
