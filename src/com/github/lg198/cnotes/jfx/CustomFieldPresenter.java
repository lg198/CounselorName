package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomFieldPresenter {

    public static FieldValueEditPane createEditPane(CustomField... fields) {
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.TOP_CENTER);
        gp.setPadding(new Insets(15));
        gp.setHgap(6);
        gp.setVgap(10);

        List<FieldValueAccessor> accessors = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            CustomField field = fields[i];
            accessors.add(createFieldEditPane(field, gp, i));
        }
        return new FieldValueEditPane() {
            @Override
            public GridPane getGridPane() {
                return gp;
            }

            @Override
            public List<FieldValueAccessor> getValueAccessors() {
                return accessors;
            }
        };
    }


    private static FieldValueAccessor createFieldEditPane(final CustomField cf, GridPane gp, int row) {
        Label label = new Label(cf.getName() + ":");
        gp.add(label, 0, row);
        GridPane.setValignment(label, VPos.TOP);

        FieldValueAccessor fva;

        switch (cf.getType()) {
            case TEXT:
                final TextField tf = new TextField();
                tf.setText(cf.getValue());
                gp.add(tf, 1, row);

                fva = () -> CustomField.cloneWithValue(cf, tf.getText());
                break;
            case NUMBER:
                final TextField nf = new TextField();
                nf.setText(cf.getValue());
                nf.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue.matches("\\d*")) {
                        int value = Integer.parseInt(newValue);
                        nf.getProperties().put("intValue", value);
                    } else {
                        nf.setText(oldValue);
                        nf.positionCaret(nf.getLength());
                    }
                });
                gp.add(nf, 1, row);

                fva = () -> CustomField.cloneWithValue(cf, "" + nf.getProperties().get("intValue"));
                break;
            case OPTIONS:
                try {
                    final ChoiceBox<String> obox = new ChoiceBox<>();
                    String val = DatabaseManager.getDefaultCustomFieldValue(cf.getId());
                    for (String choice : val.split(";")[1].split(",")) {
                        obox.getItems().add(choice);
                    }
                    obox.getSelectionModel().select(val.split(";")[0]);
                    gp.add(obox, 1, row);

                    fva = () -> CustomField.cloneWithValue(cf, obox.getSelectionModel().getSelectedItem());
                } catch (SQLException e) {
                    Label l = new Label("Could not load values!");
                    l.setTextFill(Color.RED);
                    gp.add(l, 1, row);

                    fva = () -> cf;
                }
                break;
            default:
                fva = () -> cf;
        }
        return fva;
    }

    public interface FieldValueAccessor {
        CustomField getCustomField();
    }

    public interface FieldValueEditPane {

        GridPane getGridPane();

        List<FieldValueAccessor> getValueAccessors();
    }

}
