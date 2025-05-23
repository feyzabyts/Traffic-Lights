package com.example.demo122.Controller;

import com.example.demo122.Model.CarModel;
import com.example.demo122.viEW.IntersectionView;
import javafx.animation.AnimationTimer;

public class IntersectionController {
    private CarModel model;
    private IntersectionView view;
    private AnimationTimer timer;

    public IntersectionController(CarModel model, IntersectionView view) {
        this.model = model;
        this.view = view;

        // Başlat düğmesine olay ekle
        view.getStartButton().setOnAction(event -> startSimulation());
    }

    private void startSimulation() {
        if (timer != null) {
            timer.stop();
        }
        timer = new AnimationTimer() {
            private long lastCarAdded = 0;
            private final long carAddInterval = 2_000_000_000L; // 2 saniye

            @Override
            public void handle(long now) {
                // Her 2 saniyede bir araba ekle
                if (now - lastCarAdded > carAddInterval) {
                    model.addCar();
                    lastCarAdded = now;
                }
                // Arabaları hareket ettir ve View'ı güncelle
                model.updateCars();
                view.drawCars(model);
            }
        };
        timer.start();
    }
}