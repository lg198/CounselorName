package com.github.lg198.cnotes.jfx.settings;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class CustomFieldPanel {

    public ListView<String> list = new ListView<>(FXCollections.observableArrayList());

    public Button addButton, remButton;

    public VBox build() {
        VBox box = new VBox();
        box.setPadding(new Insets(10));
        box.setAlignment(Pos.CENTER);
        box.setSpacing(15);

        VBox listBox = new VBox();
        listBox.setAlignment(Pos.TOP_LEFT);
        listBox.setFillWidth(true);
        listBox.getChildren().add(new Label("Edit Current Custom Fields:"));
        listBox.getChildren().add(list);
        list.setPlaceholder(new Label("There are no custom fields."));
        VBox.setVgrow(list, Priority.ALWAYS);

        box.getChildren().add(listBox);
        VBox.setVgrow(listBox, Priority.SOMETIMES);

        return box;
    }
}
