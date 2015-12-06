package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.backup.BackupFrequency;
import com.github.lg198.cnotes.core.CounselorNotesMain;
import com.github.lg198.cnotes.core.Settings;
import com.github.lg198.cnotes.encryption.Encryption;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author lg198
 */
public class StartupScreen {

    public void show(Stage stage) {


        GridPane grid = new GridPane();
        //grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setId("maingrid");
        grid.setVgap(16);
        grid.setHgap(10);

        Label plabel = new Label("Password:");
        plabel.getStyleClass().add("lg-label");
        GridPane.setHalignment(plabel, HPos.RIGHT);
        grid.add(plabel, 0, 0);
        final PasswordField pfield = new PasswordField();
        //pfield.setStyle("-fx-font-size: 16px");
        grid.add(pfield, 1, 0);

        grid.setVgap(5);

        Label pclabel = new Label("Confirm Password:");
        pclabel.getStyleClass().add("lg-label");
        GridPane.setHalignment(pclabel, HPos.RIGHT);
        grid.add(pclabel, 0, 1);
        final PasswordField pcfield = new PasswordField();
        GridPane.setMargin(pcfield, new Insets(0, 0, 16, 0));
        GridPane.setMargin(pclabel, new Insets(0, 0, 16, 0));
        //pcfield.setStyle("-fx-font-size: 16px");
        grid.add(pcfield, 1, 1);

        Label blabel = new Label("Backup Schedule:");
        blabel.getStyleClass().add("lg-label");
        GridPane.setHalignment(blabel, HPos.RIGHT);
        grid.add(blabel, 0, 2);
        String[] options = new String[BackupFrequency.values().length];
        for (int i = 0; i < options.length; i++) {
            options[i] = Arrays.stream(BackupFrequency.values()[i].name().toLowerCase().split("_"))
                               .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1, s.length()))
                               .collect(Collectors.joining(" "));
        }
        final ChoiceBox backupChoices = new ChoiceBox(FXCollections.observableArrayList(options));
        //cb.setStyle("-fx-font-size: 16px");
        backupChoices.setMaxWidth(Double.MAX_VALUE);
        backupChoices.getSelectionModel().select(BackupFrequency.defaultFrequency());
        GridPane.setMargin(backupChoices, new Insets(0, 0, 16, 0));
        GridPane.setMargin(blabel, new Insets(0, 0, 16, 0));
        grid.add(backupChoices, 1, 2);

        HBox bbox = new HBox();
        bbox.setSpacing(6);
        Button submit = new Button("Submit");
        submit.setStyle("-fx-cursor: hand;");
        submit.setOnAction(event -> {
            if (!pfield.getText().equals(pcfield.getText())) {
                Alert a = new Alert(AlertType.WARNING);
                a.setTitle("");
                a.setHeaderText("Cannot create profile");
                a.setContentText("The passwords do not match!");
                a.showAndWait();
                return;
            }
            if (pfield.getText().isEmpty()) {
                Alert a = new Alert(AlertType.WARNING);
                a.setTitle("");
                a.setHeaderText("Cannot create profile");
                a.setContentText("The passwords may not be blank!");
                a.showAndWait();
                return;
            }

            Settings.setBackupFrequency(BackupFrequency.values()[backupChoices.getSelectionModel().getSelectedIndex()]);

            try {
                Platform.setImplicitExit(false);
                stage.hide();
                Encryption.init(new String(pfield.getText()));
                Stage primary = new Stage();
                MainScreen ms = new MainScreen();
                ms.show(primary);
                Platform.setImplicitExit(true);
            } catch (SQLException ex) {
                new ExceptionAlert("Error", "Unable to save password...", "CounselorNotes was unable to save your password profile.", ex);
            }
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> {
            FadeTransition ft = new FadeTransition();
            ft.setNode(grid);
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setDuration(Duration.seconds(0.5));
            ft.setOnFinished(evt -> Platform.exit());
            ft.playFromStart();
        });
        bbox.setAlignment(Pos.CENTER_RIGHT);
        bbox.getChildren().addAll(cancel, submit);
        grid.add(bbox, 0, 3, 2, 1);

        Scene scene = new Scene(grid);
        scene.setFill(null);

        stage.setScene(scene);
        stage.setTitle("Create Counselor Profile");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.sizeToScene();

        if (!stage.isShowing()) {
            stage.show();
        }
    }

}
