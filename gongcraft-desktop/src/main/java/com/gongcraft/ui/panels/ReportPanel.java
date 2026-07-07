package com.gongcraft.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class ReportPanel {

    private VBox root;

    public ReportPanel() {
        // Defer JavaFX node creation until JavaFX toolkit is ready.
    }

    @jakarta.annotation.PostConstruct
    private void init() {
        root = new VBox();
        root.setPadding(new Insets(15));
    }

    public Parent getRoot() {
        return root;
    }
}
