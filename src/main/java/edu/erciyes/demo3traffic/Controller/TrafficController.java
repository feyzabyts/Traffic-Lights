package edu.erciyes.demo3traffic.Controller;

import edu.erciyes.demo3traffic.Model.TrafficLight;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrafficController {
    private static final int TOTAL_CYCLE_DURATION = 120;
    private static final int YELLOW_DURATION = 3;
    private Map<String, Integer> vehicleDistribution;
    private Map<String, Integer> greenDurations;
    private Timeline timeline;
    private double elapsedTime = 0;
    private Button startBtn;
    private Button assignVehiclesBtn;
    private Button stopBtn;
    private Button resetBtn;

    public TrafficController() {
        vehicleDistribution = new HashMap<>();
        greenDurations = new HashMap<>();
        vehicleDistribution.put("North", 0);
        vehicleDistribution.put("West", 0);
        vehicleDistribution.put("South", 0);
        vehicleDistribution.put("East", 0);
        calculateGreenDurations();
    }

    public void setButtons(Button startBtn, Button assignVehiclesBtn, Button stopBtn, Button resetBtn) {
        this.startBtn = startBtn;
        this.assignVehiclesBtn = assignVehiclesBtn;
        this.stopBtn = stopBtn;
        this.resetBtn = resetBtn;
    }

    public void assignVehiclesRandomly() {
        int totalVehicles = (int) (20 + Math.random() * 61);
        vehicleDistribution.clear();
        String[] directions = {"North", "West", "South", "East"};
        int remaining = totalVehicles;
        for (int i = 0; i < directions.length - 1; i++) {
            int count = (int) (Math.random() * (remaining + 1));
            vehicleDistribution.put(directions[i], count);
            remaining -= count;
        }
        vehicleDistribution.put(directions[3], remaining);
        calculateGreenDurations();
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

    public void startSimulation(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        if (timeline != null && timeline.getStatus() == Timeline.Status.PAUSED) {
            timeline.play();
            updateAllTimers(northLight, westLight, southLight, eastLight);
            startBtn.setDisable(true);
            assignVehiclesBtn.setDisable(true);
            stopBtn.setDisable(false);
            resetBtn.setDisable(false);
            return;
        }

        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline();
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
                    setLightState(light, "GREEN");
                    updateTimer(light, greenDuration);
                }));
                t += greenDuration;
            }

            if (t < 120) {
                frames.add(new KeyFrame(Duration.seconds(t - elapsedTime), e -> {
                    setLightState(light, "YELLOW");
                    updateTimer(light, yellowDuration);
                }));
                t += yellowDuration;
            }
        }

        if (t < 120) {
            frames.add(new KeyFrame(Duration.seconds(120 - elapsedTime), e -> {
                setAllRed(northLight, westLight, southLight, eastLight);
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

    public void pauseSimulation(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.pause();
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

    public void resetSimulation(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        if (timeline != null) {
            timeline.stop();
        }
        elapsedTime = 0;
        setAllRed(northLight, westLight, southLight, eastLight);
        resetAllTimers(northLight, westLight, southLight, eastLight);
        vehicleDistribution.put("North", 5);
        vehicleDistribution.put("West", 5);
        vehicleDistribution.put("South", 5);
        vehicleDistribution.put("East", 5);
        recalculateGreenDurations();
        timeline = null;
        startBtn.setDisable(false);
        assignVehiclesBtn.setDisable(false);
        stopBtn.setDisable(true);
        resetBtn.setDisable(false);
    }

    private void setAllRed(VBox northLight, VBox westLight, VBox southLight, VBox eastLight) {
        setLightState(northLight, "RED");
        setLightState(westLight, "RED");
        setLightState(southLight, "RED");
        setLightState(eastLight, "RED");
    }

    private void setLightState(VBox light, String state) {
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