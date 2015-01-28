package com.github.lg198.cnotes.gui;

import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.rootpane.WebFrame;

public class GuiImport {

	public GuiImport(WebFrame wf) {
		WebLookAndFeel.setDecorateDialogs(true);
		init(wf);
		WebLookAndFeel.setDecorateDialogs(false);
	}
	
	public WebDialog wd;

	public void init(WebFrame wf) {
		wd = new WebDialog(wf);
		WebProgressBar progress = new WebProgressBar();
		progress.setIndeterminate(true);
		progress.setStringPainted(true);
		progress.setString("Importing names...");
		wd.setLayout(new VerticalFlowLayout());
		wd.add(progress);
		wd.setDefaultCloseOperation(WebDialog.DO_NOTHING_ON_CLOSE);
		wd.setVisible(true);
	}

}
