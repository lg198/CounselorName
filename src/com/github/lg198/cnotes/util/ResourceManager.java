package com.github.lg198.cnotes.util;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ResourceManager {

    private static final Font FA;

    static {
        FA = Font.loadFont(ResourceManager.class.getResourceAsStream("/com/github/lg198/cnotes/res/gui/FontAwesome.ttf"), 14);
    }


    public static Label generateGlyph(char glyph, double size, Color c) {
        Label t = new Label("" + glyph);
        t.getStyleClass().add("lg-glyph");
        t.setFont(Font.font(FA.getFamily(), size));
        t.setTextFill(c);
        return t;
    }
}
