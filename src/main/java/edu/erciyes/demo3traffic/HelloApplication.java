package edu.erciyes.demo3traffic;

import edu.erciyes.demo3traffic.Controller.TrafficController;
import edu.erciyes.demo3traffic.View.TrafficView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {
    @Override
    public void start(Stage primaryStage) {
        TrafficController controller = new TrafficController();
        TrafficView trafficView = new TrafficView(controller);

        Scene mainScene = trafficView.createCombinedScene();

        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Traffic Simulation");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}