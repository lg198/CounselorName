package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.encryption.Encryption;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.*;

public class LoginScreen {

    public PasswordField pfield = new PasswordField();

    public LoginScreen() {
    }

    public BorderPane build(final OverlayStackPane overlay) {
        pfield.setStyle("-fx-font-size: 14px");
        final BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(15));

        VBox vb = new VBox();
        Label l = new Label("Password:");
        l.setStyle("-fx-font-size: 14px");

        HBox buttons = new HBox();
        Button enter = new Button("Enter");
        enter.setDisable(true);
        enter.setOnAction(evt -> {
            if (Encryption.get().checkPassword(pfield.getText())) {
                overlay.hideOverlayFade(Duration.seconds(0.5));
            } else {
                pfield.setStyle("-fx-focus-color: red; -fx-font-size: 14px");
                pfield.getProperties().put("incorrectPassword", "");
                Toolkit.getDefaultToolkit().beep();
                pfield.requestFocus();
                pfield.selectAll();
                /*Popup p = new Popup();
                Label message = new Label("Incorrect password!");
                message.setStyle("-fx-font-size: 16px");
                message.setTextFill(Color.FIREBRICK);
                Button ok = new Button("Okay");
                ok.setOnAction(evt1 -> p.hide());
                GridPane gp = new GridPane();
                gp.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));
                gp.setAlignment(Pos.CENTER);
                gp.add(message, 0, 0);
                gp.add(ok, 0, 1);
                GridPane.setHalignment(message, HPos.CENTER);
                GridPane.setHalignment(ok, HPos.RIGHT);
                gp.setVgap(10);
                gp.setMinSize(stage.getWidth(), stage.getHeight());
                gp.setMaxSize(stage.getWidth(), stage.getHeight());
                p.getContent().add(gp);
                p.show(parent, stage.getX() + bp.getLayoutX(), stage.getY() + bp.getLayoutY());*/
            }
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(evt -> Platform.exit());

        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.getChildren().addAll(cancel, enter);
        buttons.setSpacing(6);

        pfield.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (pfield.getProperties().containsKey("incorrectPassword")) {
                pfield.getProperties().remove("incorrectPassword");
                pfield.setStyle("-fx-font-size: 14px");
            }
            if (newValue.isEmpty()) {
                enter.setDisable(true);
            } else {
                enter.setDisable(false);
            }
        }));
        pfield.onActionProperty().bind(enter.onActionProperty());

        vb.getChildren().addAll(l, pfield, buttons);
        VBox.setMargin(buttons, new Insets(10, 0, 0, 0));
        bp.setCenter(vb);

        bp.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, null, null)));

        return bp;
    }

}
