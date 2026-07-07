package com.gongcraft.ui;

import com.gongcraft.ui.panels.DashboardPanel;
import com.gongcraft.ui.panels.CustomerPanel;
import com.gongcraft.ui.panels.OrderPanel;
import com.gongcraft.ui.panels.PaymentPanel;
import com.gongcraft.ui.panels.ProductionPanel;
import com.gongcraft.ui.panels.ProductPanel;
import com.gongcraft.ui.panels.ReportPanel;
import com.gongcraft.ui.panels.SettingsPanel;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.springframework.stereotype.Component;

@Component
public class MainWindow {

    private final BorderPane root = new BorderPane();

    public MainWindow(
            DashboardPanel dashboardPanel,
            CustomerPanel customerPanel,
            ProductPanel productPanel,
            OrderPanel orderPanel,
            ProductionPanel productionPanel,
            PaymentPanel paymentPanel,
            ReportPanel reportPanel,
            SettingsPanel settingsPanel
    ) {
        initializeUI(dashboardPanel, customerPanel, productPanel, orderPanel, productionPanel, paymentPanel, reportPanel, settingsPanel);
    }

    private void initializeUI(
            DashboardPanel dashboardPanel,
            CustomerPanel customerPanel,
            ProductPanel productPanel,
            OrderPanel orderPanel,
            ProductionPanel productionPanel,
            PaymentPanel paymentPanel,
            ReportPanel reportPanel,
            SettingsPanel settingsPanel
    ) {
        root.setStyle("-fx-background-color: #f0f0f0;");

        root.setTop(createMenuBar());

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab dashboardTab = new Tab("Dashboard", dashboardPanel.getRoot());
        Tab customersTab = new Tab("Customers", customerPanel.getRoot());
        Tab productsTab = new Tab("Products", productPanel.getRoot());
        Tab ordersTab = new Tab("Orders", orderPanel.getRoot());
        Tab productionTab = new Tab("Production", productionPanel.getRoot());
        Tab paymentsTab = new Tab("Payments", paymentPanel.getRoot());
        Tab reportsTab = new Tab("Reports", reportPanel.getRoot());
        Tab settingsTab = new Tab("Settings", settingsPanel.getRoot());

        tabs.getTabs().addAll(dashboardTab, customersTab, productsTab, ordersTab, productionTab, paymentsTab, reportsTab, settingsTab);
        root.setCenter(tabs);

        root.setBottom(createStatusBar());
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> System.out.println("GongCraft Desktop"));
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(5));
        statusBar.setStyle("-fx-border-color: #ccc; -fx-background-color: #ffffff;");

        Label statusLabel = new Label("Ready");
        Label userLabel = new Label("User: Admin");

        HBox.setMargin(userLabel, new Insets(0, 0, 0, 10));
        statusBar.getChildren().addAll(statusLabel, userLabel);
        return statusBar;
    }

    public Parent getRoot() {
        return root;
    }
}

