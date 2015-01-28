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
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.rootpane.WebDialog;
import com.alee.laf.text.WebPasswordField;
import com.github.lg198.cnotes.gui.util.FieldVerifier;
import com.github.lg198.cnotes.gui.util.FieldVerifier.FieldVerificationListener;
import com.github.lg198.cnotes.gui.util.VerificationResult;

public class GuiLogin {
	private WebLabel passwordLabel = new WebLabel("Password:");
	private WebPasswordField passwordField = new WebPasswordField();
	private WebButton submit = new WebButton("Submit");
	private FieldVerifier verifier;

	public GuiLogin(WebDialog wd) {
		init(wd);
	}

	public void init(final WebDialog wd) {
		submit.setRolloverShine(true);
		verifier = new FieldVerifier(new FieldVerificationListener() {

			@Override
			public VerificationResult verify(JComponent source) {
				if (new String(passwordField.getPassword()).isEmpty()) {
					return new VerificationResult(false, "The password cannot be empty!");
				} //check for password!
				return new VerificationResult(true);
			}


		}, passwordField);

		TitledBorderPainter titledBorderPainter = new TitledBorderPainter("Login");
		titledBorderPainter.setTitleOffset(10);
		titledBorderPainter.setRound(4);
		wd.setResizable(false);
		wd.getContentPane().add(
				new GroupPanel(GroupingType.fillAll, new GroupPanel(GroupingType.fillLast, 5, new GroupPanel(
						GroupingType.fillAll, 5, false, passwordLabel, new WebLabel()), new GroupPanel(GroupingType.fillAll, 5,
						false, passwordField, new GroupPanel(GroupingType.fillFirst, new WebLabel(), submit
								.setPreferredWidth(100)))).setMargin(10).setPainter(titledBorderPainter)).setMargin(5));

		ActionListener al = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (verifier.verify()) {
					wd.dispose();
				}
			}
		};

		submit.addActionListener(al);
		passwordField.addActionListener(al);

		wd.setSize(new Dimension(350, 180));
		wd.setLocationRelativeTo(null);
		wd.setDefaultCloseOperation(WebDialog.DO_NOTHING_ON_CLOSE);
	}

}
