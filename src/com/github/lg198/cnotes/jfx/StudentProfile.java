package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.bean.Note;
import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.util.GridBuilder;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;

public class StudentProfile {

    private static final DateTimeFormatter FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendText(ChronoField.MONTH_OF_YEAR)
                    .appendLiteral(" ")
                    .appendText(ChronoField.DAY_OF_MONTH)
                    .appendLiteral(", at ")
                    .appendText(ChronoField.HOUR_OF_AMPM).appendLiteral(":")
                    .appendText(ChronoField.MINUTE_OF_HOUR).appendLiteral(" ")
                    .appendText(ChronoField.AMPM_OF_DAY, new HashMap<Long, String>() {{
                        put(0L, "AM");
                        put(1L, "PM");
                    }})
                    .toFormatter();
    private Student student;

    public StudentProfile(Student s) {
        student = s;
    }

    public VBox build() {
        List<CustomField> customFields;
        List<Note> notes;
        try {
            customFields = DatabaseManager.getCustomFields(student);
            notes = DatabaseManager.getNotes(student);
        } catch (SQLException ex) {
            Label l = new Label(student.getNamePretty().split(" ")[0] + "'s profile could not be loaded!");
            l.setStyle("-fx-text-fill: red; -fx-font-size: 20px;");
            VBox box = new VBox(l);
            return box;
        }
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(20));

        Label infoTitle = new Label("Student Information");
        infoTitle.getStyleClass().add("lg-profile-header");
        vbox.getChildren().add(infoTitle);

        GridPane fieldPane = createStudentInfo(customFields);
        vbox.getChildren().add(fieldPane);
        VBox.setMargin(fieldPane, new Insets(0, 0, 0, 20));

        Label notesTitle = new Label("Student Notes");
        notesTitle.getStyleClass().add("lg-profile-header");
        VBox.setMargin(notesTitle, new Insets(20, 0, 0, 0));
        vbox.getChildren().add(notesTitle);
        if (!notes.isEmpty()) {
            ScrollPane notesBox = createStudentNotes(notes);
            vbox.getChildren().add(notesBox);
            VBox.setMargin(notesBox, new Insets(0, 0, 0, 20));
        }

        return vbox;
    }

    private GridPane createStudentInfo(List<CustomField> customFields) {
        GridBuilder gb = new GridBuilder();
        gb.hgap(6);
        for (CustomField cf : customFields) {
            createCustomFieldPresentation(gb, cf);
        }
        return gb.build();
    }

    private void createCustomFieldPresentation(GridBuilder gb, CustomField cf) {
        gb.add(new Label(cf.getName() + ":"));
        gb.add(new Label(cf.getValue()));
        gb.row();
    }

    private ScrollPane createStudentNotes(List<Note> notes) {
        VBox nbox = new VBox();
        ScrollPane sp = new ScrollPane(nbox);

        for (Note n : notes) {
            nbox.getChildren().add(createNotePresentation(n));
        }

        return sp;
    }

    private TitledPane createNotePresentation(Note n) {
        TitledPane tp = new TitledPane();


        return tp;
    }

}
