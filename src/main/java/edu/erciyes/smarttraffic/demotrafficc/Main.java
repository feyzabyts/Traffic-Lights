package edu.erciyes.smarttraffic.demotrafficc;

import edu.erciyes.smarttraffic.demotrafficc.Model.Vehicle;
import edu.erciyes.smarttraffic.demotrafficc.Model.VehicleType;
import edu.erciyes.smarttraffic.demotrafficc.View.BusView;
import edu.erciyes.smarttraffic.demotrafficc.View.TaxiView;
import edu.erciyes.smarttraffic.demotrafficc.View.TruckView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends Application {
    enum Direction { DOWN, RIGHT, LEFT }
    Direction direction = Direction.DOWN;

    private final double paneWidth = 800;
    private final double paneHeight = 800;
    private final double centerX = paneWidth / 2;
    private final double centerY = paneHeight / 2;

    private final List<Vehicle> vehicles = new ArrayList<>();
    private int vehicleSpawnTimer = 0;

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-color: #999999;");

        // Yol çizimleri
        Line verticalRoad = new Line(centerX, centerY - 200, centerX, centerY + 200);
        verticalRoad.setStroke(Color.PINK);
        verticalRoad.setStrokeWidth(100);

        Line horizontalRoad = new Line(centerX - 200, centerY, centerX + 200, centerY);
        horizontalRoad.setStroke(Color.PINK);
        horizontalRoad.setStrokeWidth(100);

        // Oklar
        Polygon topArrow = createArrow(centerX - 23, centerY - 150, 0);
        Polygon top1Arrow = createArrow(centerX - 23, centerY + 150, 0);
        Polygon rightArrow = createArrow(centerX + 150, centerY + 23, 90);
        Polygon right1Arrow = createArrow(centerX + 150, centerY - 23, -90);
        Polygon bottomArrow = createArrow(centerX + 23, centerY + 150, 180);
        Polygon bottom1Arrow = createArrow(centerX + 23, centerY - 150, 180);
        Polygon leftArrow = createArrow(centerX - 150, centerY - 23, -90);
        Polygon left1Arrow = createArrow(centerX - 150, centerY + 23, 90);

        pane.getChildren().addAll(
                verticalRoad, horizontalRoad,
                topArrow, rightArrow, bottomArrow, leftArrow,
                top1Arrow, bottom1Arrow, right1Arrow, left1Arrow
        );

        Timeline animation = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            vehicleSpawnTimer++;
            if (vehicleSpawnTimer >= 100) {
                vehicleSpawnTimer = 0;

                int startX = (int) centerX - 10;
                int startY = 50;

                Vehicle newVehicle;
                Node sprite;
                VehicleType type;

                double randomType = Math.random();
                if (randomType < 0.33) {
                    sprite = new TaxiView(startX, startY);
                    type = VehicleType.TAXI;
                } else if (randomType < 0.66) {
                    sprite = new TruckView(startX, startY);
                    type = VehicleType.TRUCK;
                } else {
                    sprite = new BusView(startX, startY);
                    type = VehicleType.BUS;
                }

                pane.getChildren().add(sprite);
                newVehicle = new Vehicle(startX, startY, type, sprite);
                vehicles.add(newVehicle);
            }

            Iterator<Vehicle> iterator = vehicles.iterator();
            while (iterator.hasNext()) {
                Vehicle v = iterator.next();
                v.update();

                // Pembe yol bittiğinde araç silinsin
                boolean isOutOfRoad = false;
                switch (direction) {
                    case DOWN:
                        if (v.getY() > centerY + 200) isOutOfRoad = true;
                        break;
                    case RIGHT:
                        if (v.getX() > centerX + 200) isOutOfRoad = true;
                        break;
                    case LEFT:
                        if (v.getX() < centerX - 200) isOutOfRoad = true;
                        break;
                }

                if (isOutOfRoad) {
                    pane.getChildren().remove(v.getSprite());
                    iterator.remove();
                }
            }
        }));

        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();

        Scene scene = new Scene(pane, paneWidth, paneHeight);
        stage.setTitle("Dört Yol Kavşağı - Çoklu Araç");
        stage.setScene(scene);
        stage.show();
    }

    private Polygon createArrow(double x, double y, double angle) {
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                0.0, -10.0,
                -10.0, 10.0,
                10.0, 10.0
        );
        arrow.setFill(Color.WHITE);
        arrow.setLayoutX(x);
        arrow.setLayoutY(y);
        arrow.setRotate(angle);
        return arrow;
    }

    public static void main(String[] args) {
        launch(args);
    }
}