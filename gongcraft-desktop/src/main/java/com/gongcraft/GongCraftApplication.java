package com.gongcraft;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GongCraftApplication extends Application {

    private static ConfigurableApplicationContext springContext;
    private static Stage primaryStage;

    private final com.gongcraft.ui.MainWindow mainWindow;
    private final com.gongcraft.ui.panels.DashboardPanel dashboardPanel;
    private final com.gongcraft.ui.panels.CustomerPanel customerPanel;
    private final com.gongcraft.ui.panels.ProductPanel productPanel;
    private final com.gongcraft.ui.panels.OrderPanel orderPanel;
    private final com.gongcraft.ui.panels.ProductionPanel productionPanel;
    private final com.gongcraft.ui.panels.PaymentPanel paymentPanel;
    private final com.gongcraft.ui.panels.ReportPanel reportPanel;
    private final com.gongcraft.ui.panels.SettingsPanel settingsPanel;

    public GongCraftApplication() {
        // JavaFX requires a no-arg constructor.
        this.mainWindow = null;
        this.dashboardPanel = null;
        this.customerPanel = null;
        this.productPanel = null;
        this.orderPanel = null;
        this.productionPanel = null;
        this.paymentPanel = null;
        this.reportPanel = null;
        this.settingsPanel = null;
    }

    // Called from Spring after context is started (optional).
    public GongCraftApplication(
            com.gongcraft.ui.MainWindow mainWindow,
            com.gongcraft.ui.panels.DashboardPanel dashboardPanel,
            com.gongcraft.ui.panels.CustomerPanel customerPanel,
            com.gongcraft.ui.panels.ProductPanel productPanel,
            com.gongcraft.ui.panels.OrderPanel orderPanel,
            com.gongcraft.ui.panels.ProductionPanel productionPanel,
            com.gongcraft.ui.panels.PaymentPanel paymentPanel,
            com.gongcraft.ui.panels.ReportPanel reportPanel,
            com.gongcraft.ui.panels.SettingsPanel settingsPanel) {
        this.mainWindow = mainWindow;
        this.dashboardPanel = dashboardPanel;
        this.customerPanel = customerPanel;
        this.productPanel = productPanel;
        this.orderPanel = orderPanel;
        this.productionPanel = productionPanel;
        this.paymentPanel = paymentPanel;
        this.reportPanel = reportPanel;
        this.settingsPanel = settingsPanel;
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("GongCraft - Sistem Pemesanan & Monitoring Produksi Gamelan");
        stage.setWidth(1400);
        stage.setHeight(800);

        // Build JavaFX UI on JavaFX Application Thread.
        // If JavaFX used the no-arg constructor, fields are null; fetch beans from Spring context.
        var mw = (this.mainWindow != null)
                ? this.mainWindow
                : springContext.getBean(com.gongcraft.ui.MainWindow.class);

        var dp = (this.dashboardPanel != null)
                ? this.dashboardPanel
                : springContext.getBean(com.gongcraft.ui.panels.DashboardPanel.class);

        var cp = (this.customerPanel != null)
                ? this.customerPanel
                : springContext.getBean(com.gongcraft.ui.panels.CustomerPanel.class);

        var pp = (this.productPanel != null)
                ? this.productPanel
                : springContext.getBean(com.gongcraft.ui.panels.ProductPanel.class);

        var op = (this.orderPanel != null)
                ? this.orderPanel
                : springContext.getBean(com.gongcraft.ui.panels.OrderPanel.class);

        var prp = (this.productionPanel != null)
                ? this.productionPanel
                : springContext.getBean(com.gongcraft.ui.panels.ProductionPanel.class);

        var payp = (this.paymentPanel != null)
                ? this.paymentPanel
                : springContext.getBean(com.gongcraft.ui.panels.PaymentPanel.class);

        var rp = (this.reportPanel != null)
                ? this.reportPanel
                : springContext.getBean(com.gongcraft.ui.panels.ReportPanel.class);

        var sp = (this.settingsPanel != null)
                ? this.settingsPanel
                : springContext.getBean(com.gongcraft.ui.panels.SettingsPanel.class);

        mw.buildUI(dp, cp, pp, op, prp, payp, rp, sp);

        Scene scene = new Scene(mw.getRoot());

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        if (springContext != null) {
            springContext.close();
        }
    }

    public static void main(String[] args) {
        // Start Spring first to create beans; JavaFX UI will be built in start(...).
        springContext = SpringApplication.run(GongCraftApplication.class, args);
        Application.launch(args);
    }

    public static ConfigurableApplicationContext getSpringContext() {
        return springContext;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
