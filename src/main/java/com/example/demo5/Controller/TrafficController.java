package com.example.demo5.Controller;

import com.example.demo5.Model.*;
import com.example.demo5.View.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TrafficController {
    private static final int TOTAL_CYCLE_DURATION = 120;
    private static final int YELLOW_DURATION = 3;
    private Map<String, Integer> vehicleDistribution;
    private Map<String, Integer> greenDurations;
    private Map<String, String> lightStates;
    private Timeline timeline;
    private Timeline vehicleTimeline;
    private double elapsedTime = 0;
    private Button startBtn;
    private Button assignVehiclesBtn;
    private Button stopBtn;
    private Button resetBtn;
    private Button northInc;
    private Button southInc;
    private Button eastInc;
    private Button westInc;
    private Button northDec;
    private Button southDec;
    private Button eastDec;
    private Button westDec;
    private List<Vehicle> vehicles = new ArrayList<>();
    private Map<String, Integer> vehiclesToInc;
    private int vehicleIncTimer = 0;
    private Pane roadPane;

    public TrafficController() {
        vehicleDistribution = new HashMap<>();
        greenDurations = new HashMap<>();
        lightStates = new HashMap<>();
        vehiclesToInc = new HashMap<>();
        vehicleDistribution.put("North", 0);
        vehicleDistribution.put("West", 0);
        vehicleDistribution.put("South", 0);
        vehicleDistribution.put("East", 0);
        lightStates.put("North", "RED");
        lightStates.put("West", "RED");
        lightStates.put("South", "RED");
        lightStates.put("East", "RED");
        calculateGreenDurations();
    }

    public void setButtons(Button startBtn, Button assignVehiclesBtn, Button stopBtn, Button resetBtn,
                           Button northInc, Button northDec, Button westInc, Button westDec,
                           Button southInc, Button southDec, Button eastInc, Button eastDec) {
        this.startBtn = startBtn;
        this.assignVehiclesBtn = assignVehiclesBtn;
        this.stopBtn = stopBtn;
        this.resetBtn = resetBtn;
        this.northInc = northInc;
        this.northDec = northDec;
        this.westInc = westInc;
        this.westDec = westDec;
        this.southInc = southInc;
        this.southDec = southDec;
        this.eastInc = eastInc;
        this.eastDec = eastDec;
    }

    public void setRoadPane(Pane roadPane) {
        this.roadPane = roadPane;
    }

    public void assignVehiclesRandomly() {
        vehicleDistribution.clear();
        vehiclesToInc.clear();
        vehicles.clear();
        roadPane.getChildren().removeIf(node -> node instanceof BusView || node instanceof TaxiView || node instanceof TruckView);

        String[] directions = {"North", "West", "South", "East"};
        int totalVehicles = (int) (20 + Math.random() * 41);
        int minVehiclesPerDirection = 5;
        int remaining = totalVehicles;

        for (String dir : directions) {
            vehicleDistribution.put(dir, minVehiclesPerDirection);
            remaining -= minVehiclesPerDirection;
        }

        if (remaining > 0) {
            for (int i = 0; i < directions.length - 1; i++) {
                int maxAdditional = remaining / (directions.length - i);
                int count = (int) (Math.random() * (maxAdditional + 1));
                vehicleDistribution.put(directions[i], vehicleDistribution.get(directions[i]) + count);
                remaining -= count;
            }
            vehicleDistribution.put(directions[3], vehicleDistribution.get(directions[3]) + remaining);
        }

        for (String dir : directions) {
            int total = vehicleDistribution.get(dir);
            int toShowNow = Math.min(3, total);
            int toSpawnLater = total - toShowNow;

            for (int i = 0; i < toShowNow; i++) {
                bastakiVehicle(dir, i);
            }
            vehiclesToInc.put(dir, toSpawnLater);
        }

        recalculateGreenDurations();
        enableIncDecButtons(true);
    }

    private void enableIncDecButtons(boolean enable) {
        if (northInc != null) northInc.setDisable(!enable);
        if (northDec != null) northDec.setDisable(!enable);
        if (southInc != null) southInc.setDisable(!enable);
        if (southDec != null) southDec.setDisable(!enable);
        if (eastInc != null) eastInc.setDisable(!enable);
        if (eastDec != null) eastDec.setDisable(!enable);
        if (westInc != null) westInc.setDisable(!enable);
        if (westDec != null) westDec.setDisable(!enable);
    }

    private void calculateGreenDurations() {
        greenDurations.clear();
        int totalVehicles = vehicleDistribution.values().stream().mapToInt(Integer::intValue).sum();
        int totalGreenAvailable = TOTAL_CYCLE_DURATION - 4 * YELLOW_DURATION;

        for (Map.Entry<String, Integer> entry : vehicleDistribution.entrySet()) {
            int count = entry.getValue();
            int duration = totalVehicles == 0 ? 5 : (int) ((count / (double) totalVehicles) * totalGreenAvailable);
            greenDurations.put(entry.getKey(), Math.max(duration, 5));
        }
    }

    public void recalculateGreenDurations() {
        calculateGreenDurations();
    }

    public TrafficLight getTrafficLightForDirection(String direction) {
        int greenDuration = greenDurations.getOrDefault(direction, 5);
        int redDuration = TOTAL_CYCLE_DURATION - greenDuration - YELLOW_DURATION;
        return new TrafficLight(greenDuration, redDuration, YELLOW_DURATION);
    }

    public Map<String, Integer> getVehicleDistribution() {
        return vehicleDistribution;
    }

    public int getTotalVehicleCount() {
        return vehicleDistribution.values().stream().mapToInt(Integer::intValue).sum();
    }

    public void adjustVehicleCount(String direction, int delta) {
        int current = vehicleDistribution.getOrDefault(direction, 0);
        int newCount = Math.max(0, current + delta);
        vehicleDistribution.put(direction, newCount);
        vehiclesToInc.put(direction, newCount);

        if (delta > 0) {
            bastakiVehicle(direction, 0);
        } else if (delta < 0 && current > 0) {
            removeVehicle(direction);
        }

        recalculateGreenDurations();
    }

    private void addVehicle(String direction, boolean forceShow) {
        if (!forceShow && !"GREEN".equals(lightStates.getOrDefault(direction, "RED"))) {
            return;
        }

        VehicleType type = VehicleType.values()[(int) (Math.random() * VehicleType.values().length)];
        Node sprite;
        double x = 0, y = 0;
        Direction dir = Direction.valueOf(direction.toUpperCase());

        switch (dir) {
            case NORTH:
                x = 330;
                y = 0;
                break;
            case SOUTH:
                x = 270;
                y = 600;
                break;
            case EAST:
                x = 600;
                y = 270;
                break;
            case WEST:
                x = 0;
                y = 330;
                break;
        }

        switch (type) {
            case TAXI:
                sprite = new TaxiView(x, y);
                break;
            case TRUCK:
                sprite = new TruckView(x, y);
                break;
            case BUS:
                sprite = new BusView(x, y);
                break;
            default:
                sprite = new TaxiView(x, y);
        }

        switch (dir) {
            case NORTH:
                sprite.setRotate(90);
                break;
            case SOUTH:
                sprite.setRotate(270);
                break;
            case EAST:
                sprite.setRotate(180);
                break;
            case WEST:
                sprite.setRotate(0);
                break;
        }

        roadPane.getChildren().add(sprite);
        Vehicle vehicle = new Vehicle(x, y, type, sprite, dir);
        vehicle.setLightState(lightStates.get(direction));
        vehicles.add(vehicle);
    }

    private void bastakiVehicle(String direction, int index) {
        VehicleType type = VehicleType.values()[(int) (Math.random() * VehicleType.values().length)];
        Node sprite;
        double x = 0, y = 0;
        Direction dir = Direction.valueOf(direction.toUpperCase());

        double offset = 0;
        List<Vehicle> sameDirVehicles = vehicles.stream()
                .filter(v -> v.getDirection().toString().equals(direction))
                .sorted((v1, v2) -> {
                    switch (dir) {
                        case NORTH:
                            return Double.compare(v1.Yget(), v2.Yget());
                        case SOUTH:
                            return Double.compare(v2.Yget(), v1.Yget());
                        case EAST:
                            return Double.compare(v2.Xget(), v1.Xget());
                        case WEST:
                            return Double.compare(v1.Xget(), v2.Xget());
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());

        if (!sameDirVehicles.isEmpty()) {
            Vehicle lastVehicle = sameDirVehicles.get(sameDirVehicles.size() - 1);
            double lastPos = getPosition(lastVehicle);
            double spacing = getVehicleSpacing(type, dir);
            double lastSpacing = getVehicleSpacing(lastVehicle.getType(), dir);
            double requiredGap = (spacing + lastSpacing) / 2;

            switch (dir) {
                case NORTH:
                    offset = lastPos - requiredGap;
                    break;
                case SOUTH:
                    offset = lastPos + requiredGap;
                    break;
                case EAST:
                    offset = lastPos + requiredGap;
                    break;
                case WEST:
                    offset = lastPos - requiredGap;
                    break;
            }
        } else {
            offset = index * getVehicleSpacing(type, dir);
        }

        switch (dir) {
            case NORTH:
                x = 300;           // Kuzey giriş yolu merkezi (300 + laneWidth/2)
                y = 200 - offset;  // Kavşak üst kenarı (240) öncesi
                break;
            case SOUTH:
                x = 250;           // Güney giriş yolu merkezi (240 + laneWidth/2)
                y = 360 + offset;  // Kavşak alt kenarı (360) sonrası
                break;
            case EAST:
                x = 390 + offset;  // Kavşak sağ kenarı (360) sonrası
                y = 250;           // Doğu giriş yolu merkezi (240 + laneWidth/2)
                break;
            case WEST:
                x = 200 - offset;  // Kavşak sol kenarı (240) öncesi
                y = 320;           // Batı giriş yolu merkezi (300 + laneWidth/2)
                break;
        }

        switch (type) {
            case TAXI:
                sprite = new TaxiView(x, y);
                break;
            case TRUCK:
                sprite = new TruckView(x, y);
                break;
            case BUS:
                sprite = new BusView(x, y);
                break;
            default:
                sprite = new TaxiView(x, y);
        }

        switch (dir) {
            case NORTH:
                sprite.setRotate(90);
                break;
            case SOUTH:
                sprite.setRotate(270);
                break;
            case EAST:
                sprite.setRotate(180);
                break;
            case WEST:
                sprite.setRotate(0);
                break;
        }

        roadPane.getChildren().add(sprite);
        Vehicle vehicle = new Vehicle(x, y, type, sprite, dir);
        vehicle.setLightState("RED");
        vehicles.add(vehicle);
    }

    private void removeVehicle(String direction) {
        Vehicle vehicleToRemove = vehicles.stream()
                .filter(v -> v.getDirection().toString().equals(direction))
                .findFirst()
                .orElse(null);

        if (vehicleToRemove != null) {
            roadPane.getChildren().remove(vehicleToRemove.getsprite());
            vehicles.remove(vehicleToRemove);
        }
    }

    public void startSimulation(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        if (timeline != null && timeline.getStatus() == Timeline.Status.PAUSED) {
            timeline.play();
            vehicleTimeline.play();
            updateAllTimers(northLight, westLight, southLight, eastLight);
            startBtn.setDisable(true);
            assignVehiclesBtn.setDisable(true);
            stopBtn.setDisable(false);
            resetBtn.setDisable(false);
            enableIncDecButtons(false);
            return;
        }

        if (timeline != null) {
            timeline.stop();
            vehicleTimeline.stop();
        }

        vehicles.clear();
        roadPane.getChildren().removeIf(node -> node instanceof BusView || node instanceof TaxiView || node instanceof TruckView);
        vehiclesToInc.clear();
        for (Map.Entry<String, Integer> entry : vehicleDistribution.entrySet()) {
            String dir = entry.getKey();
            int count = entry.getValue();
            vehiclesToInc.put(dir, count);
            for (int i = 0; i < Math.min(3, count); i++) {
                bastakiVehicle(dir, i);
            }
            vehiclesToInc.put(dir, count - Math.min(3, count));
        }

        timeline = new Timeline();
        vehicleTimeline = new Timeline(new KeyFrame(Duration.millis(20), e -> updateVehicles()));
        vehicleTimeline.setCycleCount(Timeline.INDEFINITE);
        vehicleTimeline.play();

        int yellowDuration = 3;
        double t = elapsedTime;

        String[] directions = {"North", "West", "South", "East"};
        VBox[] lights = {northLight, westLight, southLight, eastLight};
        ArrayList<KeyFrame> frames = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            String direction = directions[i];
            VBox light = lights[i];
            int greenDuration = getTrafficLightForDirection(direction).getGreenLed().getDuration();

            if (t < 120) {
                frames.add(new KeyFrame(Duration.seconds(t - elapsedTime), e -> {
                    SetallRED(northLight, westLight, southLight, eastLight);
                    setLightState(light, "GREEN", direction);
                    updateTimer(light, greenDuration);
                    updateVehicleLightStates(direction, "GREEN");
                }));
                t += greenDuration;
            }

            if (t < 120) {
                frames.add(new KeyFrame(Duration.seconds(t - elapsedTime), e -> {
                    setLightState(light, "YELLOW", direction);
                    updateTimer(light, yellowDuration);
                    updateVehicleLightStates(direction, "YELLOW");
                }));
                t += yellowDuration;
            }
        }

        if (t < 120) {
            frames.add(new KeyFrame(Duration.seconds(120 - elapsedTime), e -> {
                SetallRED(northLight, westLight, southLight, eastLight);
                updateVehicleLightStates(null, "RED");
                elapsedTime = 0;
                startBtn.setDisable(false);
                assignVehiclesBtn.setDisable(false);
                stopBtn.setDisable(true);
                resetBtn.setDisable(false);
                enableIncDecButtons(true);
            }));
        }

        timeline.getKeyFrames().addAll(frames);
        timeline.setCycleCount(1);
        timeline.setOnFinished(e -> {
            elapsedTime = 0;
            startBtn.setDisable(false);
            assignVehiclesBtn.setDisable(false);
            stopBtn.setDisable(true);
            resetBtn.setDisable(false);
            enableIncDecButtons(true);
        });
        timeline.play();

        Timeline timeTracker = new Timeline(new KeyFrame(Duration.seconds(1), e -> elapsedTime += 1));
        timeTracker.setCycleCount(Timeline.INDEFINITE);
        timeTracker.play();

        startBtn.setDisable(true);
        assignVehiclesBtn.setDisable(true);
        stopBtn.setDisable(false);
        resetBtn.setDisable(false);
        enableIncDecButtons(false);
    }

    private void updateVehicles() {
        vehicleIncTimer++;
        if (vehicleIncTimer >= 100) {
            vehicleIncTimer = 0;
            increaseVehicles();
        }

        for (Direction dir : Direction.values()) {
            String direction = dir.toString();
            List<Vehicle> dirVehicles = vehicles.stream()
                    .filter(v -> v.getDirection().toString().equals(direction))
                    .sorted((v1, v2) -> {
                        switch (dir) {
                            case NORTH:
                                return Double.compare(v1.Yget(), v2.Yget());
                            case SOUTH:
                                return Double.compare(v2.Yget(), v1.Yget());
                            case EAST:
                                return Double.compare(v2.Xget(), v1.Xget());
                            case WEST:
                                return Double.compare(v1.Xget(), v2.Xget());
                            default:
                                return 0;
                        }
                    })
                    .collect(Collectors.toList());

            double lastPos = GetStartPosition(dir);
            Vehicle prevVehicle = null;

            for (Vehicle v : dirVehicles) {
                double currPos = getPosition(v);
                double spacing = getVehicleSpacing(v.getType(), dir);

                if ("RED".equals(v.getLightState()) || "YELLOW".equals(v.getLightState())) {
                    v.setMoving(false);
                } else {
                    boolean canMove = true;
                    if (prevVehicle != null) {
                        double prevPos = getPosition(prevVehicle);
                        double prevSpacing = getVehicleSpacing(prevVehicle.getType(), dir);
                        double requiredGap = (spacing + prevSpacing) / 2;

                        switch (dir) {
                            case NORTH:
                                if (currPos - prevPos < requiredGap) canMove = false;
                                break;
                            case SOUTH:
                                if (prevPos - currPos < requiredGap) canMove = false;
                                break;
                            case EAST:
                                if (prevPos - currPos < requiredGap) canMove = false;
                                break;
                            case WEST:
                                if (currPos - prevPos < requiredGap) canMove = false;
                                break;
                        }
                    }

                    v.setMoving(canMove);
                }

                if (v.haraketet()) {
                    v.update();
                }

                if (isOutOfRoad(v)) {
                    roadPane.getChildren().remove(v.getsprite());
                    vehicles.remove(v);
                    vehicleDistribution.put(direction, vehicleDistribution.getOrDefault(direction, 0) - 1);
                    vehiclesToInc.put(direction, vehiclesToInc.getOrDefault(direction, 0) - 1);
                    recalculateGreenDurations();
                    break;
                }

                prevVehicle = v;
                lastPos = currPos;
            }
        }
    }

    private void increaseVehicles() {
        for (String dir : new String[]{"North", "West", "South", "East"}) {
            int count = vehiclesToInc.getOrDefault(dir, 0);
            if (count > 0 && "GREEN".equals(lightStates.getOrDefault(dir, "RED"))) {
                addVehicle(dir, true);
                vehiclesToInc.put(dir, count - 1);
            }
        }
    }

    private double GetStartPosition(Direction dir) {
        switch (dir) {
            case NORTH:
                return 195;
            case SOUTH:
                return 336;
            case EAST:
                return 365;
            case WEST:
                return 215;
            default:
                return 0;
        }
    }

    private double getPosition(Vehicle v) {
        switch (v.getDirection()) {
            case NORTH:
            case SOUTH:
                return v.Yget();
            case EAST:
            case WEST:
                return v.Xget();
            default:
                return 0;
        }
    }

    private double getVehicleSpacing(VehicleType type, Direction dir) {
        switch (type) {
            case TAXI:
                return dir == Direction.NORTH || dir == Direction.SOUTH ? 35 : 35;
            case BUS:
                return dir == Direction.NORTH || dir == Direction.SOUTH ? 45 : 45;
            case TRUCK:
                return dir == Direction.NORTH || dir == Direction.SOUTH ? 55 : 55;
            default:
                return 35;
        }
    }

    private boolean isOutOfRoad(Vehicle v) {
        switch (v.getDirection()) {
            case NORTH:
                return v.Yget() > 600;
            case SOUTH:
                return v.Yget() < 0;
            case EAST:
                return v.Xget() < 0;
            case WEST:
                return v.Xget() > 600;
            default:
                return false;
        }
    }

    public void pauseSimulation(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.pause();
            vehicleTimeline.pause();
            for (VBox light : new VBox[]{northLight, westLight, southLight, eastLight}) {
                if (light != null) {
                    Label timerLabel = (Label) light.getChildren().get(3);
                    String currentTime = timerLabel.getText();
                    if (!currentTime.equals("0")) {
                        Timeline countdown = new Timeline();
                        countdown.getKeyFrames().add(new KeyFrame(Duration.seconds(0), e -> timerLabel.setText(currentTime)));
                        countdown.play();
                    }
                }
            }
            startBtn.setDisable(false);
            assignVehiclesBtn.setDisable(true);
            stopBtn.setDisable(true);
            resetBtn.setDisable(false);
            enableIncDecButtons(false);
        }
    }

    public void resetSimulation(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        if (timeline != null) {
            timeline.stop();
            vehicleTimeline.stop();
        }
        elapsedTime = 0;
        SetallRED(northLight, westLight, southLight, eastLight);
        resetAllTimers(northLight, westLight, southLight, eastLight);
        vehicleDistribution.put("North", 0);
        vehicleDistribution.put("West", 0);
        vehicleDistribution.put("South", 0);
        vehicleDistribution.put("East", 0);
        vehiclesToInc.clear();
        vehicles.clear();
        roadPane.getChildren().removeIf(node -> node instanceof BusView || node instanceof TaxiView || node instanceof TruckView);
        timeline = null;
        vehicleTimeline = null;
        startBtn.setDisable(false);
        assignVehiclesBtn.setDisable(false);
        stopBtn.setDisable(true);
        resetBtn.setDisable(false);
        enableIncDecButtons(true);
        recalculateGreenDurations();
    }

    private void SetallRED(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        setLightState(northLight, "RED", "North");
        setLightState(westLight, "RED", "West");
        setLightState(southLight, "RED", "South");
        setLightState(eastLight, "RED", "East");
        updateVehicleLightStates(null, "RED");
    }

    private void setLightState(VBox light, String state, String direction) {
        if (light == null) return;
        Circle red = (Circle) light.getChildren().get(0);
        Circle yellow = (Circle) light.getChildren().get(1);
        Circle green = (Circle) light.getChildren().get(2);

        switch (state) {
            case "RED":
                red.setFill(Color.RED);
                yellow.setFill(Color.DARKGOLDENROD);
                green.setFill(Color.DARKGREEN);
                break;
            case "YELLOW":
                red.setFill(Color.DARKRED);
                yellow.setFill(Color.YELLOW);
                green.setFill(Color.DARKGREEN);
                break;
            case "GREEN":
                red.setFill(Color.DARKRED);
                yellow.setFill(Color.DARKGOLDENROD);
                green.setFill(Color.LIMEGREEN);
                break;
        }
        if (direction != null) {
            lightStates.put(direction, state);
        }
    }

    private void updateVehicleLightStates(String direction, String state) {
        if (direction == null) {
            vehicles.forEach(v -> v.setLightState("RED"));
        } else {
            try {
                Direction dirEnum = Direction.valueOf(direction.toUpperCase());
                vehicles.stream()
                        .filter(v -> v.getDirection() == dirEnum)
                        .forEach(v -> v.setLightState(state));
            } catch (IllegalArgumentException e) {
                System.err.println("Geçersiz yön: " + direction);
            }
        }
    }

    private void updateTimer(VBox light, int duration) {
        if (light == null) return;
        Label timerLabel = (Label) light.getChildren().get(3);
        timerLabel.setText(String.valueOf(duration));

        Timeline countdown = new Timeline();
        for (int i = duration - 1; i >= 0; i--) {
            final int count = i;
            countdown.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(duration - i), e -> timerLabel.setText(String.valueOf(count)))
            );
        }
        countdown.play();
    }

    private void updateAllTimers(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        for (VBox light : new VBox[]{northLight, westLight, southLight, eastLight}) {
            if (light != null) {
                Label timerLabel = (Label) light.getChildren().get(3);
                try {
                    int currentTime = Integer.parseInt(timerLabel.getText());
                    if (currentTime > 0) {
                        Timeline countdown = new Timeline();
                        for (int i = currentTime - 1; i >= 0; i--) {
                            final int count = i;
                            countdown.getKeyFrames().add(
                                    new KeyFrame(Duration.seconds(currentTime - i), e -> timerLabel.setText(String.valueOf(count)))
                            );
                        }
                        countdown.play();
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    private void resetAllTimers(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        for (VBox light : new VBox[]{northLight, westLight, southLight, eastLight}) {
            if (light != null) {
                Label timerLabel = (Label) light.getChildren().get(3);
                Timeline countdown = new Timeline();
                countdown.getKeyFrames().add(new KeyFrame(Duration.seconds(0), e -> timerLabel.setText("0")));
                countdown.play();
            }
        }
    }
}