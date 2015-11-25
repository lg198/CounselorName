package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.bean.Student;
import com.github.lg198.cnotes.bean.field.CustomField;
import com.github.lg198.cnotes.core.CNotesApplication;
import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.jfx.main.SearchArea;
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
import java.util.Map;

/**
 * @author lg198
 */
public class MainScreen {
    private static Image I_STUDENT_LARGE = new Image("/com/github/lg198/cnotes/res/icons/student_blue.png", 64, 64, false, true);
    public Stage primary;
    public OverlayStackPane overlay = new OverlayStackPane();
    public TabPane studentTabs = new TabPane();
    public StackPane tabStack = new StackPane();

    private SearchArea searchArea;

    public MainScreen() {
        searchArea = new SearchArea(this);
    }

    public void show(Stage stage) {
        primary = stage;
        BorderPane root = new BorderPane();
        SplitPane split = new SplitPane();
        root.setCenter(split);

        GridPane mb = createMenuBar(stage);
        root.setTop(mb);

        VBox sa = searchArea.build();
        split.getItems().add(sa);

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
        gb.add(settings).halign(HPos.RIGHT);

        return gb.build();
    }

    private void showImportWindow() {
        Platform.runLater(() -> {
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
        });
    }



}

