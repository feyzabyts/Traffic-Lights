package edu.erciyes.demo4birlestirmetrafik.View;

import edu.erciyes.demo4birlestirmetrafik.Controller.TrafficController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
    private Pane roadPane;

    public TrafficView(TrafficController controller) {
        this.controller = controller;
    }

    public Scene createScene() {
        HBox mainLayout = new HBox(20);
        mainLayout.setAlignment(Pos.CENTER);

        roadPane = createRoadPane();
        GridPane controlPane = createControlPane();

        roadPane.setPrefWidth(600);
        controlPane.setPrefWidth(300);

        mainLayout.getChildren().addAll(roadPane, controlPane);
        controller.setRoadPane(roadPane);

        return new Scene(mainLayout, 900, 600);
    }


    private Pane createRoadPane() {
        Pane roadPane = new Pane();
        double width = 600;
        double height = 600;
        //Arka planı yesil yapıyor
        roadPane.setStyle("-fx-background-color: #ace1af;");

        double laneWidth = 60;

        double roadWidth = laneWidth * 2;
        double intersectionSize = roadWidth;

        Color roadColor = Color.DIMGRAY;

        Rectangle westRoadIn = new Rectangle(0, height / 2 - laneWidth, width / 2 - intersectionSize / 2, laneWidth);
        Rectangle westRoadOut = new Rectangle(0, height / 2, width / 2 - intersectionSize / 2, laneWidth);
        Rectangle eastRoadIn = new Rectangle(width / 2 + intersectionSize / 2, height / 2, width / 2 - intersectionSize / 2, laneWidth);
        Rectangle eastRoadOut = new Rectangle(width / 2 + intersectionSize / 2, height / 2 - laneWidth, width / 2 - intersectionSize / 2, laneWidth);
        Rectangle northRoadIn = new Rectangle(width / 2, 0, laneWidth, height / 2 - intersectionSize / 2);
        Rectangle northRoadOut = new Rectangle(width / 2 - laneWidth, 0, laneWidth, height / 2 - intersectionSize / 2);
        Rectangle southRoadIn = new Rectangle(width / 2 - laneWidth, height / 2 + intersectionSize / 2, laneWidth, height / 2 - intersectionSize / 2);
        Rectangle southRoadOut = new Rectangle(width / 2, height / 2 + intersectionSize / 2, laneWidth, height / 2 - intersectionSize / 2);
        Rectangle intersection = new Rectangle(width / 2 - intersectionSize / 2, height / 2 - intersectionSize / 2, intersectionSize, intersectionSize);

        for (Rectangle r : new Rectangle[]{westRoadIn, westRoadOut, eastRoadIn, eastRoadOut,
                northRoadIn, northRoadOut, southRoadIn, southRoadOut, intersection}) {
            r.setFill(roadColor);
        }

        roadPane.getChildren().addAll(westRoadIn, westRoadOut, eastRoadIn, eastRoadOut,
                northRoadIn, northRoadOut, southRoadIn, southRoadOut, intersection);

        Line[] dashedLines = new Line[]{
                new Line(0, height / 2, width / 2 - intersectionSize / 2, height / 2),
                new Line(width / 2 + intersectionSize / 2, height / 2, width, height / 2),
                new Line(width / 2, 0, width / 2, height / 2 - intersectionSize / 2),
                new Line(width / 2, height / 2 + intersectionSize / 2, width / 2, height)
        };

        for (Line dashed : dashedLines) {
            dashed.setStroke(Color.WHITE);
            dashed.setStrokeWidth(2);
            dashed.getStrokeDashArray().addAll(10.0, 10.0);
        }

        roadPane.getChildren().addAll(dashedLines);

        northLight = createTrafficLight("North");
        westLight = createTrafficLight("West");
        southLight = createTrafficLight("South");
        eastLight = createTrafficLight("East");

        northLight.setRotate(90);
        northLight.setLayoutX(width / 2 + intersectionSize / 2 - 170);
        northLight.setLayoutY(height / 2 - intersectionSize / 2 - 50);

        southLight.setRotate(270);
        southLight.setLayoutX(width / 2 - intersectionSize / 2 + 160);
        southLight.setLayoutY(height / 2 + intersectionSize / 2 - 19);

        westLight.setRotate(180);
        westLight.setLayoutX(width / 2 - intersectionSize / 2 - 30);
        westLight.setLayoutY(height / 2 - intersectionSize / 2 + 120);

        eastLight.setRotate(0);
        eastLight.setLayoutX(width / 2 + intersectionSize / 2 + 10);
        eastLight.setLayoutY(height / 2 + intersectionSize / 2 - 200);

        roadPane.getChildren().addAll(northLight, westLight, southLight, eastLight);

        Label northLabel = new Label("NORTH");
        northLabel.setLayoutX(northLight.getLayoutX());
        northLabel.setLayoutY(northLight.getLayoutY() - 10);
        northLabel.setRotate(90);

        Label southLabel = new Label("SOUTH");
        southLabel.setLayoutX(southLight.getLayoutX() - 20);
        southLabel.setLayoutY(southLight.getLayoutY() + 70);
        southLabel.setRotate(270);

        Label westLabel = new Label("WEST");
        westLabel.setLayoutX(westLight.getLayoutX() - 40);
        westLabel.setLayoutY(westLight.getLayoutY() + 20);
        westLabel.setRotate(0);

        Label eastLabel = new Label("EAST");
        eastLabel.setLayoutX(eastLight.getLayoutX() + 20);
        eastLabel.setLayoutY(eastLight.getLayoutY() + 20);
        eastLabel.setRotate(0);

        roadPane.getChildren().addAll(northLabel, southLabel, westLabel, eastLabel);

        Image treeImage = new Image(getClass().getResourceAsStream("/images/tree.png"));

// Her köşe için bir ImageView oluştur
        ImageView[] treeIcons = new ImageView[] {
                new ImageView(treeImage),
                new ImageView(treeImage),
                new ImageView(treeImage),
                new ImageView(treeImage)
        };

        for (ImageView tree : treeIcons) {
            tree.setFitWidth(80);
            tree.setFitHeight(80);
        }
        //Konum ayarlama
        treeIcons[0].setLayoutX(30);  treeIcons[0].setLayoutY(30);
        treeIcons[1].setLayoutX(450); treeIcons[1].setLayoutY(30);
        treeIcons[2].setLayoutX(450); treeIcons[2].setLayoutY(550);
        treeIcons[3].setLayoutX(30);  treeIcons[3].setLayoutY(550);

        for (ImageView tree : treeIcons) {
            roadPane.getChildren().add(tree);  // root senin ana layout'un olmalı (örneğin Pane, AnchorPane vs.)
        }

        System.out.println("Traffic Light Coordinates:");
        System.out.println("North Traffic Light: (" + northLight.getLayoutX() + ", " + northLight.getLayoutY() + ")");
        System.out.println("South Traffic Light: (" + southLight.getLayoutX() + ", " + southLight.getLayoutY() + ")");
        System.out.println("West Traffic Light: (" + westLight.getLayoutX() + ", " + westLight.getLayoutY() + ")");
        System.out.println("East Traffic Light: (" + eastLight.getLayoutX() + ", " + eastLight.getLayoutY() + ")");

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

        // Pass all buttons to the controller
        Button northInc = new Button("↑");
        Button northDec = new Button("↓");
        Button westInc = new Button("↑");
        Button westDec = new Button("↓");
        Button southInc = new Button("↑");
        Button southDec = new Button("↓");
        Button eastInc = new Button("↑");
        Button eastDec = new Button("↓");

        //Buton aktiflestirme
        controller.setButtons(startBtn, assignVehiclesBtn, stopBtn, resetBtn, northInc, northDec, westInc, westDec, southInc, southDec, eastInc, eastDec);

        startBtn.setOnAction(e -> {
            controller.startSimulation(northLight, westLight, southLight, eastLight);
            updateVehicleLabels();
        });

        stopBtn.setOnAction(e -> {
            controller.pauseSimulation(northLight, westLight, southLight, eastLight);
            updateVehicleLabels();
        });

        resetBtn.setOnAction(e -> {
            controller.resetSimulation(northLight, westLight, southLight, eastLight);
            updateVehicleLabels();
        });

        assignVehiclesBtn.setOnAction(e -> {
            controller.assignVehiclesRandomly();
            updateVehicleLabels();
        });


        northInc.setOnAction(e -> {
            controller.adjustVehicleCount("North", 1);
            updateVehicleLabels();
        });
        northDec.setOnAction(e -> {
            controller.adjustVehicleCount("North", -1);
            updateVehicleLabels();
        });
        westInc.setOnAction(e -> {
            controller.adjustVehicleCount("West", 1);
            updateVehicleLabels();
        });
        westDec.setOnAction(e -> {
            controller.adjustVehicleCount("West", -1);
            updateVehicleLabels();
        });
        southInc.setOnAction(e -> {
            controller.adjustVehicleCount("South", 1);
            updateVehicleLabels();
        });
        southDec.setOnAction(e -> {
            controller.adjustVehicleCount("South", -1);
            updateVehicleLabels();
        });
        eastInc.setOnAction(e -> {
            controller.adjustVehicleCount("East", 1);
            updateVehicleLabels();
        });
        eastDec.setOnAction(e -> {
            controller.adjustVehicleCount("East", -1);
            updateVehicleLabels();
        });

        HBox controlButtons = new HBox(10, startBtn, stopBtn, resetBtn, assignVehiclesBtn);
        controlButtons.setAlignment(Pos.CENTER);

        grid.add(new Label("North"), 0, 0);
        grid.add(new HBox(5, northInc, northDec), 1, 0);
        grid.add(northVehicleLabel, 2, 0);
        grid.add(new Label("West"), 0, 1);
        grid.add(new HBox(5, westInc, westDec), 1, 1);
        grid.add(westVehicleLabel, 2, 1);
        grid.add(new Label("South"), 0, 2);
        grid.add(new HBox(5, southInc, southDec), 1, 2);
        grid.add(southVehicleLabel, 2, 2);
        grid.add(new Label("East"), 0, 3);
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
        VBox light = new VBox(4, red, yellow, green, timeLabel);
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
}