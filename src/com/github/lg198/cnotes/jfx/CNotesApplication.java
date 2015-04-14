package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.core.CounselorNotesMain;
import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CNotesApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            CounselorNotesMain.main(new String[0]);
        } catch (Exception ex) {
            new ExceptionAlert("Error on Startup", "Unable to start CounselorNotes", "CounselorNotes was unable to start due to a " + ex.getClass().getSimpleName() + ".", ex);
            return;
        }
        new MainScreen().show(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
