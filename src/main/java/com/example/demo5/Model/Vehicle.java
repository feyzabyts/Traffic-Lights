package com.example.demo5.Model;

import com.example.demo5.View.*;
import javafx.scene.Node;


public class Vehicle {
    private double x, y;
    private boolean reachedIntersection = false;
    private Direction direction;
    private VehicleType type;
    private Node sprite;
    private boolean isMoving = true;
    private double speed = 4.0;
    private String lightState;

    public Vehicle(double x, double y, VehicleType type, Node sprite, Direction direction) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.sprite = sprite;
        this.direction = direction;
        this.lightState = "RED";
    }

    public void update() {
        if (!reachedIntersection) {
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
            if (isMoving && "GREEN".equals(lightState)) {
                switch (direction) {
                    case NORTH: y += speed; break;
                    case SOUTH: y -= speed; break;
                    case EAST: x -= speed; break;
                    case WEST: x += speed; break;
                }
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

    public void setLightState(String state) {
        this.lightState = state;
    }

    public String getLightState() {
        return lightState;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public boolean haraketet() {
        return isMoving;
    }

    public double Xget() {
        return x;
    }

    public double Yget() {
        return y;
    }

    public Node getsprite() {
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
}