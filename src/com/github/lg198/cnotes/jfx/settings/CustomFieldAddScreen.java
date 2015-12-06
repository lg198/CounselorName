package com.github.lg198.cnotes.jfx.settings;

import com.github.lg198.cnotes.bean.field.CustomFieldType;
import com.github.lg198.cnotes.core.CNotesApplication;
import com.github.lg198.cnotes.util.GridBuilder;
import com.github.lg198.cnotes.util.ResourceManager;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.function.BiConsumer;

public class CustomFieldAddScreen {

    private TextField name = new TextField();
    private ChoiceBox<CustomFieldType> type = new ChoiceBox<>(FXCollections.observableArrayList(CustomFieldType.values()));
    private Button cancel, add;
    private PopOver popup;
    private Label warningLabel = new Label("");
    private BiConsumer<String, CustomFieldType> consumer;

    public void show(Node n, BiConsumer<String, CustomFieldType> c) {
        consumer = (s, t) -> {
            popup.hide();
            c.accept(s, t);
        };

        popup = new PopOver();
        popup.setContentNode(build());
        popup.sizeToScene();
        popup.setDetachable(false);
        popup.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
        popup.show(n);
    }

    private GridPane build() {
        GridBuilder grid = new GridBuilder();
        grid.padding(new Insets(10));
        grid.hgap(6);
        grid.vgap(10);

        name.textProperty().addListener((obs, oldv, newv) -> warningLabel.setText(""));

        grid.add(new Label("Field Name:"));
        grid.add(name);

        type.getSelectionModel().select(CustomFieldType.TEXT);
        type.setMaxWidth(Double.MAX_VALUE);

        grid.row();
        grid.add(new Label("Field Type:"));
        grid.add(type).hgrow(Priority.ALWAYS);

        type.setConverter(new StringConverter<CustomFieldType>() {
            @Override
            public String toString(CustomFieldType object) {
                return object.name().charAt(0) + object.name().substring(1, object.name().length()).toLowerCase();
            }

            @Override
            public CustomFieldType fromString(String string) {
                return CustomFieldType.valueOf(string.toUpperCase());
            }
        });

        add = new Button(null, ResourceManager.generateGlyph(FontAwesome.Glyph.CHECK.getChar(), 16, Color.LIGHTGREEN));
        cancel = new Button(null, ResourceManager.generateGlyph(FontAwesome.Glyph.CLOSE.getChar(), 16, Color.RED));

        cancel.setOnAction(e -> popup.hide());
        add.setOnAction(e -> {
            if (name.getText().trim().isEmpty()) {
                warningLabel.setText("The name cannot be blank!");
                return;
            }
            if (!name.getText().replaceAll("\\s+", "").matches("[a-zA-Z0-9\\-_]+")) {
                warningLabel.setText("The name contains invalid characters!");
                return;
            }
            consumer.accept(name.getText(), type.getSelectionModel().getSelectedItem());
            popup.hide();
        });

        warningLabel.setTextFill(Color.RED);

        HBox bottomBox = new HBox(cancel, add);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setSpacing(6);

        grid.row();
        grid.add(warningLabel).colSpan(2);

        grid.row();
        grid.add(bottomBox).halign(HPos.RIGHT).colSpan(2);

        GridPane gp = grid.build();
        gp.setStyle("-fx-background-color: whitesmoke;");
        return gp;
    }
}
