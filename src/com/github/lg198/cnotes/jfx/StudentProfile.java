package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.database.DatabaseManager;
import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class StudentProfile {

    private Student student;

    public StudentProfile(Student s) {
        student = s;
    }

    public BorderPane build() {
        List<CustomField> customFields = null;
        try {
             customFields = DatabaseManager.getCustomFields(student);
        } catch (SQLException ex) {
            Label l = new Label(student.getNamePretty().split(" ")[0] + "'s profile could not be loaded!");
            l.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");
            BorderPane bp = new BorderPane();
            bp.setCenter(l);
            return bp;
        }
        BorderPane bp = new BorderPane();
        GridPane gp = new GridPane();
        
        for (CustomField cf : customFields) {
            
        }                
        
        bp.setCenter(gp);
        return bp;
    }

}
