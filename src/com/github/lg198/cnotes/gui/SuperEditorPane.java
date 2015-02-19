package com.github.lg198.cnotes.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.html.HTMLDocument;

import com.alee.laf.button.WebButton;
import com.alee.laf.text.WebEditorPane;
import com.alee.laf.toolbar.WebToolBar;
import com.github.lg198.cnotes.gui.util.IconLoader;

public class SuperEditorPane {
	
	private WebEditorPane pane;
	private WebToolBar toolbar;
	
	private WebButton bold, italic, underlined;
	
	public SuperEditorPane() {
		init();
	}
	
	private void init() {
		toolbar = new WebToolBar(WebToolBar.HORIZONTAL);
		toolbar.setFloatable(false);
		pane = new WebEditorPane();
		pane.setContentType("text/html");
		initFontComponents();
		pane.setSize(300, 300);
	}
	
	private void initFontComponents() {
		StyleContext sc = new StyleContext();
		Style boldStyle = sc.addStyle("Bold", sc.getStyle(StyleContext.DEFAULT_STYLE));
		bold = createButton("bold", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				int start = pane.getSelectionStart(), end = pane.getSelectionEnd();
				try {
					HTMLDocument hd = (HTMLDocument) pane.getDocument();
					hd.setCharacterAttributes(start, end-start, StyleConstants., replace);
					pane.getDocument().insertString(end, "</b>", null);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			
		});
		toolbar.add(bold);
		italic = createButton("italic", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		underlined = createButton("underlined", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	private WebButton createButton(String icon, ActionListener al) {
		WebButton wb = WebButton.createIconWebButton(IconLoader.getIcon(icon, 16), com.alee.global.StyleConstants.smallRound, true);
		wb.addActionListener(al);
		return wb;
	}
	
	public WebEditorPane getPane() {
		return pane;
	}
	
	public WebToolBar getToolbar() {
		return toolbar;
	}

}
