package com.gongcraft.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class ReportPanel {

    private final VBox root = new VBox(10);

    public ReportPanel() {
        root.setPadding(new Insets(15));
        root.getChildren().add(new Label("Reports (placeholder)"));
    }

    public Parent getRoot() {
        return root;
    }
}

