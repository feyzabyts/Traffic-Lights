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
    private static final int TOTAL_CYCLE_DURATION = 120; // Toplam döngü süresi (saniye)
    private static final int YELLOW_DURATION = 3; // Sarı ışık süresi (saniye)
    private Map<String, Integer> vehicleDistribution; // Yön başına araç sayısı
    private Map<String, Integer> greenDurations; // Yön başına yeşil ışık süresi
    private Map<String, String> lightStates; // Yön başına trafik ışığı durumu
    private Timeline timeline; // Işıklar için zaman çizelgesi
    private Timeline vehicleTimeline; // Araçlar için zaman çizelgesi
    private double elapsedTime = 0; // Geçen süre
    private Button startBtn; // Başlat butonu
    private Button assignVehiclesBtn; // Araç ata butonu
    private Button stopBtn; // Durdur butonu
    private Button resetBtn; // Sıfırla butonu
    private List<Vehicle> vehicles = new ArrayList<>(); // Araç listesi
    private Map<String, Integer> vehiclesToSpawn; // Üretilecek araç sayısı
    private int vehicleSpawnTimer = 0; // Araç üretim zamanlayıcısı
    private Pane roadPane; // Yol paneli

    public TrafficController() {
        vehicleDistribution = new HashMap<>();
        greenDurations = new HashMap<>();
        lightStates = new HashMap<>();
        vehiclesToSpawn = new HashMap<>();
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

    // Butonları ayarlar
    public void setButtons(Button startBtn, Button assignVehiclesBtn, Button stopBtn, Button resetBtn) {
        this.startBtn = startBtn;
        this.assignVehiclesBtn = assignVehiclesBtn;
        this.stopBtn = stopBtn;
        this.resetBtn = resetBtn;
    }

    // Yol panelini ayarlar
    public void setRoadPane(Pane roadPane) {
        this.roadPane = roadPane;
    }

    // Araçları rastgele yönlere atar
    public void assignVehiclesRandomly() {
        int totalVehicles = (int) (20 + Math.random() * 61); // 20-80 arası rastgele araç
        vehicleDistribution.clear();
        vehiclesToSpawn.clear();
        String[] directions = {"North", "West", "South", "East"};
        int remaining = totalVehicles;
        for (int i = 0; i < directions.length - 1; i++) {
            int count = (int) (Math.random() * (remaining + 1));
            vehicleDistribution.put(directions[i], count);
            vehiclesToSpawn.put(directions[i], count);
            remaining -= count;
        }
        vehicleDistribution.put(directions[3], remaining);
        vehiclesToSpawn.put(directions[3], remaining);
        calculateGreenDurations();
    }

    // Yeşil ışık sürelerini hesaplar
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

    // Yeşil süreleri yeniden hesaplar
    public void recalculateGreenDurations() {
        calculateGreenDurations();
    }

    // Belirli bir yön için trafik ışığı nesnesi döndürür
    public TrafficLight getTrafficLightForDirection(String direction) {
        int greenDuration = greenDurations.getOrDefault(direction, 5);
        int redDuration = TOTAL_CYCLE_DURATION - greenDuration - YELLOW_DURATION;
        return new TrafficLight(greenDuration, redDuration, YELLOW_DURATION);
    }

    // Araç dağılımını döndürür
    public Map<String, Integer> getVehicleDistribution() {
        return vehicleDistribution;
    }

    // Toplam araç sayısını döndürür
    public int getTotalVehicleCount() {
        return vehicleDistribution.values().stream().mapToInt(Integer::intValue).sum();
    }

    // Simülasyonu başlatır
    public void startSimulation(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        if (timeline != null && timeline.getStatus() == Timeline.Status.PAUSED) {
            timeline.play();
            vehicleTimeline.play();
            updateAllTimers(northLight, westLight, southLight, eastLight);
            startBtn.setDisable(true);
            assignVehiclesBtn.setDisable(true);
            stopBtn.setDisable(false);
            resetBtn.setDisable(false);
            return;
        }

        if (timeline != null) {
            timeline.stop();
            vehicleTimeline.stop();
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
        });
        timeline.play();

        Timeline timeTracker = new Timeline(new KeyFrame(Duration.seconds(1), e -> elapsedTime += 1));
        timeTracker.setCycleCount(Timeline.INDEFINITE);
        timeTracker.play();

        startBtn.setDisable(true);
        assignVehiclesBtn.setDisable(true);
        stopBtn.setDisable(false);
        resetBtn.setDisable(false);
    }

    // Araçları günceller ve hareket ettirir
    private void updateVehicles() {
        vehicleSpawnTimer++;
        if (vehicleSpawnTimer >= 100) { // Her ~2 saniyede bir araç oluştur
            vehicleSpawnTimer = 0;
            spawnVehicles();
        }

        // Araç pozisyonlarını güncelle ve kuyruk kontrolü yap
        for (Direction dir : Direction.values()) {
            String direction = dir.toString();
            List<Vehicle> dirVehicles = vehicles.stream()
                    .filter(v -> v.getDirection().toString().equals(direction))
                    .sorted((v1, v2) -> {
                        switch (dir) {
                            case NORTH: return Double.compare(v1.getY(), v2.getY()); // Aşağı doğru sıralama
                            case SOUTH: return Double.compare(v2.getY(), v1.getY()); // Yukarı doğru sıralama
                            case EAST: return Double.compare(v2.getX(), v1.getX());  // Sola doğru sıralama
                            case WEST: return Double.compare(v1.getX(), v2.getX());  // Sağa doğru sıralama
                            default: return 0;
                        }
                    })
                    .collect(Collectors.toList());

            double lastPos = getStartPosition(dir);
            for (Vehicle v : dirVehicles) {
                double currPos = getPosition(v);
                double spacing = getVehicleSpacing(v.getType());

                // Araç yalnızca yeşil ışıkta hareket eder
                if ("GREEN".equals(v.getLightState())) {
                    v.setMoving(true);
                } else {
                    v.setMoving(false);
                }

                if (v.isMoving()) {
                    v.update();
                    lastPos = currPos;
                }

                // Yolun dışına çıkan araçları kaldır
                if (isOutOfRoad(v)) {
                    roadPane.getChildren().remove(v.getSprite());
                    vehicles.remove(v);
                    break; // Iterator güvenliği
                }
            }
        }
    }

    // Araçları üretir (yalnızca yeşil ışıkta)
    private void spawnVehicles() {
        String[] directions = {"North", "West", "South", "East"};
        for (String dir : directions) {
            // Yalnızca trafik ışığı yeşil ise araç üret
            if (!"GREEN".equals(lightStates.get(dir))) {
                continue;
            }

            int count = vehiclesToSpawn.getOrDefault(dir, 0);
            if (count > 0) {
                VehicleType type = VehicleType.values()[(int) (Math.random() * VehicleType.values().length)];
                Node sprite;
                double x = 0, y = 0;
                Direction direction = Direction.valueOf(dir.toUpperCase());

                // Şeritlere uygun başlangıç pozisyonları (soldan akış)
                switch (direction) {
                    case NORTH:
                        x = 250; // Sol taraf
                        y = 0;   // Yukarıdan başlar
                        break;
                    case SOUTH:
                        x = 310; // Sağ taraf
                        y = 600; // Aşağıdan başlar
                        break;
                    case EAST:
                        x = 600; // Sağdan başlar
                        y = 250; // Sol taraf
                        break;
                    case WEST:
                        x = 0;   // Soldan başlar
                        y = 310; // Sağ taraf
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
                Vehicle vehicle = new Vehicle(x, y, type, sprite, direction);
                vehicle.setLightState(lightStates.get(dir));
                vehicles.add(vehicle);
                vehiclesToSpawn.put(dir, count - 1);
            }
        }
    }

    // Yön için başlangıç pozisyonunu döndürür
    private double getStartPosition(Direction dir) {
        switch (dir) {
            case NORTH: return 275; // Kesişim noktası y
            case SOUTH: return 325;
            case EAST: return 325;
            case WEST: return 275;
            default: return 0;
        }
    }

    // Araç pozisyonunu döndürür
    private double getPosition(Vehicle v) {
        switch (v.getDirection()) {
            case NORTH: return v.getY();
            case SOUTH: return v.getY();
            case EAST: return v.getX();
            case WEST: return v.getX();
            default: return 0;
        }
    }

    // Araçlar arası mesafeyi döndürür
    private double getVehicleSpacing(VehicleType type) {
        switch (type) {
            case TAXI: return 35; // Araç boyutundan biraz büyük
            case TRUCK: return 55;
            case BUS: return 45;
            default: return 35;
        }
    }

    // Araç yolun dışında mı kontrol eder
    private boolean isOutOfRoad(Vehicle v) {
        switch (v.getDirection()) {
            case NORTH: return v.getY() > 600;  // Aşağı çıkar
            case SOUTH: return v.getY() < 0;    // Yukarı çıkar
            case EAST: return v.getX() < 0;     // Sola çıkar
            case WEST: return v.getX() > 600;   // Sağa çıkar
            default: return false;
        }
    }

    // Simülasyonu duraklatır
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
        }
    }

    // Simülasyonu sıfırlar
    public void resetSimulation(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        if (timeline != null) {
            timeline.stop();
            vehicleTimeline.stop();
        }
        elapsedTime = 0;
        setAllRed(northLight, westLight, southLight, eastLight);
        resetAllTimers(northLight, westLight, southLight, eastLight);
        vehicleDistribution.put("North", 5);
        vehicleDistribution.put("West", 5);
        vehicleDistribution.put("South", 5);
        vehicleDistribution.put("East", 5);
        vehiclesToSpawn.put("North", 5);
        vehiclesToSpawn.put("West", 5);
        vehiclesToSpawn.put("South", 5);
        vehiclesToSpawn.put("East", 5);
        recalculateGreenDurations();
        vehicles.clear();
        roadPane.getChildren().removeIf(node -> node instanceof BusView || node instanceof TaxiView || node instanceof TruckView);
        timeline = null;
        vehicleTimeline = null;
        startBtn.setDisable(false);
        assignVehiclesBtn.setDisable(false);
        stopBtn.setDisable(true);
        resetBtn.setDisable(false);
    }

    // Tüm ışıkları kırmızı yapar
    private void setAllRed(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        setLightState(northLight, "RED", "North");
        setLightState(westLight, "RED", "West");
        setLightState(southLight, "RED", "South");
        setLightState(eastLight, "RED", "East");
        updateVehicleLightStates(null, "RED");
    }

    // Trafik ışığı durumunu ayarlar
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

    // Araçların trafik ışığı durumlarını günceller
    private void updateVehicleLightStates(String direction, String state) {
        if (direction == null) {
            vehicles.forEach(v -> v.setLightState("RED"));
        } else {
            vehicles.stream()
                    .filter(v -> v.getDirection().toString().equals(direction))
                    .forEach(v -> v.setLightState(state));
        }
    }

    // Zamanlayıcıyı günceller
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

    // Tüm zamanlayıcıları günceller
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

    // Tüm zamanlayıcıları sıfırlar
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