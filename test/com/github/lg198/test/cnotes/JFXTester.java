package com.github.lg198.test.cnotes;

import com.github.lg198.cnotes.jfx.EditorRegion;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JFXTester extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        Scene sc = new Scene(new EditorRegion());
        stage.setScene(sc);
        stage.sizeToScene();

        stage.show();
    }
}
