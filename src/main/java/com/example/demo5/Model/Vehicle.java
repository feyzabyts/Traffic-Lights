package com.example.demo5.Model;

import com.example.demo5.View.BusView;
import com.example.demo5.View.TruckView;
import com.example.demo5.View.TaxiView;
import javafx.scene.Node;
import javafx.scene.Group;

public class Vehicle {
    private double x, y;
    private boolean reachedIntersection = false;
    private Direction direction;
    private VehicleType type;
    private Node sprite;
    private boolean isMoving = true;
    private double speed = 4.0; // Hız (isteğe bağlı olarak artırılabilir)
    private String lightState;

    public Vehicle(double x, double y, VehicleType type, Node sprite, Direction direction) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.sprite = sprite;
        this.direction = direction;
        this.lightState = "RED";
        setInitialRotation(); // Başlangıç rotasyonunu ayarla
    }

    // Başlangıç rotasyonunu yöne göre ayarlar
    private void setInitialRotation() {
        double angle = switch (direction) {
            case NORTH -> 0;
            case SOUTH -> 180;
            case EAST -> 270;
            case WEST -> 90;
        };
        setRotation(angle);
    }

    // Araç pozisyonunu ve yönünü günceller
    public void update() {
        if (!reachedIntersection) {
            // Kesişim noktasına ulaşana kadar mevcut yönde hareket et
            switch (direction) {
                case NORTH:
                    y += speed;
                    if (y >= 275) reachedIntersection = true;
                    break;
                case SOUTH:
                    y -= speed;
                    if (y <= 325) reachedIntersection = true;
                    break;
                case EAST:
                    x -= speed;
                    if (x <= 325) reachedIntersection = true;
                    break;
                case WEST:
                    x += speed;
                    if (x >= 275) reachedIntersection = true;
                    break;
            }
        } else {
            // Kesişim noktasına ulaşıldı, rastgele yön seç
            if (isMoving && "GREEN".equals(lightState)) {
                // Rastgele yön seçimi (düz, sola, sağa)
                if (!hasChosenNewDirection()) {
                    chooseNewDirection();
                }
                // Yeni yöne göre hareket et
                switch (direction) {
                    case NORTH: y += speed; break;
                    case SOUTH: y -= speed; break;
                    case EAST: x -= speed; break;
                    case WEST: x += speed; break;
                }
            }
        }

        // Sprite pozisyonunu güncelle
        if (sprite instanceof TaxiView) {
            ((TaxiView) sprite).updatePosition(x, y);
        } else if (sprite instanceof TruckView) {
            ((TruckView) sprite).updatePosition(x, y);
        } else if (sprite instanceof BusView) {
            ((BusView) sprite).updatePosition(x, y);
        }
    }

    // Yeni yönü rastgele seçer
    private boolean hasChosenDirection = false;
    private void chooseNewDirection() {
        if (hasChosenDirection) return;
        Direction[] possibleDirections = getPossibleDirections(direction);
        int randomIndex = (int) (Math.random() * 3); // 0: düz, 1: sola, 2: sağa
        direction = possibleDirections[randomIndex];
        setRotationForDirection(direction);
        hasChosenDirection = true; // Yön seçildi
    }

    // Yöne göre olası yönleri döndürür (düz, sola, sağa)
    private Direction[] getPossibleDirections(Direction currentDirection) {
        return switch (currentDirection) {
            case NORTH -> new Direction[]{Direction.NORTH, Direction.WEST, Direction.EAST};
            case SOUTH -> new Direction[]{Direction.SOUTH, Direction.EAST, Direction.WEST};
            case EAST -> new Direction[]{Direction.EAST, Direction.NORTH, Direction.SOUTH};
            case WEST -> new Direction[]{Direction.WEST, Direction.SOUTH, Direction.NORTH};
        };
    }

    // Yeni yöne göre rotasyonu ayarlar
    private void setRotationForDirection(Direction newDirection) {
        double angle = switch (newDirection) {
            case NORTH -> 0;
            case SOUTH -> 180;
            case EAST -> 270;
            case WEST -> 90;
        };
        setRotation(angle);
    }

    // Araç sprite’ının rotasyonunu ayarlar
    public void setRotation(double angle) {
        if (sprite instanceof Group) {
            ((Group) sprite).setRotate(angle);
        }
    }

    public void setLightState(String state) {
        this.lightState = state;
    }

    public String getLightState() {
        return lightState;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Node getSprite() {
        return sprite;
    }

    public VehicleType getType() {
        return type;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean hasReachedIntersection() {
        return reachedIntersection;
    }

    public boolean hasChosenNewDirection() {
        return hasChosenDirection;
    }

    public void setDirection(Direction newDirection) {
        this.direction = newDirection;
        setRotationForDirection(newDirection);
    }

    public void setY(double v) {
        this.y = v;
    }

    public void setX(double x) {
        this.x = x;
    }
}