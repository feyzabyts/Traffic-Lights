package com.example.demo122;

import com.example.demo122.Controller.IntersectionController;
import com.example.demo122.Model.CarModel;
import com.example.demo122.viEW.IntersectionView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        CarModel model = new CarModel();
        IntersectionView view = new IntersectionView();
        IntersectionController controller = new IntersectionController(model, view);

        Scene scene = new Scene(view.getView(), 400, 450);
        primaryStage.setTitle("Kavşak Simülasyonu");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}