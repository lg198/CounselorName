package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.core.CNotesApplication;
import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.util.GridBuilder;
import com.github.lg198.cnotes.util.ResourceManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.glyphfont.FontAwesome;

import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lg198
 */
public class MainScreen {

    private static Image I_STUDENT = new Image("/com/github/lg198/cnotes/res/icons/student_blue.png", 16, 16, false, true);
    private static Image I_STUDENT_LARGE = new Image("/com/github/lg198/cnotes/res/icons/student_blue.png", 64, 64, false, true);
    private static Image I_IMPORT = new Image("/com/github/lg198/cnotes/res/icons/import_blue.png", 16, 16, false, true);
    private static Image I_SEARCH = new Image("/com/github/lg198/cnotes/res/icons/search_blue.png", 16, 16, false, true);
    private Stage primary;
    private OverlayStackPane overlay = new OverlayStackPane();
    private CustomTextField searchField = new CustomTextField();
    private ListView<String> studentList;
    private List<Student> currentStudents = new ArrayList<>();
    private TabPane studentTabs = new TabPane();
    private StackPane tabStack = new StackPane();

    public void show(Stage stage) {
        primary = stage;
        BorderPane root = new BorderPane();
        SplitPane split = createSplitPane();
        root.setCenter(split);

        GridPane mb = createMenuBar(stage);
        root.setTop(mb);

        VBox searchArea = createSearchArea();
        split.getItems().add(searchArea);

        tabStack.getChildren().addAll(studentTabs, createTabPlaceholder());
        studentTabs.getTabs().addListener((ListChangeListener<Tab>) c -> {
            if (!c.next()) {
                return;
            }
            if (c.wasRemoved() && c.getList().isEmpty() && tabStack.getChildren().size() == 1) {
                tabStack.getChildren().add(1, createTabPlaceholder());
            } else if (c.wasAdded() && tabStack.getChildren().size() > 1) {
                tabStack.getChildren().remove(1);
            }
        });

        split.getItems().add(tabStack);

        overlay.getChildren().addAll(root);

        LoginScreen loginScreen = new LoginScreen();
        BorderPane login = loginScreen.build(overlay);
        overlay.showOverlay(login);
        login.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) event.consume();
        });

        Scene scene = new Scene(overlay, 1000, 680);
        scene.getStylesheets().add(CNotesApplication.class.getResource("/com/github/lg198/cnotes/res/gui/main_style.css").toExternalForm());
        scene.getStylesheets().add(CNotesApplication.class.getResource("/com/github/lg198/cnotes/res/gui/blueStyle.css").toExternalForm());

        stage.setScene(scene);
        stage.getIcons().add(I_STUDENT_LARGE);
        stage.setTitle("CounselorNotes");

        stage.show();

        loginScreen.pfield.requestFocus();
    }

    public GridPane createTabPlaceholder() {
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(10);
        gp.getStyleClass().add("lg-tab-placeholder");
        Label header = new Label("Student info goes here");
        header.getStyleClass().add("lg-tab-placeholder-title");
        Label subtitle = new Label("Open student profiles by searching in the search pane on the left");
        subtitle.getStyleClass().add("lg-tab-placeholder-subtitle");
        gp.add(ResourceManager.generateGlyph(FontAwesome.Glyph.GRADUATION_CAP.getChar(), 80, Color.web("#051357")), 0, 0, 1, 2);
        gp.add(header, 1, 0);
        gp.add(subtitle, 1, 1);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(gp.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.25), new KeyValue(gp.opacityProperty(), 1))
        );
        tl.playFromStart();
        return gp;
    }


    private SplitPane createSplitPane() {
        SplitPane sp = new SplitPane();

        return sp;
    }

    private VBox createSearchArea() {
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);

        searchField.setPromptText("Search Students");
        ImageView iv = new ImageView(I_SEARCH);
        searchField.setLeft(ResourceManager.generateGlyph(FontAwesome.Glyph.SEARCH.getChar(), 12, Color.BLUE));
        HBox.setHgrow(searchField, Priority.ALWAYS);
        HBox.setMargin(iv, new Insets(0, 3, 0, 3));
        vb.getChildren().add(searchField);

        studentList = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();
        studentList.setItems(items);
        resetStudentListPlaceholder();
        VBox.setVgrow(studentList, Priority.ALWAYS);
        vb.getChildren().add(studentList);

        studentList.setCellFactory(param -> {
            TextFieldListCell<String> s = new TextFieldListCell<String>();
            s.setGraphic(new ImageView(I_STUDENT));
            s.setContentDisplay(ContentDisplay.LEFT);
            return s;
        });

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                searchStudents();
            }
        });

        studentList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1 && studentList.getSelectionModel().getSelectedIndex() >= 0) {
                    Student s = currentStudents.get(studentList.getSelectionModel().getSelectedIndex());
                    Tab exists = null;
                    for (Tab t : studentTabs.getTabs()) {
                        if (t.getProperties().get("studentId").equals(s.getId())) {
                            exists = t;
                        }
                    }
                    if (exists == null) {
                        exists = generateTab(s);
                        studentTabs.getTabs().add(exists);
                    }
                    studentTabs.getSelectionModel().select(exists);
                }
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

        addStudent.setOnAction((ae) -> {
            showAddStudentDialog();
        });
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

    private Tab generateTab(Student s) {
        Tab t = new Tab(s.getNamePretty());
        t.getProperties().put("studentId", s.getId());
        t.setGraphic(new ImageView(I_STUDENT));
        StudentProfile sp = new StudentProfile(s);
        t.setContent(sp.build());
        return t;
    }

    private GridPane createMenuBar(final Stage stage) {
        final GridBuilder gb = new GridBuilder();
        gb.hgap(10);
        gb.build().getStyleClass().add("lg-menu-box");
        /*
        final ContextMenu file = new ContextMenu();
        MenuItem importItem = new MenuItem("Import...");
        importItem.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                showImportWindow();
                file.hide();
            }
        });
        importItem.setGraphic(ResourceManager.generateGlyph(FontAwesome.Glyph.DOWNLOAD.getChar(), 16, Color.BLUE));
        file.getItems().addAll(importItem);

        Button fileButton = new Button("File");

        fileButton.setContextMenu(file);

        fileButton.setOnAction(event -> file.show(fileButton, Side.BOTTOM, 0, 0));*/

        Button ibutton = new Button("Import Students", ResourceManager.generateGlyph(FontAwesome.Glyph.DOWNLOAD.getChar(), 25, Color.BLUE));
        ibutton.setContentDisplay(ContentDisplay.LEFT);
        ibutton.setOnAction(event -> showImportWindow());
        gb.add(ibutton);

        Rectangle spaceRect = new Rectangle();
        spaceRect.setFill(null);
        gb.add(spaceRect).hgrow(Priority.ALWAYS);

        Button settings = new Button(null, ResourceManager.generateGlyph(FontAwesome.Glyph.GEAR.getChar(), 25, Color.BLACK));
        settings.setOnAction(event -> Platform.runLater(() -> {
            overlay.showOverlayFade(new SettingsScreen().build(), Duration.seconds(0.5));
        }));
        settings.setId("settings-button");
        settings.applyCss();
        gb.add(settings).

                halign(HPos.RIGHT);

        return gb.build();
    }

    private void showImportWindow() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                BorderPane root = new BorderPane();
                root.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
                GridBuilder builder = new GridBuilder();

                Label l1 = new Label("Path:");
                builder.add(l1);

                final TextField pathField = new TextField();
                builder.add(pathField);

                final FileChooser chooser = new FileChooser();
                final Button browseButton = new Button("Browse");
                browseButton.setOnAction(event -> {
                    File f = chooser.showOpenDialog(primary);
                    if (f != null) {
                        pathField.setText(f.getAbsolutePath());
                    }
                });
                builder.add(browseButton);
                builder.row();

                Button submit = new Button("Submit");
                submit.setOnAction(event -> overlay.hideOverlayFade(Duration.seconds(0.5)));

                builder.add(submit).colSpan(3).halign(HPos.RIGHT).margin(new Insets(10, 0, 0, 0));

                GridPane grid = builder.build();
                grid.setPadding(new Insets(10));
                grid.setAlignment(Pos.CENTER);
                grid.setVgap(10);
                grid.setHgap(5);
                root.setCenter(grid);

                overlay.showOverlayFade(root, Duration.seconds(0.5));
            }
        });
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

                    overlay.hideOverlayFade(Duration.seconds(0.5));
                    Notification sn = new Notification("Student Added");
                    sn.setGraphic(new ImageView(I_STUDENT));
                    sn.show(primary);

                    searchStudents();
                } catch (SQLException e) {
                    new ExceptionAlert("Error", "Could not create student", "A database exception occurred and the specified student profile cannot be created", e);
                }
            });
            grid.add(submit, 1, 3);
            GridPane.setHalignment(submit, HPos.RIGHT);
            GridPane.setMargin(submit, new Insets(10, 0, 0, 0));
            grid.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));

            overlay.showOverlayFade(grid, Duration.seconds(0.5));

            firstName.requestFocus();
        });
    }


    private void searchStudents() {
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

    private void resetStudentListPlaceholder() {
        studentList.setPlaceholder(new Label("Search students using the box above."));
    }
}

