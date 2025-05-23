package com.example.demo155.Model;

import com.example.demo155.Model.Direction;
import com.example.demo155.Model.VehicleType;
import com.example.demo155.controller.TrafficController;
import com.example.demo155.view.BusView;
import com.example.demo155.view.TaxiView;
import com.example.demo155.view.TruckView;
import com.example.demo155.view.TrafficView;
import javafx.scene.Node;

public class Vehicle {
    public int x, y;
    private boolean reachedIntersection = false;
    private Direction direction = Direction.DOWN;
    private VehicleType type;
    private Node sprite;
    private TrafficController controller;
    private String road;

    public Vehicle(int x, int y, VehicleType type, Node sprite, TrafficController controller) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.sprite = sprite;
        this.controller = controller;
        this.road = determineRoad(x, y);
        System.out.println("Vehicle created at (" + x + ", " + y + ") on road: " + road);
    }

    private String determineRoad(int x, int y) {
        // Yolların yaklaşık koordinat aralıkları
        if (y <= 250 && x >= 250 && x <= 350) return "North"; // Kuzey yolu
        if (x <= 250 && y >= 300 && y <= 400) return "West"; // Batı yolu
        if (y >= 350 && x >= 250 && x <= 350) return "South"; // Güney yolu
        if (x >= 350 && y >= 200 && y <= 300) return "East"; // Doğu yolu
        System.out.println("Warning: Could not determine road for (" + x + ", " + y + "), defaulting to North");
        return "North"; // Varsayılan
    }

    public boolean shouldStop() {
        // Trafik ışığı koordinatları (TrafficView ile eşleştirildi)
        double northLightX = 280.0, northLightY = 200.0;
        double westLightX = 250.0, westLightY = 370.0;
        double southLightX = 320.0, southLightY = 370.0;
        double eastLightX = 320.0, eastLightY = 200.0;
        double stopDistance = 30.0; // Daha geniş durma mesafesi

        String lightState = controller.getLightState(road);
        boolean isRedOrYellow = lightState.equals("RED") || lightState.equals("YELLOW");

        boolean shouldStop = false;
        switch (road) {
            case "North":
                if (Math.abs(y - northLightY) < stopDistance && Math.abs(x - northLightX) < stopDistance) {
                    shouldStop = isRedOrYellow;
                }
                break;
            case "West":
                if (Math.abs(x - westLightX) < stopDistance && Math.abs(y - westLightY) < stopDistance) {
                    shouldStop = isRedOrYellow;
                }
                break;
            case "South":
                if (Math.abs(y - southLightY) < stopDistance && Math.abs(x - southLightX) < stopDistance) {
                    shouldStop = isRedOrYellow;
                }
                break;
            case "East":
                if (Math.abs(x - eastLightX) < stopDistance && Math.abs(y - eastLightY) < stopDistance) {
                    shouldStop = isRedOrYellow;
                }
                break;
        }

        System.out.println("Vehicle at (" + x + ", " + y + ") on " + road + ", light: " + lightState + ", shouldStop: " + shouldStop);
        return shouldStop;
    }



    public void update() {
        // Öndeki araçla mesafe kontrolü
        boolean tooClose = false;
        for (Vehicle other : TrafficView.getVehicles()) { // TrafficView'dan araç listesine erişim
            if (other != this && other.getRoad().equals(road)) {
                double distance = Math.sqrt(Math.pow(x - other.getX(), 2) + Math.pow(y - other.getY(), 2));
                if (distance < 50) { // 50 piksel mesafe sınırı
                    tooClose = true;
                    break;
                }
            }
        }

        if (shouldStop() || tooClose) {
            System.out.println("Vehicle stopped at (" + x + ", " + y + ") on " + road + (tooClose ? " (too close)" : ""));
            if (sprite instanceof TaxiView) {
                ((TaxiView) sprite).updatePosition(x, y);
            } else if (sprite instanceof TruckView) {
                ((TruckView) sprite).updatePosition(x, y);
            } else if (sprite instanceof BusView) {
                ((BusView) sprite).updatePosition(x, y);
            }
            return;
        }

        if (!reachedIntersection) {
            switch (road) {
                case "North":
                    y += 2;
                    break;
                case "South":
                    y -= 2;
                    break;
                case "West":
                    x += 2;
                    break;
                case "East":
                    x -= 2;
                    break;
            }
            if (Math.abs(x - 300) < 50 && Math.abs(y - 300) < 50) {
                reachedIntersection = true;
                int random = (int) (Math.random() * 3);
                if (random == 0) direction = Direction.DOWN;
                else if (random == 1) direction = Direction.RIGHT;
                else direction = Direction.LEFT;
                System.out.println("Vehicle reached intersection, new direction: " + direction);
            }
        } else {
            switch (direction) {
                case DOWN: y += 2; break;
                case RIGHT: x += 2; break;
                case LEFT: x -= 2; break;
            }
        }

        if (sprite instanceof TaxiView) {
            ((TaxiView) sprite).updatePosition(x, y);
        } else if (sprite instanceof TruckView) {
            ((TruckView) sprite).updatePosition(x, y);
        } else if (sprite instanceof BusView) {
            ((BusView) sprite).updatePosition(x, y);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Node getSprite() {
        return sprite;
    }

    public VehicleType getType() {
        return type;
    }

    public String getRoad() {
        return road;
    }
}