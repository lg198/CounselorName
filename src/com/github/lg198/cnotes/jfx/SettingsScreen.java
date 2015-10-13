package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.jfx.settings.CustomFieldPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class SettingsScreen {

    private TabPane tabs = new TabPane();

    public VBox build() {
        VBox box = new VBox();
        box.setAlignment(Pos.TOP_CENTER);
        box.setSpacing(15);
        box.setPadding(new Insets(10));

        box.getChildren().add(tabs);
        VBox.setVgrow(tabs, Priority.ALWAYS);

        tabs.getTabs().addAll(
                buildCustomFieldTab()
        );

        return box;
    }

    private Tab buildCustomFieldTab() {
        Tab t = new Tab();
        t.setText("Custom Fields");
        t.setContent(new CustomFieldPanel().build());
        return t;
    }
}
