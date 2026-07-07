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

    public GongCraftApplication(com.gongcraft.ui.MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("GongCraft - Sistem Pemesanan & Monitoring Produksi Gamelan");
        stage.setWidth(1400);
        stage.setHeight(800);

        Scene scene = new Scene(mainWindow.getRoot());
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

