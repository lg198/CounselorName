//Author: Layne Gustafson
//Date created: Jan 26, 2015
package com.github.lg198.cnotes.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JComponent;

import com.alee.extended.painter.TitledBorderPainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.laf.rootpane.WebFrame;
import com.alee.laf.text.WebTextField;
import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.core.Givens;
import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.gui.util.FieldVerifier;
import com.github.lg198.cnotes.gui.util.FieldVerifier.FieldVerificationListener;
import com.github.lg198.cnotes.gui.util.IconLoader;

public class GuiAddStudent {
	
	private WebLabel fnLabel = new WebLabel("First Name:");
	private WebTextField fnField = new WebTextField();
	private WebLabel lnLabel = new WebLabel("Last Name:");
	private WebTextField lnField = new WebTextField();
	private WebButton submit = new WebButton("Add", IconLoader.getIcon("add_green", 16));
	private FieldVerifier verifier;
	
	private GuiMain mainGui;
	
	public GuiAddStudent(GuiMain mg) {
		mainGui = mg;
		WebLookAndFeel.setDecorateFrames(true);
		init();
		WebLookAndFeel.setDecorateFrames(false);
	}
	
	public void init() {
		submit.setRolloverShine(true);
		verifier = new FieldVerifier(new FieldVerificationListener() {

			@Override
			public boolean verify(JComponent source) {
				if (source.equals(fnField)) {
					return !fnField.getText().isEmpty();
				} else {
					return !lnField.getText().isEmpty();
				}
			}

			@Override
			public String getMessage(JComponent source) {
				return "This field cannot be empty!";
			}
			 
		 }, lnField, fnField);
		
		TitledBorderPainter titledBorderPainter = new TitledBorderPainter("Add Student");
        titledBorderPainter.setTitleOffset(10);
        titledBorderPainter.setRound(4);
        
		final WebFrame wf = new WebFrame(Givens.fullName());
		wf.setResizable(false);
		wf.getContentPane().add(new GroupPanel(GroupingType.fillAll, new GroupPanel(GroupingType.fillLast, 5, 
				new GroupPanel(GroupingType.fillAll, 5, false, fnLabel, lnLabel, new WebLabel()),
				new GroupPanel(GroupingType.fillAll, 5, false, fnField, lnField, 
						new GroupPanel(GroupingType.fillFirst, new WebLabel(), submit.setPreferredWidth(100))))
		.setMargin(10).setPainter(titledBorderPainter)).setMargin(5));
		
		ActionListener al = new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				if (verifier.verify()) {
					String firstName = fnField.getText().trim();
					String lastName = lnField.getText().trim();
					try {
						Student s = DatabaseManager.addStudent(firstName, lastName);
						wf.dispose();
						mainGui.openStudent(s);
					} catch (SQLException e1) {
						WebOptionPane.showMessageDialog(null, "A database error has occurred: student could not be saved!", "Error", 
								WebOptionPane.ERROR_MESSAGE, null);
					}
					
				}
			}
		};
		
		submit.addActionListener(al);
		lnField.addActionListener(al);
		
		lnField.setFontSize(16);
		fnField.setFontSize(16);
		lnLabel.setFontSize(16);
		fnLabel.setFontSize(16);
		
		wf.setSize(new Dimension(350, 220));
		wf.setLocationRelativeTo(null);
		wf.setVisible(true);
	}

}
