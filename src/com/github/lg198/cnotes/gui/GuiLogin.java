//Author: Layne Gustafson
//Date created: Jan 25, 2015
package com.github.lg198.cnotes.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;

import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebPasswordField;
import com.alee.laf.text.WebTextField;
import com.github.lg198.cnotes.core.Givens;
import com.github.lg198.cnotes.gui.util.FieldVerifier;
import com.github.lg198.cnotes.gui.util.FieldVerifier.FieldVerificationListener;

public class GuiLogin {
	private WebLabel usernameLabel = new WebLabel("Username:");
	private WebTextField usernameField = new WebTextField();
	private WebLabel passwordLabel = new WebLabel("Password:");
	private WebPasswordField passwordField = new WebPasswordField();
	private WebButton submit = new WebButton("Submit");
	private FieldVerifier verifier;
	
	public GuiLogin() {
		WebLookAndFeel.setDecorateFrames(true);
		init();
		WebLookAndFeel.setDecorateFrames(false);
	}
	
	public void init() {
		submit.setRolloverShine(true);
		verifier = new FieldVerifier(new FieldVerificationListener() {

			@Override
			public boolean verify(JComponent source) {
				if (source.equals(passwordField)) {
					return !new String(passwordField.getPassword()).isEmpty();
				}else {
					return !usernameField.getText().isEmpty();
				}
			}

			@Override
			public String getMessage(JComponent source) {
				return "This field cannot be empty!";
			}
			 
		 }, passwordField, usernameField);
		
		TitledBorderPainter titledBorderPainter = new TitledBorderPainter("Login");
        titledBorderPainter.setTitleOffset(10);
        titledBorderPainter.setRound(4);
		final WebFrame wf = new WebFrame(Givens.fullName());
		wf.setResizable(false);
		wf.getContentPane().add(new GroupPanel(GroupingType.fillAll, new GroupPanel(GroupingType.fillLast, 5, 
				new GroupPanel(GroupingType.fillAll, 5, false, usernameLabel, passwordLabel, new WebLabel()),
				new GroupPanel(GroupingType.fillAll, 5, false, usernameField, passwordField, 
						new GroupPanel(GroupingType.fillFirst, new WebLabel(), submit.setPreferredWidth(100))))
		.setMargin(10).setPainter(titledBorderPainter)).setMargin(5));
		
		ActionListener al = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (verifier.verify()) {
					wf.dispose();
				}
			}
		};
		
		submit.addActionListener(al);
		passwordField.addActionListener(al);
		
		wf.setSize(new Dimension(350, 220));
		wf.setLocationRelativeTo(null);
		wf.setDefaultCloseOperation(WebFrame.EXIT_ON_CLOSE);
		wf.setVisible(true);
	}

}
