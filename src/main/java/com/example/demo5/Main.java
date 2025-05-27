package com.example.demo5;

import com.example.demo5.Controller.TrafficController;
import com.example.demo5.View.TrafficView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        TrafficController controller = new TrafficController();
        TrafficView view = new TrafficView(controller);
        Scene scene = view.createCombinedScene();
        primaryStage.setScene(scene);
        primaryStage.setTitle("Traffic Simulation");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}