package com.example.demo155;

import com.example.demo155.controller.TrafficController;
import com.example.demo155.view.TrafficView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        TrafficController controller = new TrafficController();
        TrafficView view = new TrafficView(controller);
        Scene scene = view.createCombinedScene();

        primaryStage.setTitle("Traffic Simulation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}