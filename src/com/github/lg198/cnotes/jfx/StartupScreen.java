
package com.github.lg198.cnotes.jfx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author lg198
 */
public class StartupScreen {
    
    public void show(Stage stage) {
        stage.initStyle(StageStyle.TRANSPARENT);

        Font titleFont = Font.loadFont(CNotesApplication.class.getResource("/com/github/lg198/cnotes/res/gui/Roboto-Regular.ttf").toExternalForm(), 20);

        StackPane sp = new StackPane();

        BorderPane bp = new BorderPane();

        FlowPane fp = new FlowPane();
        fp.setAlignment(Pos.CENTER);

        HBox toolbar = new HBox();
        toolbar.getStyleClass().add("lg-toolbar");
        toolbar.setAlignment(Pos.CENTER_RIGHT);

        Button b = new Button("X");
        b.setOnAction(new EventHandler() {

            @Override
            public void handle(Event event) {
                Platform.exit();
            }

        });
        b.getStyleClass().add("lg-close-button");
        toolbar.getChildren().add(b);

        bp.setCenter(fp);
        bp.setTop(toolbar);

        GridPane grid = new GridPane();
        //grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setId("maingrid");
        grid.setVgap(16);
        grid.setHgap(10);

        Text hwtext = new Text("Welcome!");
        hwtext.getStyleClass().add("lg-title");
        hwtext.setFont(titleFont);
        GridPane.setMargin(hwtext, new Insets(0, 0, 16, 0));
        GridPane.setHalignment(hwtext, HPos.CENTER);
        GridPane.setColumnSpan(hwtext, 2);
        grid.add(hwtext, 0, 0);

        Label plabel = new Label("Password:");
        plabel.getStyleClass().add("lg-label");
        GridPane.setHalignment(plabel, HPos.RIGHT);
        grid.add(plabel, 0, 1);
        PasswordField pfield = new PasswordField();
        pfield.setStyle("-fx-font-size: 16px");
        grid.add(pfield, 1, 1);

        grid.setVgap(5);

        Label pclabel = new Label("Confirm Password:");
        pclabel.getStyleClass().add("lg-label");
        GridPane.setHalignment(pclabel, HPos.RIGHT);
        grid.add(pclabel, 0, 2);
        PasswordField pcfield = new PasswordField();
        GridPane.setMargin(pcfield, new Insets(0, 0, 16, 0));
        GridPane.setMargin(pclabel, new Insets(0, 0, 16, 0));
        pcfield.setStyle("-fx-font-size: 16px");
        grid.add(pcfield, 1, 2);

        Label blabel = new Label("Backup Schedule:");
        blabel.getStyleClass().add("lg-label");
        GridPane.setHalignment(blabel, HPos.RIGHT);
        grid.add(blabel, 0, 3);
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList(
            "Every hour", "Every 12 hours", "Every day", "Every other day", "Every 5 days", "Every week", "Every month"
        ));
        cb.setStyle("-fx-font-size: 16px");
        cb.setMaxWidth(Double.MAX_VALUE);
        cb.setValue("Every day");
        GridPane.setMargin(cb, new Insets(0, 0, 16, 0));
        GridPane.setMargin(blabel, new Insets(0, 0, 16, 0));
        grid.add(cb, 1, 3);
        
        HBox bbox = new HBox();
        Button submit = new Button("Submit");
        submit.setStyle("-fx-font-size: 14px; -fx-cursor: hand;");
        bbox.setAlignment(Pos.CENTER_RIGHT);
        bbox.getChildren().add(submit);
        grid.add(bbox, 0, 4, 2, 1);

        fp.getChildren().add(grid);

        Canvas c = new Canvas(400, 500);
        GraphicsContext gc = c.getGraphicsContext2D();
        gc.save();
        gc.drawImage(new Image(CNotesApplication.class.getResource("/com/github/lg198/cnotes/res/gui/grey_wash_wall.png").toExternalForm()), 0, 0, c.getWidth(), c.getHeight());
        gc.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop[]{new Stop(0, Color.rgb(200, 200, 200, 0.5)), new Stop(1, Color.rgb(10, 10, 10, 0.5))}));
        gc.fillRect(0, 0, c.getWidth(), c.getHeight());
        gc.restore();

        sp.getChildren().addAll(c, bp);

        Scene scene = new Scene(sp, 400, 500);

        scene.getStylesheets().add(CNotesApplication.class.getResource("/com/github/lg198/cnotes/res/gui/setup_style.css").toExternalForm());

        stage.setScene(scene);
        
        if (!stage.isShowing()) {
            stage.show();
        }
    }
    
}
