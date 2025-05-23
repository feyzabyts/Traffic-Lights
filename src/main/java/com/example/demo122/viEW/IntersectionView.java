package com.example.demo122.viEW;

import com.example.demo122.Model.CarModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class IntersectionView {
    private VBox view;
    private Canvas canvas;
    private Button startButton;

    public IntersectionView() {
        view = new VBox(10);
        canvas = new Canvas(400, 400);
        startButton = new Button("Başlat");

        // Kavşağı çiz
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawIntersection(gc);

        view.getChildren().addAll(canvas, startButton);
    }

    public VBox getView() {
        return view;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Button getStartButton() {
        return startButton;
    }

    public void drawIntersection(GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(0, 0, 400, 400); // Arka plan
        gc.setFill(Color.BLACK);
        gc.fillRect(150, 0, 100, 150); // Kuzey yolu
        gc.fillRect(150, 250, 100, 150); // Güney yolu
        gc.fillRect(0, 150, 150, 100); // Batı yolu
        gc.fillRect(250, 150, 150, 100); // Doğu yolu
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(150, 150, 100, 100); // Kavşak merkezi
    }

    public void drawCars(CarModel model) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        // Önce kavşağı yeniden çiz
        drawIntersection(gc);
        // Arabaları çiz
        gc.setFill(Color.RED);
        for (CarModel.Car car : model.getCars()) {
            gc.fillOval(car.getX() - 10, car.getY() - 10, 20, 20);
        }
    }
}