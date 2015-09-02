package com.github.lg198.cnotes.jfx;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import javafx.util.Duration;

public class Notification {

    private Label label;
    private FadeTransition fadeIn, fadeOut;
    private volatile Timeline existTimeline;
    private volatile boolean hiding = false;

    public Notification(String s) {
        label = new Label(s);
        label.setFont(Font.font(15f));
    }

    public void setGraphic(Node n) {
        label.setGraphic(n);
        label.setContentDisplay(ContentDisplay.LEFT);
        n.prefHeight(label.getHeight());
    }

    private StackPane build() {
        final StackPane bp = new StackPane();
        label.setTextFill(Color.WHITE);
        bp.getChildren().add(label);

        bp.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE.darker().darker(), new CornerRadii(3), null)));
        bp.setPadding(new Insets(10));
        bp.setBorder(new Border(new BorderStroke(Color.BLACK.brighter().brighter().brighter(),
                                                 BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(3), new Insets(-5))));

        bp.setOnMouseClicked((event -> {
            if (hiding) {
                return;
            }
            fadeOut.setDuration(Duration.seconds(0.8));
            hide();
        }));
        return bp;
    }

    private Circle buildCircle() {
        Circle c = new Circle(15, Color.GRAY);
        return c;
    }

    private void hide() {
        hiding = true;
        existTimeline.stop();
        fadeOut.play();
    }

    public void show(Window w) {
        Popup npop = new Popup();
        npop.getContent().add(build());
        npop.setAnchorLocation(PopupWindow.AnchorLocation.WINDOW_BOTTOM_RIGHT);

        fadeIn = new FadeTransition();
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setDuration(Duration.seconds(1));
        fadeIn.setNode(npop.getContent().get(0));

        fadeOut = new FadeTransition();
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDuration(Duration.seconds(2));
        fadeOut.setNode(npop.getContent().get(0));

        existTimeline = new Timeline(new KeyFrame(Duration.seconds(4), evt -> hide()));

        fadeIn.setOnFinished(event -> existTimeline.playFromStart());
        fadeOut.setOnFinished(event -> npop.hide());

        w.setOnHidden(event -> hide());

        npop.show(w, w.getX() + w.getWidth() - 10, w.getY() + w.getHeight() - 10);

        ChangeListener<Number> cl = (observable, oldValue, newValue) -> {
            npop.setAnchorX(w.getX() + w.getWidth() - 10);
            npop.setAnchorY(w.getY() + w.getHeight() - 10);
        };
        w.widthProperty().addListener(cl);
        w.heightProperty().addListener(cl);
        w.xProperty().addListener(cl);
        w.yProperty().addListener(cl);

        fadeIn.play();
    }
}
