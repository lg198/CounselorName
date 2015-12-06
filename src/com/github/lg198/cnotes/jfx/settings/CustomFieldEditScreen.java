package com.github.lg198.cnotes.jfx.settings;

import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.core.CNotesApplication;
import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.jfx.Notification;
import com.github.lg198.cnotes.util.ResourceManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import org.controlsfx.control.PopOver;
import org.controlsfx.glyphfont.FontAwesome;

import java.sql.SQLException;

public class CustomFieldEditScreen {
    private static final Image I_ERROR = ResourceManager.getImage("warning_orange", 16, 16);

    public Region editable;
    public CustomField current;
    public VBox box = new VBox();

    private PopOver popup = new PopOver();

    private String defaultValue = "";

    public void acceptUpdate(Object s) {
        defaultValue = (String) s;
    }

    public void show(Node owner, String fid) {
        popup.setContentNode(build());

        load(fid);

        popup.sizeToScene();
        popup.setDetachable(false);
        popup.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
        popup.show(owner);
        popup.getContentNode().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                popup.hide();
            }
        });
    }

    private VBox build() {
        box.setPadding(new Insets(10));
        box.setStyle("-fx-background-color: whitesmoke;");
        box.setSpacing(10);

        Button close = new Button(null, ResourceManager.generateGlyph(FontAwesome.Glyph.CLOSE.getChar(), 16, Color.RED));
        close.setOnAction(e -> popup.hide());
        Button save = new Button(null, ResourceManager.generateGlyph(FontAwesome.Glyph.CHECK.getChar(), 16, Color.GREEN));
        save.setOnAction(e -> {
            try {
                DatabaseManager.setDefaultCustfomFieldValue(current.getId(), defaultValue);
                current = new CustomField(current.getId(), current.getName(), defaultValue, current.getType());
                popup.hide();
            } catch (SQLException e1) {
                e1.printStackTrace();
                showError("Failed to set field value.");
            }
        });
        HBox closeBox = new HBox(close, save);
        closeBox.setSpacing(5);
        closeBox.setAlignment(Pos.CENTER_RIGHT);
        box.getChildren().add(closeBox);
        return box;
    }

    private void load(String fid) {
        try {
            CustomField cf = DatabaseManager.getDefaultCustomField(fid);
            current = cf;
            box.getChildren().remove(editable);
            editable = cf.getType().buildEditDefaultRegion(cf.getValue(), this::acceptUpdate);
            box.getChildren().add(0, editable);
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Failed to load custom field.");
        }
    }

    private void showError(String message) {
        Notification en = new Notification(message);
        en.setGraphic(new ImageView(I_ERROR));
        en.show(CNotesApplication.PRIMARY);
    }
}
