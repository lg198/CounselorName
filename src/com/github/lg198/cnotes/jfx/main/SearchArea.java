package com.github.lg198.cnotes.jfx.main;

import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.jfx.*;
import com.github.lg198.cnotes.util.ResourceManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchArea {
    private static Image I_SEARCH = new Image("/com/github/lg198/cnotes/res/icons/search_blue.png", 16, 16, false, true);
    private static Image I_STUDENT = new Image("/com/github/lg198/cnotes/res/icons/student_blue.png", 16, 16, false, true);

    private Button addStudent, remStudent;

    public CustomTextField searchField = new CustomTextField();
    public ListView<String> studentList;
    public List<Student> currentStudents = new ArrayList<>();

    private MainScreen parent;

    public SearchArea(MainScreen p) {
        parent = p;
    }

    public VBox build() {
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);

        searchField.setPromptText("Search Students");
        ImageView iv = new ImageView(I_SEARCH);
        searchField.setLeft(ResourceManager.generateGlyph(FontAwesome.Glyph.SEARCH.getChar(), 12, Color.BLUE));
        HBox.setHgrow(searchField, Priority.ALWAYS);
        HBox.setMargin(iv, new Insets(0, 3, 0, 3));
        vb.getChildren().add(searchField);

        studentList = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        studentList.setItems(items);
        resetStudentListPlaceholder();
        VBox.setVgrow(studentList, Priority.ALWAYS);
        vb.getChildren().add(studentList);

        studentList.setCellFactory(param -> {
            TextFieldListCell<String> s = new TextFieldListCell<>();
            s.setGraphic(new ImageView(I_STUDENT));
            s.setContentDisplay(ContentDisplay.LEFT);
            return s;
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) ->
                searchStudents());

        studentList.setOnMouseClicked(event -> {
            if (event.getClickCount() > 1 && studentList.getSelectionModel().getSelectedIndex() >= 0) {
                Student s = currentStudents.get(studentList.getSelectionModel().getSelectedIndex());
                Tab exists = null;
                for (Tab t : parent.studentTabs.getTabs()) {
                    if (t.getProperties().get("studentId").equals(s.getId())) {
                        exists = t;
                    }
                }
                if (exists == null) {
                    exists = generateTab(s);
                    parent.studentTabs.getTabs().add(exists);
                }
                parent.studentTabs.getSelectionModel().select(exists);
            }
        });

        HBox addrem = new HBox();
        addrem.getStyleClass().add("lg-tool-box");
        addrem.setAlignment(Pos.CENTER_RIGHT);
        Button addStudent = new Button(null, ResourceManager.generateGlyph(FontAwesome.Glyph.PLUS.getChar(), 28, Color.web("#07C4B6")));
        addStudent.setId("addStudent");
        Insets bi = new Insets(0, 5, 0, 5);
        addStudent.setPadding(bi);
        Button remStudent = new Button(null, ResourceManager.generateGlyph(FontAwesome.Glyph.MINUS.getChar(), 28, Color.web("#07C4B6")));
        remStudent.setId("remStudent");
        remStudent.setPadding(bi);

        addStudent.getStyleClass().add("lg-tool-box-button");
        remStudent.getStyleClass().add("lg-tool-box-button");

        addStudent.setOnAction((ae) -> showAddStudentDialog());
        remStudent.setOnAction((ae) -> {
            if (currentStudents.isEmpty()) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }
            int i = studentList.getSelectionModel().getSelectedIndex();
            Student selected = currentStudents.get(i);
            try {
                DatabaseManager.deleteStudent(selected);
                currentStudents.remove(i);
                studentList.getItems().remove(i);
            } catch (SQLException e) {
                new ExceptionAlert("Error", "Could not remove student", "A database exception occurred and the specified student profile cannot be deleted", e);
            }
            studentList.requestFocus();
        });

        addrem.getChildren().addAll(addStudent, remStudent);

        vb.getChildren().add(addrem);

        vb.setMaxWidth(250);
        vb.setMinWidth(250);

        return vb;
    }

    public void searchStudents() {
        if (studentList.getPlaceholder().getProperties().containsKey("uhoh")) resetStudentListPlaceholder();
        try {
            List<Student> l = DatabaseManager.searchStudents(searchField.getText().trim());
            studentList.getItems().clear();
            for (Student s : l) {
                studentList.getItems().add(s.getNamePretty());
            }
            currentStudents = l;
        } catch (SQLException ex) {
            Label uhoh = new Label("Unable to connect to database!");
            uhoh.getStyleClass().add("lg-warning-label");
            uhoh.getProperties().put("uhoh", "true");
            studentList.setPlaceholder(uhoh);
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void resetStudentListPlaceholder() {
        studentList.setPlaceholder(new Label("Search students using the box above."));
    }

    private Tab generateTab(Student s) {
        Tab t = new Tab(s.getNamePretty());
        t.getProperties().put("studentId", s.getId());
        t.setGraphic(new ImageView(I_STUDENT));
        StudentProfile sp = new StudentProfile(s);
        t.setContent(sp.build());
        return t;
    }

    private void showAddStudentDialog() {
        Platform.runLater(() -> {
            GridPane grid = new GridPane();
            grid.setHgap(6);
            grid.setVgap(10);
            grid.setPadding(new Insets(15));

            final TextField firstName = new TextField();
            grid.add(new Label("First Name:"), 0, 0);
            grid.add(firstName, 1, 0);

            final TextField lastName = new TextField();
            grid.add(new Label("Last Name:"), 0, 1);
            grid.add(lastName, 1, 1);

            CustomField[] fields;
            try {
                fields = DatabaseManager.getDefaultCustomFields().toArray(new CustomField[0]);
            } catch (SQLException e) {
                fields = new CustomField[0];
            }
            CustomFieldPresenter.FieldValueEditPane fvep = CustomFieldPresenter.createEditPane(fields);
            GridPane fieldGrid = fvep.getGridPane();
            final List<CustomFieldPresenter.FieldValueAccessor> accessors = fvep.getValueAccessors();

            TitledPane collapse = new TitledPane("Student Fields", fieldGrid);
            collapse.setExpanded(false);
            collapse.setAnimated(true);
            //collapse.heightProperty().addListener((obs, oldHeight, newHeight) -> stage.sizeToScene());


            grid.add(collapse, 0, 2, 2, 1);
            GridPane.setMargin(collapse, new Insets(10, 0, 0, 0));

            final Button submit = new Button("Add Student");
            submit.setOnAction((event) -> {
                submit.requestFocus();
                String fn = firstName.getText();
                String ln = lastName.getText();

                if (fn.replaceAll("\\s+", "").isEmpty()) {
                    PopOver po = new PopOver();
                    po.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                    po.setDetachable(false);
                    po.setContentNode(new Label("The student's first name cannot be empty!"));
                    po.show(firstName);
                    firstName.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (po.isShowing()) po.hide();
                    });
                    return;
                } else if (ln.replaceAll("\\s+", "").isEmpty()) {
                    PopOver po = new PopOver();
                    po.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);
                    po.setDetachable(false);
                    po.setContentNode(new Label("The student's last name cannot be empty!"));
                    po.show(lastName);
                    lastName.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (po.isShowing()) po.hide();
                    });
                    return;
                }

                try {
                    Student s = DatabaseManager.addStudent(fn, ln);

                    for (CustomFieldPresenter.FieldValueAccessor fva : accessors) {
                        DatabaseManager.updateCustomField(s, fva.getCustomField());
                    }

                    parent.overlay.hideOverlayFade(Duration.seconds(0.5));
                    Notification sn = new Notification("Student Added");
                    sn.setGraphic(new ImageView(I_STUDENT));
                    sn.show(parent.primary);

                    searchStudents();
                } catch (SQLException e) {
                    new ExceptionAlert("Error", "Could not create student", "A database exception occurred and the specified student profile cannot be created", e);
                }
            });
            grid.add(submit, 1, 3);
            GridPane.setHalignment(submit, HPos.RIGHT);
            GridPane.setMargin(submit, new Insets(10, 0, 0, 0));
            grid.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));

            parent.overlay.showOverlayFade(grid, Duration.seconds(0.5));

            firstName.requestFocus();
        });
    }
}
