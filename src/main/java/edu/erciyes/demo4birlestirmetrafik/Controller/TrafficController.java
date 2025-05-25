package edu.erciyes.demo4birlestirmetrafik.Controller;

import edu.erciyes.demo4birlestirmetrafik.Model.TrafficLight;
import edu.erciyes.demo4birlestirmetrafik.Model.Direction;
import edu.erciyes.demo4birlestirmetrafik.Model.Vehicle;
import edu.erciyes.demo4birlestirmetrafik.Model.VehicleType;
import edu.erciyes.demo4birlestirmetrafik.View.BusView;
import edu.erciyes.demo4birlestirmetrafik.View.TaxiView;
import edu.erciyes.demo4birlestirmetrafik.View.TruckView;
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
        int totalVehicles = (int) (20 + Math.random() * 61);
        int remaining = totalVehicles;

        for (int i = 0; i < directions.length - 1; i++) {
            int count = (int) (Math.random() * (remaining + 1));
            vehicleDistribution.put(directions[i], count);
            remaining -= count;
        }
        vehicleDistribution.put(directions[3], remaining); // kalanları son yöne ver
        calculateGreenDurations();

        for (String dir : directions) {
            int total = vehicleDistribution.getOrDefault(dir, 0);
            int toProduceNow = Math.min(3, total);
            int toProduceLater = Math.max(0, total - 3);

            // Sadece 3 tanesini şimdi ekle (kırmızı ışıkta da olsa bekleyecek)
            for (int i = 0; i < toProduceNow; i++) {
                addVehicle(dir, false); // hemen sahneye ekle
            }
            // Geri kalanı sıraya al, yeşilde üretilecek
            vehiclesToInc.put(dir, toProduceLater);
        }

        // Butonları aktif et
        northInc.setDisable(false);
        northDec.setDisable(false);
        westInc.setDisable(false);
        westDec.setDisable(false);
        southInc.setDisable(false);
        southDec.setDisable(false);
        eastInc.setDisable(false);
        eastDec.setDisable(false);
    }

    private void enableIncDecButtons(boolean enable) {
        if (northInc != null) northInc.setDisable(!enable);
        if (northDec != null) northDec.setDisable(!enable);
        if (southInc != null) southInc.setDisable(!enable);
        if (southDec != null) southDec.setDisable(!enable);
        if (eastInc != null)  eastInc.setDisable(!enable);
        if (eastDec != null)  eastDec.setDisable(!enable);
        if (westInc != null)  westInc.setDisable(!enable);
        if (westDec != null)  westDec.setDisable(!enable);
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

    // New method to handle vehicle count adjustments from buttons
    // Updated method to handle vehicle count adjustments from buttons
    public void adjustVehicleCount(String direction, int delta) {
        int current = vehicleDistribution.getOrDefault(direction, 0);
        int newCount = Math.max(0, current + delta);
        vehicleDistribution.put(direction, newCount);

        int existingInScene = (int) vehicles.stream()
                .filter(v -> v.getDirection().toString().equalsIgnoreCase(direction))
                .count();

        int toProduceLater = Math.max(0, newCount - 3);
        vehiclesToInc.put(direction, toProduceLater);

        if (delta > 0 && existingInScene < 3) {
            addVehicle(direction, false); // kırmızı da olsa 3'e kadar göster
        } else if (delta < 0 && current > 0) {
            removeVehicle(direction);
        }

        recalculateGreenDurations();
    }



    private void addVehicle(String direction, boolean forceGreenCheck) {
        String currentLight = lightStates.getOrDefault(direction, "RED");
        if (forceGreenCheck && !"GREEN".equals(currentLight)) return;

        VehicleType type = VehicleType.values()[(int) (Math.random() * VehicleType.values().length)];
        Node sprite;
        double x = 0, y = 0;
        Direction dir = Direction.valueOf(direction.toUpperCase());

        switch (dir) {
            case NORTH: x = 250; y = 0; break;
            case SOUTH: x = 310; y = 600; break;
            case EAST:  x = 600; y = 250; break;
            case WEST:  x = 0;   y = 320; break;
        }

        switch (type) {
            case TAXI: sprite = new TaxiView(x, y); break;
            case TRUCK: sprite = new TruckView(x, y); break;
            case BUS: sprite = new BusView(x, y); break;
            default: sprite = new TaxiView(x, y); break;
        }

        roadPane.getChildren().add(sprite);
        Vehicle vehicle = new Vehicle(x, y, type, sprite, dir);
        vehicle.setLightState(currentLight);
        vehicles.add(vehicle);
    }

    // Yeni metod: Belirtilen yönden bir aracı kaldırır
    private void removeVehicle(String direction) {
        Vehicle vehicleToRemove = vehicles.stream()
                .filter(v -> v.getDirection().toString().equals(direction))
                .findFirst()
                .orElse(null);

        if (vehicleToRemove != null) {
            roadPane.getChildren().remove(vehicleToRemove.getSprite());
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
            northInc.setDisable(true);
            northDec.setDisable(true);
            westInc.setDisable(true);
            westDec.setDisable(true);
            southInc.setDisable(true);
            southDec.setDisable(true);
            eastInc.setDisable(true);
            eastDec.setDisable(true);
            return;
        }

        if (timeline != null) {
            timeline.stop();
            vehicleTimeline.stop();
        }

        // Clear existing vehicles and repopulate based on vehiclesToInc
        vehicles.clear();
        roadPane.getChildren().removeIf(node -> node instanceof BusView || node instanceof TaxiView || node instanceof TruckView);
        vehiclesToInc.clear();
        for (Map.Entry<String, Integer> entry : vehicleDistribution.entrySet()) {
            vehiclesToInc.put(entry.getKey(), entry.getValue());
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
                    setAllRed(northLight, westLight, southLight, eastLight);
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
                setAllRed(northLight, westLight, southLight, eastLight);
                updateVehicleLightStates(null, "RED");
                elapsedTime = 0;
                startBtn.setDisable(false);
                assignVehiclesBtn.setDisable(false);
                stopBtn.setDisable(true);
                resetBtn.setDisable(false);
                northInc.setDisable(false);
                northDec.setDisable(false);
                westInc.setDisable(false);
                westDec.setDisable(false);
                southInc.setDisable(false);
                southDec.setDisable(false);
                eastInc.setDisable(false);
                eastDec.setDisable(false);
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
            northInc.setDisable(false);
            northDec.setDisable(false);
            westInc.setDisable(false);
            westDec.setDisable(false);
            southInc.setDisable(false);
            southDec.setDisable(false);
            eastInc.setDisable(false);
            eastDec.setDisable(false);
        });
        timeline.play();

        Timeline timeTracker = new Timeline(new KeyFrame(Duration.seconds(1), e -> elapsedTime += 1));
        timeTracker.setCycleCount(Timeline.INDEFINITE);
        timeTracker.play();

        startBtn.setDisable(true);
        assignVehiclesBtn.setDisable(true);
        stopBtn.setDisable(false);
        resetBtn.setDisable(false);
        northInc.setDisable(true);
        northDec.setDisable(true);
        westInc.setDisable(true);
        westDec.setDisable(true);
        southInc.setDisable(true);
        southDec.setDisable(true);
        eastInc.setDisable(true);
        eastDec.setDisable(true);
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
                            case NORTH: return Double.compare(v1.getY(), v2.getY());
                            case SOUTH: return Double.compare(v2.getY(), v1.getY());
                            case EAST: return Double.compare(v2.getX(), v1.getX());
                            case WEST: return Double.compare(v1.getX(), v2.getX());
                            default: return 0;
                        }
                    })
                    .collect(Collectors.toList());

            double lastPos = getStartPosition(dir);
            for (Vehicle v : dirVehicles) {
                double currPos = getPosition(v);
                double spacing = getVehicleSpacing(v.getType());

                if ("RED".equals(v.getLightState()) || "YELLOW".equals(v.getLightState())) {
                    v.setMoving(false);
                    if (currPos > lastPos + spacing) {
                        v.setMoving(true);
                    }
                } else {
                    v.setMoving(true);
                }

                if (v.isMoving()) {
                    v.update();
                    lastPos = currPos;
                }

                if (isOutOfRoad(v)) {
                    roadPane.getChildren().remove(v.getSprite());
                    vehicles.remove(v);
                    break;
                }
            }
        }
    }

    private void increaseVehicles() {
        for (String dir : new String[]{"North", "West", "South", "East"}) {
            int count = vehiclesToInc.getOrDefault(dir, 0);
            String lightState = lightStates.getOrDefault(dir, "RED");

            if ("GREEN".equals(lightState) && count > 0) {
                addVehicle(dir, true); // yeşil kontrolüyle ekle
                vehiclesToInc.put(dir, count - 1);
            }
        }
    }




    private void addVehicleInitial(String direction, int index) {
        VehicleType type = VehicleType.values()[(int) (Math.random() * VehicleType.values().length)];
        Node sprite;
        double x = 0, y = 0;
        Direction dir = Direction.valueOf(direction.toUpperCase());

        double offset = index * getVehicleSpacing(type);

        switch (dir) {
            case NORTH:
                x = 250;
                y = 0 - offset;
                break;
            case SOUTH:
                x = 310;
                y = 600 + offset;
                break;
            case EAST:
                x = 600 + offset;
                y = 250;
                break;
            case WEST:
                x = 0 - offset;
                y = 320;
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

        roadPane.getChildren().add(sprite);
        Vehicle vehicle = new Vehicle(x, y, type, sprite, dir);
        vehicle.setLightState("RED"); // kırmızıda bekliyor
        vehicles.add(vehicle);
    }




    private double getStartPosition(Direction dir) {
        switch (dir) {
            case NORTH: return 275;
            case SOUTH: return 325;
            case EAST: return 325;
            case WEST: return 275;
            default: return 0;
        }
    }

    private double getPosition(Vehicle v) {
        switch (v.getDirection()) {
            case NORTH: return v.getY();
            case SOUTH: return v.getY();
            case EAST: return v.getX();
            case WEST: return v.getX();
            default: return 0;
        }
    }

    private double getVehicleSpacing(VehicleType type) {
        switch (type) {
            case TAXI: return 35;
            case TRUCK: return 55;
            case BUS: return 45;
            default: return 35;
        }
    }

    private boolean isOutOfRoad(Vehicle v) {
        switch (v.getDirection()) {
            case NORTH: return v.getY() > 600;
            case SOUTH: return v.getY() < 0;
            case EAST: return v.getX() < 0;
            case WEST: return v.getX() > 600;
            default: return false;
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
            northInc.setDisable(true);
            northDec.setDisable(true);
            westInc.setDisable(true);
            westDec.setDisable(true);
            southInc.setDisable(true);
            southDec.setDisable(true);
            eastInc.setDisable(true);
            eastDec.setDisable(true);
        }
    }

    public void resetSimulation(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        if (timeline != null) {
            timeline.stop();
            vehicleTimeline.stop();
        }
        elapsedTime = 0;
        setAllRed(northLight, westLight, southLight, eastLight);
        resetAllTimers(northLight, westLight, southLight, eastLight);
        // Instead of resetting to 5, keep the current vehicleDistribution values
        vehicleDistribution.put("North", 0);
        vehicleDistribution.put("West", 0);
        vehicleDistribution.put("South", 0);
        vehicleDistribution.put("East", 0);
        vehiclesToInc.clear();
        for (Map.Entry<String, Integer> entry : vehicleDistribution.entrySet()) {
            vehiclesToInc.put(entry.getKey(), entry.getValue());
        }
        vehicles.clear();
        roadPane.getChildren().removeIf(node -> node instanceof BusView || node instanceof TaxiView || node instanceof TruckView);
        timeline = null;
        vehicleTimeline = null;
        startBtn.setDisable(false);
        assignVehiclesBtn.setDisable(false);
        stopBtn.setDisable(true);
        resetBtn.setDisable(false);
        northInc.setDisable(false);
        northDec.setDisable(false);
        westInc.setDisable(false);
        westDec.setDisable(false);
        southInc.setDisable(false);
        southDec.setDisable(false);
        eastInc.setDisable(false);
        eastDec.setDisable(false);
        recalculateGreenDurations();
    }

    private void setAllRed(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
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
            // Tüm araçların ışığını RED yap (örn. cycle sonunda)
            vehicles.forEach(v -> v.setLightState("RED"));
        } else {
            try {
                Direction dirEnum = Direction.valueOf(direction.toUpperCase()); // Enum'a çevir
                vehicles.stream()
                        .filter(v -> v.getDirection() == dirEnum) // Enum kıyaslaması
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