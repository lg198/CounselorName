package com.github.lg198.cnotes.jfx;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.web.HTMLEditor;

public class EditorRegion extends Region {

    private static final Color STROKE = Color.BLACK;
    private HTMLEditor editor;
    private Group resize;

    public EditorRegion() {
        editor = new HTMLEditor();
        resize = new Group();
        drawResize();
        getChildren().addAll(editor);

        resize.setFocusTraversable(false);
        editor.requestFocus();

        this.widthProperty().addListener((observable, oldValue, newValue) -> layoutChildren());
    }

    private void drawResize() {
        Line l1 = new Line();
        l1.setStroke(STROKE);
        l1.setStrokeWidth(1.5);
        Line l2 = new Line();
        l2.setStroke(STROKE);
        l2.setStrokeWidth(1.5);
        Line l3 = new Line();
        l3.setStroke(STROKE);
        l3.setStrokeWidth(1.5);

        final double L1WIDTH = 10;
        final double L2WIDTH = 5.5;
        final double L3WIDTH = 2;

        final double L1START = 0;
        final double L2START = (L1WIDTH - L2WIDTH) / 2;
        final double L3START = (L1WIDTH - L3WIDTH) / 2;

        l1.setStartX(0);
        l1.setEndX(L1WIDTH);
        l2.setStartX(L2START);
        l2.setEndX(L1WIDTH - L2START);
        l3.setStartX(L3START);
        l3.setEndX(L1WIDTH - L3START);

        l1.setStartY(0);
        l1.setEndY(0);
        l2.setStartY(3.5);
        l2.setEndY(3.5);
        l3.setStartY(7);
        l3.setEndY(7);

        resize.getChildren().addAll(l1, l2, l3);

        resize.getTransforms().add(new Rotate(-45, 5, 3.5));
    }

    @Override
    protected void layoutChildren() {
        layoutInArea(
                editor,
                getPadding().getLeft(),
                getPadding().getTop(),
                getWidth() - getPadding().getRight(),
                getHeight() - getPadding().getBottom(),
                0,
                HPos.CENTER,
                VPos.TOP);

        layoutInArea(
                resize,
                getWidth() - getPadding().getRight() - resize.prefWidth(-1),
                getHeight() - getPadding().getBottom() - resize.prefHeight(-1),
                resize.prefWidth(-1),
                resize.prefHeight(-1),
                0,
                HPos.CENTER,
                VPos.CENTER);
    }
}
