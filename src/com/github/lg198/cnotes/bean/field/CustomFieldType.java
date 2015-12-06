package com.github.lg198.cnotes.bean.field;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public enum CustomFieldType {

    TEXT {
        @Override
        public String getPresentableDefault(String dv) {
            return dv;
        }

        @Override
        public Region buildEditDefaultRegion(String def, Consumer c) {
            VBox box = new VBox();
            TextField field = new TextField();
            field.setText(def);
            field.textProperty().addListener((obs, o, n) -> c.accept(n));
            box.getChildren().add(field);
            return box;
        }
    },

    NUMBER {
        @Override
        public String getPresentableDefault(String dv) {
            return dv;
        }

        @Override
        public Region buildEditDefaultRegion(String def, Consumer c) {
            VBox box = new VBox();
            TextField field = new TextField();
            field.setText(def);
            field.textProperty().addListener((obs, o, n) -> {
                if (!n.matches("[0-9]+(\\.[0-9]+)?")) {
                    field.setText(o);
                    return;
                }
                c.accept(n);
            });
            box.getChildren().add(field);
            return box;
        }
    },

    OPTIONS {
        @Override
        public String getPresentableDefault(String dv) {
            //DefaultValue;value1,value2,value3
            return dv.split(";")[0];
        }

        @Override
        public Region buildEditDefaultRegion(String def, Consumer c) {
            VBox box = new VBox();
            ObservableList l;
            String defval;
            if (def.isEmpty()) {
                l = FXCollections.observableArrayList();
                defval = "";
            } else {
                String vals = def.split(";")[1];
                defval = def.split(";")[0];
                l = FXCollections.observableArrayList(vals.split(","));
            }
            ListView<String> lv = new ListView(l);
            lv.setEditable(true);
            lv.setCellFactory(TextFieldListCell.forListView());

            box.getChildren().add(lv);

            lv.getItems().addListener((ListChangeListener<? super String>) change -> {
                c.accept(defval + (defval.isEmpty() ? ";" : "") + String.join(",", lv.getItems()));
            });

            lv.setOnEditCommit(event -> {
                if (event.getNewValue().trim().isEmpty()) {
                    if (event.getIndex() >= lv.getItems().size()) {
                        return;
                    }
                    lv.getItems().remove(event.getIndex());
                } else {
                    lv.getItems().add(event.getIndex(), event.getNewValue());
                }
            });

            return box;
        }
    };

    public abstract String getPresentableDefault(String dv);

    public abstract Region buildEditDefaultRegion(String def, Consumer c);

}
