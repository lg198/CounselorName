//Author: Layne Gustafson
//Date created: Jan 25, 2015
package com.github.lg198.cnotes.gui.util;

import java.awt.Image;

import javax.swing.ImageIcon;

public class IconLoader {
	
	public static ImageIcon getIcon(String n) {
		ImageIcon ii = new ImageIcon(IconLoader.class.getResource("/com/github/lg198/cnotes/res/icons/" + n + ".png"));
		return ii;
	}
	
	public static ImageIcon getIcon(String n, int width, int height) {
		ImageIcon ii = new ImageIcon(IconLoader.class.getResource("/com/github/lg198/cnotes/res/icons/" + n + ".png"));
		Image i = ii.getImage();
		return new ImageIcon(i.getScaledInstance(width, height, Image.SCALE_SMOOTH));
	}
	
	public static ImageIcon getIcon(String n, int size) {
		return getIcon(n, size, size);
	}

}
