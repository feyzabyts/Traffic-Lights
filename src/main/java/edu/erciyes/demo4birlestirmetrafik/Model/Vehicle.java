package edu.erciyes.demo4birlestirmetrafik.Model;

import edu.erciyes.demo4birlestirmetrafik.View.BusView;
import edu.erciyes.demo4birlestirmetrafik.View.TaxiView;
import edu.erciyes.demo4birlestirmetrafik.View.TruckView;
import javafx.scene.Node;


public class Vehicle {
    private double x, y;
    private boolean isReachedIntersection = false; //Kavşağa ulaşma
    private Direction direction;
    private VehicleType type;
    private Node sprite; //yok olma araçlar kayboluyor o yüzden ekledim.
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
        if (!isReachedIntersection) {
            switch (direction) {
                case NORTH:
                    y += speed;
                    if (y >= 275) isReachedIntersection = true;
                    break;
                case SOUTH:
                    y -= speed;
                    if (y <= 325) isReachedIntersection = true;
                    break;
                case EAST:
                    x -= speed;
                    if (x <= 325) isReachedIntersection = true;
                    break;
                case WEST:
                    x += speed;
                    if (x >= 275) isReachedIntersection = true;
                    break;
            }
        } else {
            if (isMoving && ("GREEN".equals(lightState) || "YELLOW".equals(lightState))) {
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

    public boolean isMoving() {
        return isMoving;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double setX(double x) {
        this.x = x;
        return x;
    }
    public double setY(double y) {
        this.y = y;
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

}