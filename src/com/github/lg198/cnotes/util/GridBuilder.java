package com.github.lg198.cnotes.util;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class GridBuilder {

    private GridPane grid = new GridPane();
    private int row = 0, col = 0;

    public GridPane build() {
        return grid;
    }

    public void hgap(double gap) {
        grid.setHgap(gap);
    }

    public void vgap(double gap) {
        grid.setVgap(gap);
    }

    public void row() {
        row++;
        col = 0;
    }

    public GridPaneCellBuilder add(Node n) {
        grid.add(n, col++, row);
        return new GridPaneCellBuilder(n);
    }

    public ColumnConstraintsBuilder constraintsBuilder() {
        return new ColumnConstraintsBuilder();
    }

    public static class ColumnConstraintsBuilder {
        private int col = 0;
        private ColumnConstraints constraints;

        public ColumnConstraintsBuilder() {
            constraints = new ColumnConstraints();
        }

        public ColumnConstraintsBuilder percent(int perc) {
            constraints.setPercentWidth(perc);
            return this;
        }

        public ColumnConstraintsBuilder fillWidth() {
            constraints.setFillWidth(true);
            return this;
        }

        public ColumnConstraintsBuilder prefWidth(double w) {
            constraints.setPrefWidth(w);
            return this;
        }

        public ColumnConstraintsBuilder minWidth(double w) {
            constraints.setMinWidth(w);
            return this;
        }

        public ColumnConstraintsBuilder maxWidth(double w) {
            constraints.setMaxWidth(w);
            return this;
        }

        public ColumnConstraintsBuilder col() {
            col++;
            constraints = new ColumnConstraints();
            return this;
        }
    }

    public static class GridPaneCellBuilder {

        private Node node;
        private double itop = 0, iright = 0, ibottom = 0, ileft = 0;

        private GridPaneCellBuilder(Node n) {
            node = n;
        }

        public GridPaneCellBuilder halign(HPos pos) {
            GridPane.setHalignment(node, pos);
            return this;
        }

        public GridPaneCellBuilder valign(VPos pos) {
            GridPane.setValignment(node, pos);
            return this;
        }

        public GridPaneCellBuilder hgrow(Priority p) {
            GridPane.setHgrow(node, p);
            return this;
        }

        public GridPaneCellBuilder vgrow(Priority p) {
            GridPane.setVgrow(node, p);
            return this;
        }

        public GridPaneCellBuilder fillHeight() {
            GridPane.setFillHeight(node, true);
            return this;
        }

        public GridPaneCellBuilder fillWidth() {
            GridPane.setFillWidth(node, true);
            return this;
        }

        public GridPaneCellBuilder colSpan(int span) {
            GridPane.setColumnSpan(node, span);
            return this;
        }

        public GridPaneCellBuilder rowSpan(int span) {
            GridPane.setColumnSpan(node, span);
            return this;
        }

        public GridPaneCellBuilder margin(Insets insets) {
            GridPane.setMargin(node, insets);
            setInsets(insets);
            return this;
        }

        private void setInsets(Insets insets) {
            itop = insets.getTop();
            iright = insets.getRight();
            ibottom = insets.getBottom();
            ileft = insets.getLeft();
        }

        public GridPaneCellBuilder marginTop(double i) {
            Insets insets = new Insets(i, iright, ibottom, ileft);
            GridPane.setMargin(node, insets);
            setInsets(insets);
            return this;
        }

        public GridPaneCellBuilder marginRight(double i) {
            Insets insets = new Insets(itop, i, ibottom, ileft);
            GridPane.setMargin(node, insets);
            setInsets(insets);
            return this;
        }

        public GridPaneCellBuilder marginBottom(double i) {
            Insets insets = new Insets(itop, iright, i, ileft);
            GridPane.setMargin(node, insets);
            setInsets(insets);
            return this;
        }

        public GridPaneCellBuilder marginLeft(double i) {
            Insets insets = new Insets(itop, iright, ibottom, i);
            GridPane.setMargin(node, insets);
            setInsets(insets);
            return this;
        }

    }
}
