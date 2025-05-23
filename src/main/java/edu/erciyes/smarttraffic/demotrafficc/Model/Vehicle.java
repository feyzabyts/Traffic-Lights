package edu.erciyes.smarttraffic.demotrafficc.Model;

import edu.erciyes.smarttraffic.demotrafficc.View.BusView;
import edu.erciyes.smarttraffic.demotrafficc.View.TaxiView;
import edu.erciyes.smarttraffic.demotrafficc.View.TruckView;
import javafx.scene.Node;

public class Vehicle {
    public int x, y;
    private boolean reachedIntersection = false;
    private Direction direction = Direction.DOWN;
    private VehicleType type;
    private Node sprite;  // Artık genel Node

    public Vehicle(int x, int y, VehicleType type, Node sprite) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.sprite = sprite;
    }

    public void update() {
        if (!reachedIntersection) {
            y += 2;
            if (y + 5 >= 400) {
                reachedIntersection = true;
                int random = (int) (Math.random() * 3);
                if (random == 0) direction = Direction.DOWN;
                else if (random == 1) direction = Direction.RIGHT;
                else direction = Direction.LEFT;
            }
        } else {
            switch (direction) {
                case DOWN: y += 2; break;
                case RIGHT: x += 2; break;
                case LEFT: x -= 2; break;
            }
        }

        // Pozisyon güncelle
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
}