package com.github.lg198.cnotes.core;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    
    private PasswordField pfield = new PasswordField();

    public LoginScreen() {
    }
    
    public void show(Stage s) {
        pfield.setStyle("-fx-font-size: 14px");
        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(15));
        
        VBox vb = new VBox();
        Label l = new Label("Password:");
        l.setStyle("-fx-font-size: 14px");
        l.setPadding(new Insets(0, 0, 5, 0));
        vb.getChildren().addAll(l, pfield);
        bp.setCenter(vb);
        
        Scene sc = new Scene(bp);
        
        s.setScene(sc);
        s.show();
    }
    
}
