package com.github.lg198.cnotes.jfx;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;

public class LabelButton extends Label {

    private static final PseudoClass HOVER = PseudoClass.getPseudoClass("hover");
    private static final PseudoClass PRESSED = PseudoClass.getPseudoClass("pressed");

    private EventHandler<ActionEvent> handler;
    private BooleanProperty hover = new BooleanPropertyBase(false) {

        @Override
        protected void invalidated() {
            pseudoClassStateChanged(HOVER, get());
        }

        @Override
        public Object getBean() {
            return LabelButton.this;
        }

        @Override
        public String getName() {
            return "hover";
        }
    };
    private BooleanProperty pressed = new BooleanPropertyBase(false) {

        @Override
        protected void invalidated() {
            pseudoClassStateChanged(PRESSED, get());
        }

        @Override
        public Object getBean() {
            return LabelButton.this;
        }

        @Override
        public String getName() {
            return "pressed";
        }
    };

    public LabelButton(String s) {
        super(s);
        init();
    }

    public LabelButton(String s, Node n) {
        super(s, n);
        init();
    }

    private void init() {
        this.setCursor(Cursor.HAND);
        this.setOnMouseClicked(e -> {
            if (handler != null) handler.handle(new ActionEvent(this, null));
        });
        this.setOnMouseEntered(e -> hover.set(true));
        this.setOnMouseExited(e -> hover.set(false));
        this.setOnMousePressed(e -> pressed.set(true));
        this.setOnMouseReleased(e -> pressed.set(false));

        this.setPadding(new Insets(5));
    }

    public void setOnAction(EventHandler<ActionEvent> h) {
        handler = h;
    }

    public void square() {
        this.widthProperty().addListener(observable -> {
            double max1 = Math.max(getWidth(), getHeight());
            setMinWidth(max1);
            setMinHeight(max1);
            setPrefSize(max1, max1);
        });
    }


}
