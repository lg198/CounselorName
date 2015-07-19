package com.github.lg198.cnotes.core;

import com.github.lg198.cnotes.database.DatabaseManager;
import com.github.lg198.cnotes.encryption.Encryption;
import com.github.lg198.cnotes.jfx.ExceptionAlert;
import com.github.lg198.cnotes.jfx.MainScreen;
import com.github.lg198.cnotes.jfx.StartupScreen;
import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CNotesApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            CounselorNotesMain.main(new String[0]);
        } catch (Exception ex) {
            new ExceptionAlert("Error on Startup", "Unable to start CounselorNotes", "CounselorNotes was unable to start due to a " + ex.getClass().getSimpleName() + ".", ex);
            return;
        }

        try {
            if (!DatabaseManager.hasProfileField(Encryption.PASS_KEY)) {
                new StartupScreen().show(primaryStage);
            } else {
                Encryption.init();
                MainScreen ms = new MainScreen();
                ms.show(primaryStage);
            }
        } catch (SQLException e) {
            new ExceptionAlert("Error on Startup", "Unable to start CounselorNotes", "CounselorNotes was unable to start due to a " + e.getClass().getSimpleName() + ".", e);
        }

    }

}
