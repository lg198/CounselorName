package com.github.lg198.cnotes.jfx.settings;

import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.database.DatabaseManager;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.List;

public class CustomFieldEditScreen {

    public Region editable;
    public CustomField current;
    public VBox box;

    public void acceptUpdate(Object s) {
        String newdef = (String) s;
        try {
            DatabaseManager.setDefaultCustfomFieldValue(current.getId(), newdef);
        } catch (SQLException e) {
            e.printStackTrace();
            //todo: show error at bottom of screen
        }
        current = new CustomField(current.getId(), current.getName(), newdef, current.getType());
    }

    public void build() {

    }

    public void load(String fid) {
        try {
            CustomField cf = DatabaseManager.getDefaultCustomField(fid);
            current = cf;
            box.getChildren().remove(editable);
            editable = cf.getType().buildEditDefaultRegion(cf.getValue(), this::acceptUpdate);
            box.getChildren().add(editable);
        } catch (SQLException e) {
            e.printStackTrace();
            //todo: show error at bottom of screen
        }
    }
}
