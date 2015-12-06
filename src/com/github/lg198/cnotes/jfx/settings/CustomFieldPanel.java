package com.github.lg198.cnotes.jfx.settings;

import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.jfx.LabelButton;
import com.github.lg198.cnotes.util.ResourceManager;
import com.sun.javafx.scene.control.skin.ListViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.controlsfx.glyphfont.FontAwesome;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomFieldPanel {

    public ListView<String[]> list = new ListView<>(FXCollections.observableArrayList());

    public LabelButton addButton, remButton;

    private void reload() {
        list.setItems(FXCollections.observableArrayList());
        List<CustomField> cfs;
        try {
            cfs = DatabaseManager.getDefaultCustomFields();
        } catch (SQLException e) {
            e.printStackTrace();
            //TODO: SHOW ERROR
            return;
        }

        cfs.forEach(cf -> list.getItems().add(new String[]{cf.getName(), cf.getId()}));
    }

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
        Label placeholder = new Label("There are no custom fields.");
        list.setPlaceholder(placeholder);
        list.setId("s-cf-list");
        VBox.setVgrow(list, Priority.ALWAYS);

        list.setCellFactory(listView -> new ListCell<String[]>() {


            @Override
            protected void updateItem(String[] item, boolean empty) {
                if (getIndex() != -1) getProperties().put("index", getIndex());
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item[0]);
                }
            }
        });

        list.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String[] cf = list.getSelectionModel().getSelectedItem();
                new CustomFieldEditScreen().show(getCell(), cf[1]);
            }
        });

        box.getChildren().add(listBox);
        VBox.setVgrow(listBox, Priority.SOMETIMES);

        addButton = new LabelButton(null, ResourceManager.generateGlyph(FontAwesome.Glyph.PLUS.getChar(), 16, Color.web("#07C4B6")));
        remButton = new LabelButton(null, ResourceManager.generateGlyph(FontAwesome.Glyph.MINUS.getChar(), 16, Color.web("#07C4B6")));

        addButton.getStyleClass().add("lg-tool-box-button");
        remButton.getStyleClass().add("lg-tool-box-button");

        addButton.setOnAction(e -> {
            new CustomFieldAddScreen().show(getCell(), (s, t) -> {
                try {
                    DatabaseManager.defineCustomField(s, "", t);
                    reload();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    //TODO: SHOW ERROR NOTIFICATION
                }
            });
        });

        HBox bbox = new HBox(addButton, remButton);
        bbox.setAlignment(Pos.CENTER_RIGHT);
        bbox.getStyleClass().add("lg-tool-box");

        listBox.getChildren().add(bbox);

        reload();

        return box;
    }

    private Node getCell() {
        ListCell cell = getCell(list.getSelectionModel().getSelectedIndex());
        if (cell == null) {
            return list;
        }
        return cell;
    }

    private ListCell getCell(int index) {
        ListViewSkin lvs = (ListViewSkin) list.getSkin();
        try {
            Field flow = lvs.getClass().getSuperclass().getDeclaredField("flow");
            flow.setAccessible(true);
            VirtualFlow vf = (VirtualFlow) flow.get(lvs);
            Method getCells = VirtualFlow.class.getDeclaredMethod("getCells");
            getCells.setAccessible(true);
            List<ListCell> cells = (List<ListCell>) getCells.invoke(vf);
            for (ListCell c : cells) {
                if (c.getIndex() == index) {
                    return c;
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
