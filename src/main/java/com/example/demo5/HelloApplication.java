package com.example.demo5;

import com.example.demo5.View.*;
import com.example.demo5.Controller.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        TrafficController controller = new TrafficController();
        TrafficView view = new TrafficView(controller);
        stage.setScene(view.createCombinedScene());
        stage.setTitle("Traffic Simulation");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}