package com.gongcraft.ui.panels;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.springframework.stereotype.Component;

@Component
public class DashboardPanel {

    private VBox root;

    public DashboardPanel() {
        // Defer JavaFX node creation until JavaFX toolkit is ready.
    }

    @jakarta.annotation.PostConstruct
    private void init() {
        // JavaFX Toolkit must be started from JavaFX Application Thread.
        // Since Spring can init beans before Application.launch, avoid creating
        // Controls here.
        root = new VBox();
        root.setPadding(new Insets(15));
    }

    public Parent getRoot() {
        return root;
    }
}
