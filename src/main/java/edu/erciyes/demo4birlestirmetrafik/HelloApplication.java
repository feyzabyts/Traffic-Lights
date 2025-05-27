package edu.erciyes.demo4birlestirmetrafik;

import edu.erciyes.demo4birlestirmetrafik.Controller.TrafficController;
import edu.erciyes.demo4birlestirmetrafik.View.TrafficView;
import javafx.application.Application;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) {
        TrafficController controller = new TrafficController();
        TrafficView view = new TrafficView(controller);
        stage.setScene(view.createScene());
        stage.setTitle("Traffic Simulation");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}