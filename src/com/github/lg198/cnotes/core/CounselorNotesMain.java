//Author: Layne Gustafson
//Date created: Jan 23, 2015
package com.github.lg198.cnotes.core;

import java.io.File;
import java.sql.SQLException;

import com.alee.laf.WebLookAndFeel;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.utils.SwingUtils;
import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.gui.GuiMain;

public class CounselorNotesMain {
	
	public static GuiMain gui;
	public static File folder;

	public static void main(String[] args) throws Exception {
		folder = new File("/Users/lg198/Documents/");
		DatabaseManager.init("org.sqlite.JDBC", "jdbc:sqlite:" + folder.getPath() + "cndb.db");
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				try {
					DatabaseManager.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		try {
			SwingUtils.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					WebLookAndFeel.install();
					TooltipManager.initialize();
					gui = new GuiMain();
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
