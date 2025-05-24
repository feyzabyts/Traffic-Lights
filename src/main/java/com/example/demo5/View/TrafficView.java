package com.example.demo5.View;

import com.example.demo5.Controller.*;
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

    public Scene createCombinedScene() {
        HBox mainLayout = new HBox(20);
        mainLayout.setAlignment(Pos.CENTER);

        roadPane = createRoadPane();
        GridPane controlPane = createControlPane();

        roadPane.setPrefWidth(600);
        controlPane.setPrefWidth(300);

        mainLayout.getChildren().addAll(roadPane, controlPane);
        controller.setRoadPane(roadPane); // roadPane'i kontrolöre ilet

        return new Scene(mainLayout, 900, 600);
    }
    private Line createDashedLine(double startX, double startY, double endX, double endY) {
        Line dashedLine = new Line(startX, startY, endX, endY);
        dashedLine.setStroke(Color.WHITE);
        dashedLine.setStrokeWidth(2);
        dashedLine.getStrokeDashArray().addAll(10.0, 10.0); // 10 birim çizgi, 10 birim boşluk
        return dashedLine;
    }

    private Pane createRoadPane() {
        Pane roadPane = new Pane();
        double width = 600;
        double height = 600;

        double laneWidth = 60;
        double roadWidth = laneWidth * 2;
        double intersectionSize = roadWidth;

        Color roadColor = Color.DIMGRAY;

        // --- YOLLAR ---
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

        // --- ŞERİT AYIRICI KESİKLİ ÇİZGİLER ---
        Line[] dashedLines = new Line[]{
                new Line(0, height / 2, width / 2 - intersectionSize / 2, height / 2),                     // Batı
                new Line(width / 2 + intersectionSize / 2, height / 2, width, height / 2),                 // Doğu
                new Line(width / 2, 0, width / 2, height / 2 - intersectionSize / 2),                      // Kuzey
                new Line(width / 2, height / 2 + intersectionSize / 2, width / 2, height)                  // Güney
        };

        for (Line dashed : dashedLines) {
            dashed.setStroke(Color.WHITE);
            dashed.setStrokeWidth(2);
            dashed.getStrokeDashArray().addAll(10.0, 10.0); // Kesikli çizgi ayarı
        }

        roadPane.getChildren().addAll(dashedLines);

        // --- TRAFİK IŞIKLARI ---
        // Trafik ışıklarını oluştur
        northLight = createTrafficLight("North");
        westLight = createTrafficLight("West");
        southLight = createTrafficLight("South");
        eastLight = createTrafficLight("East");

// KUZEY: Yatay konumda, aşağı yönlü (rotate 90)
        northLight.setRotate(90);
        northLight.setLayoutX(width / 2 + intersectionSize / 2 - 170); // ortadan biraz sağda
        northLight.setLayoutY(height / 2 - intersectionSize / 2 - 50); // ortanın yukarısında

// GÜNEY: Yatay konumda, yukarı yönlü (rotate 270)
        southLight.setRotate(270);
        southLight.setLayoutX(width / 2 - intersectionSize / 2 + 160); // ortadan biraz solda
        southLight.setLayoutY(height / 2 + intersectionSize / 2 - 19 ); // ortanın aşağısında

// BATI: Yatay konumda, sağa yönlü (rotate 0)
        westLight.setRotate(180);
        westLight.setLayoutX(width / 2 - intersectionSize / 2 - 30); // ortanın solunda
        westLight.setLayoutY(height / 2 - intersectionSize / 2 + 120); // ortanın yukarısında

// DOĞU: Yatay konumda, sola yönlü (rotate 180)
        eastLight.setRotate(0);
        eastLight.setLayoutX(width / 2 + intersectionSize / 2 + 10); // ortanın sağında
        eastLight.setLayoutY(height / 2 + intersectionSize / 2 - 200); // ortanın aşağısında

// Hepsini sahneye ekle
        roadPane.getChildren().addAll(northLight, westLight, southLight, eastLight);
        // Kuzey için label
        Label northLabel = new Label("NORTH");
        northLabel.setLayoutX(northLight.getLayoutX());
        northLabel.setLayoutY(northLight.getLayoutY() - 10);  // Trafik ışığının üstüne 20 piksel yukarı
        northLabel.setRotate(90);  // Kuzey ışığı da döndürülmüş, yazıyı da uygun döndür

// Güney için label
        Label southLabel = new Label("SOUTH");
        southLabel.setLayoutX(southLight.getLayoutX()- 20);
        southLabel.setLayoutY(southLight.getLayoutY() + 70);  // Üstüne koyduk
        southLabel.setRotate(270);

// Batı için label
        Label westLabel = new Label("WEST");
        westLabel.setLayoutX(westLight.getLayoutX()- 40);
        westLabel.setLayoutY(westLight.getLayoutY() + 20);  // Üstüne koyduk
        westLabel.setRotate(0);

// Doğu için label
        Label eastLabel = new Label("EAST");
        eastLabel.setLayoutX(eastLight.getLayoutX()+ 20);
        eastLabel.setLayoutY(eastLight.getLayoutY() + 20);  // Üstüne koyduk
        eastLabel.setRotate(0);

// Label’ları sahneye ekle
        roadPane.getChildren().addAll(northLabel, southLabel, westLabel, eastLabel);

        // --- KÖŞE ETİKETLERİ ---
        Label[] coordLabels = new Label[]{
                new Label("<K1,T1,D1,D2,B1>"),
                new Label("<B4,D4,D3,T3,K3>"),
                new Label("<B2,D1,D4,T4,K4>"),
                new Label("<K2,T2,D2,D3,B3>")
        };
        coordLabels[0].setLayoutX(30); coordLabels[0].setLayoutY(30);
        coordLabels[1].setLayoutX(450); coordLabels[1].setLayoutY(30);
        coordLabels[2].setLayoutX(450); coordLabels[2].setLayoutY(550);
        coordLabels[3].setLayoutX(30); coordLabels[3].setLayoutY(550);

        roadPane.getChildren().addAll(coordLabels);

        // --- KONSOLA KOORDİNAT YAZDIR ---
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