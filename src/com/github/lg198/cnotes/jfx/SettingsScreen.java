package com.github.lg198.cnotes.jfx;

import com.github.lg198.cnotes.jfx.settings.CustomFieldPanel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;


public class SettingsScreen {

    private OverlayStackPane overlay;

    private TabPane tabs = new TabPane();

    private CustomFieldPanel customFieldPanel = new CustomFieldPanel();

    public SettingsScreen(OverlayStackPane osp) {
        overlay = osp;
    }

    public VBox build() {
        VBox box = new VBox();
        box.setAlignment(Pos.TOP_CENTER);
        box.setPadding(new Insets(10));

        box.getChildren().add(tabs);
        VBox.setVgrow(tabs, Priority.ALWAYS);

        tabs.getTabs().addAll(buildCustomFieldTab());

        box.getChildren().add(buildBottomBar());

        box.setPrefWidth(500);

        return box;
    }

    private HBox buildBottomBar() {
        HBox bar = new HBox();
        bar.setAlignment(Pos.CENTER_RIGHT);
        bar.getStyleClass().add("lg-tool-box");

        LabelButton exit = new LabelButton("Exit");
        exit.setOnAction(e -> overlay.hideOverlayFade(Duration.seconds(0.5)));
        exit.setId("s-exit-button");
        exit.getStyleClass().add("lg-tool-box-button");

        bar.getChildren().add(exit);

        return bar;
    }

    private Tab buildCustomFieldTab() {
        Tab t = new Tab();
        t.setText("Custom Fields");
        t.setContent(customFieldPanel.build());
        t.setClosable(false);
        return t;
    }


}
