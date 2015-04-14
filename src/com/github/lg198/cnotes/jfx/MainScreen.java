package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.database.DatabaseManager;
import java.sql.SQLException;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javax.swing.SingleSelectionModel;

/**
 *
 * @author lg198
 */
public class MainScreen {

    private TextField searchField = new TextField();
    private ListView<String> studentList;
    private TabPane studentTabs = new TabPane();
    
    private static Image I_STUDENT = new Image("/com/github/lg198/cnotes/res/gui/student_blue.png", 16, 16, false, true);

    public void show(Stage stage) {
        BorderPane root = new BorderPane();
        SplitPane split = createSplitPane();
        root.setCenter(split);

        VBox searchArea = createSearchArea();
        split.getItems().add(searchArea);

        split.getItems().add(studentTabs);

        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(CNotesApplication.class.getResource("/com/github/lg198/cnotes/res/gui/main_style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("CounselorNotes");
        stage.show();
    }

    private SplitPane createSplitPane() {
        SplitPane sp = new SplitPane();

        return sp;
    }

    private VBox createSearchArea() {
        VBox vb = new VBox();
        vb.setAlignment(Pos.CENTER);

        searchField.setStyle("-fx-background-radius: 5px;");
        searchField.setPromptText("Search Students");
        vb.getChildren().add(searchField);

        studentList = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList();
        studentList.setItems(items);
        studentList.setPlaceholder(new Label("Search students using the box above."));
        VBox.setVgrow(studentList, Priority.ALWAYS);
        vb.getChildren().add(studentList);

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    List<Student> l = DatabaseManager.searchStudents(searchField.getText());
                    studentList.getItems().clear();
                    for (Student s : l) {
                        studentList.getItems().add(s.getNamePretty());
                    }
                } catch (SQLException ex) {
                    //TODO: COULD NOT CONNECT TO DATABASE
                }
            }
        });
        
        studentList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() > 1) {
                    String sstudent = studentList.getSelectionModel().getSelectedItem();
                    String sname = sstudent.toUpperCase();
                    try {
                        Student s = DatabaseManager.getStudentFromName(sname);
                        Tab exists = null;
                        for (Tab t : studentTabs.getTabs()) {
                            if (t.getText().equals(sstudent)) {
                                exists = t;
                            }
                        }
                        if (exists==null) {
                            exists = generateTab(s);
                            studentTabs.getTabs().add(exists);
                        }
                        studentTabs.getSelectionModel().select(exists);

                    } catch (SQLException ex) {
                        new ExceptionAlert("Error", "Could not open student profile", "A database exception occurred and the specified student profile cannot be loaded", ex);
                    }
                }
            }
            
        });
        
        HBox addrem = new HBox();
        addrem.setAlignment(Pos.CENTER_RIGHT);
        Button addStudent = new Button("+");
        addStudent.setId("addStudent");
        Insets bi = new Insets(0, 5, 0, 5);
        addStudent.setPadding(bi);
        Font buttonFont = Font.font(addStudent.getFont().getFamily(), FontWeight.EXTRA_BOLD, 30);
        Button remStudent = new Button("-");
        remStudent.setId("remStudent");
        remStudent.setPadding(bi);
        addStudent.setFont(buttonFont);
        remStudent.setFont(buttonFont);
        addrem.getChildren().addAll(addStudent, remStudent);
        
        vb.getChildren().add(addrem);

        vb.setMaxWidth(250);
        vb.setMinWidth(250);

        return vb;
    }
    
    private Tab generateTab(Student s) {
        Tab t = new Tab(s.getNamePretty());
        t.setGraphic(new ImageView(I_STUDENT));
        //t.setContent(Node n);
        return t;
    }

}
