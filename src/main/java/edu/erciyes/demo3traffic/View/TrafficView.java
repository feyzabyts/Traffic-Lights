package edu.erciyes.demo3traffic.View;

import edu.erciyes.demo3traffic.Controller.TrafficController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TrafficView {
    private TrafficController controller;
    private Label vehicleLabel;
    private Label northVehicleLabel;
    private Label westVehicleLabel;
    private Label southVehicleLabel;
    private Label eastVehicleLabel;
    private VBox northLight;
    private VBox westLight;
    private VBox southLight;
    private VBox eastLight;
    private Button assignVehiclesBtn;
    private Button startBtn;
    private Button stopBtn;
    private Button resetBtn;

    public TrafficView(TrafficController controller) {
        this.controller = controller;
    }

    public Scene createCombinedScene() {
        HBox mainLayout = new HBox(20);
        mainLayout.setAlignment(Pos.CENTER);

        Pane roadPane = createRoadPane();
        GridPane controlPane = createControlPane();

        roadPane.setPrefWidth(600);
        controlPane.setPrefWidth(300);

        mainLayout.getChildren().addAll(roadPane, controlPane);

        return new Scene(mainLayout, 900, 600);
    }

    private Pane createRoadPane() {
        Pane roadPane = new Pane();
        double width = 600;
        double height = 600;
        double laneWidth = 50;
        double roadWidth = laneWidth*2;

        Rectangle westRoadIn = new Rectangle(0, height/2 - roadWidth, width/2 - roadWidth/2, laneWidth);
        westRoadIn.setFill(Color.GRAY);
        Rectangle westRoadOut = new Rectangle(0, height/2, width/2 - roadWidth/2, laneWidth);
        westRoadOut.setFill(Color.GRAY);
        Rectangle eastRoadIn = new Rectangle(width/2 + roadWidth/2, height/2 - roadWidth, width/2 - roadWidth/2, laneWidth);
        eastRoadIn.setFill(Color.GRAY);
        Rectangle eastRoadOut = new Rectangle(width/2 + roadWidth/2, height/2, width/2 - roadWidth/2, laneWidth);
        eastRoadOut.setFill(Color.GRAY);

        Rectangle northRoadIn = new Rectangle(width/2 - roadWidth, 0, laneWidth, height/2 - roadWidth/2);
        northRoadIn.setFill(Color.GRAY);
        Rectangle northRoadOut = new Rectangle(width/2, 0, laneWidth, height/2 - roadWidth/2);
        northRoadOut.setFill(Color.GRAY);
        Rectangle southRoadIn = new Rectangle(width/2 - roadWidth, height/2 + roadWidth/2, laneWidth, height/2 - roadWidth/2);
        southRoadIn.setFill(Color.GRAY);
        Rectangle southRoadOut = new Rectangle(width/2, height/2 + roadWidth/2, laneWidth, height/2 - roadWidth/2);
        southRoadOut.setFill(Color.GRAY);

        Rectangle intersection = new Rectangle(width/2 - roadWidth/2, height/2 - roadWidth/2, roadWidth, roadWidth);
        intersection.setFill(Color.DARKGRAY);

        roadPane.getChildren().addAll(westRoadIn, westRoadOut, eastRoadIn, eastRoadOut,
                northRoadIn, northRoadOut, southRoadIn, southRoadOut, intersection);

        northLight = createTrafficLight("North");
        westLight = createTrafficLight("West");
        southLight = createTrafficLight("South");
        eastLight = createTrafficLight("East");

        double northLightX = width/2 - roadWidth/2 - 20;
        double northLightY = height/2 - roadWidth/2 - 50;
        double westLightX = width/2 - roadWidth/2 - 50;
        double westLightY = height/2 + roadWidth/2 + 20;
        double southLightX = width/2 + roadWidth/2 + 20;
        double southLightY = height/2 + roadWidth/2 + 20;
        double eastLightX = width/2 + roadWidth/2 + 20;
        double eastLightY = height/2 - roadWidth/2 - 50;

        northLight.setLayoutX(northLightX);
        northLight.setLayoutY(northLightY);
        westLight.setLayoutX(westLightX);
        westLight.setLayoutY(westLightY);
        southLight.setLayoutX(southLightX);
        southLight.setLayoutY(southLightY);
        eastLight.setLayoutX(eastLightX);
        eastLight.setLayoutY(eastLightY);

        Label[] coordLabels = new Label[] {
                new Label("<K1,T1,D1,D2,B1>"), new Label("<B4,D4,D3,T3,K3>"),
                new Label("<B2,D1,D4,T4,K4>"), new Label("<K2,T2,D2,D3,B3>")
        };

        roadPane.getChildren().addAll(northLight, westLight, southLight, eastLight,
                coordLabels[0], coordLabels[1], coordLabels[2], coordLabels[3]);

        System.out.println("Traffic Light Coordinates:");
        System.out.println("North Traffic Light: (" + northLightX + ", " + northLightY + ")");
        System.out.println("West Traffic Light: (" + westLightX + ", " + westLightY + ")");
        System.out.println("South Traffic Light: (" + southLightX + ", " + southLightY + ")");
        System.out.println("East Traffic Light: (" + eastLightX + ", " + eastLightY + ")");

        return roadPane;
    }

    private GridPane createControlPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        vehicleLabel = new Label("Vehicle Count: " + controller.getTotalVehicleCount());
        northVehicleLabel = new Label("North: " + controller.getVehicleDistribution().getOrDefault("North", 0) + " vehicles");
        westVehicleLabel = new Label("West: " + controller.getVehicleDistribution().getOrDefault("West", 0) + " vehicles");
        southVehicleLabel = new Label("South: " + controller.getVehicleDistribution().getOrDefault("South", 0) + " vehicles");
        eastVehicleLabel = new Label("East: " + controller.getVehicleDistribution().getOrDefault("East", 0) + " vehicles");

        startBtn = new Button("Start");
        stopBtn = new Button("Stop");
        resetBtn = new Button("Reset");
        assignVehiclesBtn = new Button("Assign Vehicles");

        // Set buttons to controller
        controller.setButtons(startBtn, assignVehiclesBtn, stopBtn, resetBtn);

        Button northInc = new Button("↑");
        Button northDec = new Button("↓");
        Button westInc = new Button("↑");
        Button westDec = new Button("↓");
        Button southInc = new Button("↑");
        Button southDec = new Button("↓");
        Button eastInc = new Button("↑");
        Button eastDec = new Button("↓");

        startBtn.setOnAction(e -> {
            controller.startSimulation(northLight, westLight, southLight, eastLight);
        });

        stopBtn.setOnAction(e -> {
            controller.pauseSimulation(northLight, westLight, southLight, eastLight);
        });

        resetBtn.setOnAction(e -> {
            controller.resetSimulation(northLight, westLight, southLight, eastLight);
            updateVehicleLabels();
        });

        assignVehiclesBtn.setOnAction(e -> {
            controller.assignVehiclesRandomly();
            updateVehicleLabels();
        });

        northInc.setOnAction(e -> adjustVehicleCount("North", 1));
        northDec.setOnAction(e -> adjustVehicleCount("North", -1));
        westInc.setOnAction(e -> adjustVehicleCount("West", 1));
        westDec.setOnAction(e -> adjustVehicleCount("West", -1));
        southInc.setOnAction(e -> adjustVehicleCount("South", 1));
        southDec.setOnAction(e -> adjustVehicleCount("South", -1));
        eastInc.setOnAction(e -> adjustVehicleCount("East", 1));
        eastDec.setOnAction(e -> adjustVehicleCount("East", -1));

        HBox controlButtons = new HBox(10, startBtn, stopBtn, resetBtn, assignVehiclesBtn);
        controlButtons.setAlignment(Pos.CENTER);

        grid.add(new Label("Kuzey"), 0, 0);
        grid.add(new HBox(5, northInc, northDec), 1, 0);
        grid.add(northVehicleLabel, 2, 0);
        grid.add(new Label("Batı"), 0, 1);
        grid.add(new HBox(5, westInc, westDec), 1, 1);
        grid.add(westVehicleLabel, 2, 1);
        grid.add(new Label("Güney"), 0, 2);
        grid.add(new HBox(5, southInc, southDec), 1, 2);
        grid.add(southVehicleLabel, 2, 2);
        grid.add(new Label("Doğu"), 0, 3);
        grid.add(new HBox(5, eastInc, eastDec), 1, 3);
        grid.add(eastVehicleLabel, 2, 3);
        grid.add(vehicleLabel, 0, 4, 3, 1);
        grid.add(controlButtons, 0, 5, 3, 1);

        return grid;
    }

    private VBox createTrafficLight(String direction) {
        Circle red = new Circle(8, Color.DARKRED);
        red.setStroke(Color.BLACK);
        Circle yellow = new Circle(8, Color.DARKGOLDENROD);
        yellow.setStroke(Color.BLACK);
        Circle green = new Circle(8, Color.DARKGREEN);
        green.setStroke(Color.BLACK);

        Label timeLabel = new Label("0");
        VBox light = new VBox(2, red, yellow, green, timeLabel);
        light.setAlignment(Pos.CENTER);
        return light;
    }

    private void updateVehicleLabels() {
        vehicleLabel.setText("Vehicle Count: " + controller.getTotalVehicleCount());
        northVehicleLabel.setText("North: " + controller.getVehicleDistribution().getOrDefault("North", 0) + " vehicles");
        westVehicleLabel.setText("West: " + controller.getVehicleDistribution().getOrDefault("West", 0) + " vehicles");
        southVehicleLabel.setText("South: " + controller.getVehicleDistribution().getOrDefault("South", 0) + " vehicles");
        eastVehicleLabel.setText("East: " + controller.getVehicleDistribution().getOrDefault("East", 0) + " vehicles");
    }

    private void adjustVehicleCount(String direction, int delta) {
        int current = controller.getVehicleDistribution().getOrDefault(direction, 0);
        int newCount = Math.max(0, current + delta);
        controller.getVehicleDistribution().put(direction, newCount);
        updateVehicleLabels();
        controller.recalculateGreenDurations();
    }
}