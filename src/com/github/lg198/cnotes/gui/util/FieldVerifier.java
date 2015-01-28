//Author: Layne Gustafson
//Date created: Jan 26, 2015
package com.github.lg198.cnotes.gui.util;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.swing.JComponent;

import com.alee.extended.window.PopOverDirection;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.label.WebLabel;

public class FieldVerifier {
	
	private FieldVerificationListener fvl;
	private HashMap<JComponent, WebPopOver> popovers = new HashMap<JComponent, WebPopOver>();
	
	public FieldVerifier(FieldVerificationListener fvl, JComponent... c) {
		this.fvl = fvl;
		for (final JComponent cc : c) {
			if (!hasLC(cc)) {
				continue;
			}
			popovers.put(cc, new WebPopOver());
			cc.addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(FocusEvent e) {
					callLC(cc, null);
					popovers.get(cc).dispose();
				}	
			});
		}
	}
	
	public boolean verify() {
		for (JComponent c : popovers.keySet()) {
			VerificationResult result = fvl.verify(c);
			if (!result.verify()) {
				WebLabel wl = new WebLabel(IconLoader.getIcon("warning_orange", 18, 18)).setMargin(0, 2, 0, 0);
				popovers.get(c).dispose();
				popovers.put(c, new WebPopOver());
				popovers.get(c).add(new WebLabel(result.getMessage()));
				callLC(c, wl);
				popovers.get(c).show(wl, PopOverDirection.up);
				return false;
			}
		}
		return true;
	}
	
	private boolean hasLC(JComponent c) {
		try {
			c.getClass().getMethod("setLeadingComponent", JComponent.class);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	private void callLC(JComponent c, JComponent lc) {
		try {
			Method m = c.getClass().getMethod("setLeadingComponent", JComponent.class);
			m.setAccessible(true);
			m.invoke(c, lc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static interface FieldVerificationListener {
		
		public VerificationResult verify(JComponent source);
	}

}
