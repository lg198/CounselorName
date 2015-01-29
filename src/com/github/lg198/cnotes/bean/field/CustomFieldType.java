package com.github.lg198.cnotes.bean.field;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.alee.extended.transition.ComponentTransition;
import com.alee.extended.transition.effects.Direction;
import com.alee.extended.transition.effects.curtain.CurtainTransitionEffect;
import com.alee.extended.transition.effects.curtain.CurtainType;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;

public enum CustomFieldType{

	TEXT {

		@Override
		public WebPanel getPanel(final String fieldName, String fieldValue) {
			final WebButton button = new WebButton(fieldValue);
			final WebTextField field = new WebTextField();
			field.setInputPrompt("Edit " + fieldName + "...");
			field.setText(fieldValue);
			field.setFontSize(16);
			
			button.setRolloverDecoratedOnly(true);
			button.setFontSize(16);

			final ComponentTransition componentTransition = new ComponentTransition();
			final CurtainTransitionEffect effect = new CurtainTransitionEffect();
			effect.setType(CurtainType.slide);
			effect.setSize(1000);
			effect.setSpeed(30);
			effect.setMinimumSpeed(20);
			effect.setDirection(Direction.right);
			componentTransition.setTransitionEffect(effect);
			componentTransition.setContent(button);

			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					componentTransition.performTransition(field);
				}
			});
			field.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String txt = field.getText().trim();
					if (txt.isEmpty()) {
						txt = "Set " + fieldName + "...";
					}
					button.setText(txt);
					componentTransition.performTransition(button);
					if (!txt.isEmpty()) {

					}
				}
			});
			
			field.addFocusListener(new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
				}

				@Override
				public void focusLost(FocusEvent e) {
					componentTransition.performTransition(button);					
				}
				
			});
			
			field.getDocument().addDocumentListener(new DocumentListener() {
				
				private void change() {
					Font f = field.getFont();
					FontMetrics fm = field.getFontMetrics(f);
					int twidth = (int) fm.getStringBounds(field.getText() + "   ", (Graphics2D) field.getGraphics()).getWidth();
					if (twidth > field.getWidth()) {
						field.setSize(twidth + 10, field.getHeight());
						field.getParent().revalidate();
						field.getParent().repaint();
					}
				}

				@Override
				public void changedUpdate(DocumentEvent arg0) {
					change();
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					change();
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					change();
				}
				
			});

			return componentTransition;
		}

	},

	NUMBER {

		@Override
		public WebPanel getPanel(String fieldName, String fieldValue) {
			// TODO Auto-generated method stub
			return null;
		}

	},

	OPTIONS {

		@Override
		public WebPanel getPanel(String fieldName, String fieldValue) {
			// TODO Auto-generated method stub
			return null;
		}

	};
	
	public abstract WebPanel getPanel(String fieldDame, String fieldName);

}
