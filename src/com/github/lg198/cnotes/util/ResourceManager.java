package com.github.lg198.cnotes.util;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

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

    public static String getStylesheet(String name) {
        if (!name.endsWith(".css")) {
            name = name + ".css";
        }
        return ResourceManager.class.getResource("/com/github/lg198/cnotes/res/gui/" + name).toExternalForm();
    }

    private static Map<String, Map<String, Image>> imageCache = new HashMap<>();

    public static Image getImage(String id, int width, int height) {
        String path = "/com/github/lg198/cnotes/res/icons/" + id + (id.endsWith(".png") ? "" : ".png");

        if (!imageCache.containsKey(path)) {
            Map<String, Image> sizes = new HashMap<>();
            sizes.put(width + ":" + height, new Image(ResourceManager.class.getResource(path).toExternalForm(), width, height, false, true));
            imageCache.put(path, sizes);
        }
        return imageCache.get(path).get(width + ":" + height);
    }
}
