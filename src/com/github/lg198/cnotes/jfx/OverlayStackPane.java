package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.util.ResourceManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.controlsfx.glyphfont.FontAwesome;

import java.util.ListIterator;

public class OverlayStackPane extends StackPane {

    private static final double RECT_OPACITY = 0.85;

    private boolean showingOverlay = false;
    private GridPane overlayNode = null;
    private Rectangle overlayRect = null;
    private Duration duration = Duration.seconds(0.5);

    public void showOverlay(Parent n) {
        showingOverlay = true;
        overlayNode = new GridPane();
        overlayNode.setAlignment(Pos.CENTER);
        overlayNode.getProperties().put("lg-overlay", "");

        //HBox toolbar = createToolbar();
       // overlayNode.add(toolbar, 1, 0);
        overlayNode.getChildren().add(n);

        /*overlayNode.add(createVerticalSide(), 0, 0, 1, 2);
        overlayNode.add(createVerticalSide(), 2, 0, 1, 2);
        overlayNode.add(createHorizontalSide(), 0, 2, 3, 1);*/

        overlayRect = new Rectangle();
        overlayRect.getProperties().put("lg-overlay", "");
        overlayRect.widthProperty().bind(this.widthProperty());
        overlayRect.heightProperty().bind(this.heightProperty());
        overlayRect.setFill(Color.BLACK.brighter().brighter());
        overlayRect.setOpacity(RECT_OPACITY);

        getChildren().addAll(overlayRect, overlayNode);

        overlayNode.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.doubleValue() > overlayNode.getScene().getHeight()) {
                Node root = overlayNode.getChildren().remove(0);
                overlayNode.getChildren().add(new ScrollPane(root));
            }
        });

        overlayNode.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ESCAPE)
                this.hideOverlayFade(duration);
        });

        overlayNode.requestFocus();
    }

    private VBox createVerticalSide() {
        VBox sideBox = new VBox();
        sideBox.setMinWidth(6);
        sideBox.setMaxWidth(6);
        sideBox.getStyleClass().add("lg-overlay-sides");
        return sideBox;
    }

    private HBox createHorizontalSide() {
        HBox sideBox = new HBox();
        sideBox.setMaxHeight(6);
        sideBox.setMinHeight(6);
        sideBox.getStyleClass().add("lg-overlay-sides");
        return sideBox;
    }

    private HBox createToolbar() {
        HBox toolbar = new HBox();
        toolbar.getStyleClass().addAll("lg-menu-box");
        Label icon = ResourceManager.generateGlyph(FontAwesome.Glyph.CLOSE.getChar(), 10, Color.BLACK);
        Button closeButton = new Button(null, icon);
        closeButton.getStyleClass().add("close-button");
        toolbar.getChildren().add(closeButton);
        HBox.setHgrow(closeButton, Priority.NEVER);

        Rectangle rect = new Rectangle();
        toolbar.getChildren().add(rect);
        HBox.setHgrow(rect, Priority.ALWAYS);

        return toolbar;
    }

    public void showOverlayFade(Parent n, Duration time) {
        duration = time;
        showOverlay(n);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(overlayNode.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0), new KeyValue(overlayRect.opacityProperty(), 0)),
                new KeyFrame(time, new KeyValue(overlayNode.opacityProperty(), overlayNode.getOpacity())),
                new KeyFrame(time, new KeyValue(overlayRect.opacityProperty(), RECT_OPACITY))
        );
        timeline.playFromStart();
    }

    public boolean isShowingOverlay() {
        return showingOverlay;
    }

    public void hideOverlay() {
        showingOverlay = false;
        ListIterator<Node> li = getChildren().listIterator();
        while (li.hasNext()) {
            Node n = li.next();
            if (n.getProperties().containsKey("lg-overlay")) {
                n.getProperties().remove("lg-overlay");
                li.remove();
            }
        }
    }

    public void hideOverlayFade(Duration time) {
        duration = time;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(overlayNode.opacityProperty(), overlayNode.getOpacity())),
                new KeyFrame(Duration.seconds(0), new KeyValue(overlayRect.opacityProperty(), overlayRect.getOpacity())),
                new KeyFrame(time, new KeyValue(overlayNode.opacityProperty(), 0)),
                new KeyFrame(time, new KeyValue(overlayRect.opacityProperty(), 0))
        );
        timeline.setOnFinished(event -> hideOverlay());
        timeline.playFromStart();
    }
}
