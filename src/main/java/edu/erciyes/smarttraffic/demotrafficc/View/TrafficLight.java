package edu.erciyes.smarttraffic.demotrafficc.View;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TrafficLight extends Group {
    private final Circle red, yellow, green;

    public enum State {
        RED, YELLOW, GREEN
    }

    private State currentState = State.RED;

    public TrafficLight(double x, double y) {
        Rectangle body = new Rectangle(20, 60);
        body.setArcWidth(10);
        body.setArcHeight(10);
        body.setFill(Color.BLACK);

        red = new Circle(10, Color.GRAY);
        red.setCenterX(10);
        red.setCenterY(10);

        yellow = new Circle(10, Color.GRAY);
        yellow.setCenterX(10);
        yellow.setCenterY(30);

        green = new Circle(10, Color.GRAY);
        green.setCenterX(10);
        green.setCenterY(50);

        this.setLayoutX(x);
        this.setLayoutY(y);
        this.getChildren().addAll(body, red, yellow, green);

        setState(State.RED);
    }

    public void setState(State state) {
        this.currentState = state;
        red.setFill(state == State.RED ? Color.RED : Color.GRAY);
        yellow.setFill(state == State.YELLOW ? Color.YELLOW : Color.GRAY);
        green.setFill(state == State.GREEN ? Color.GREEN : Color.GRAY);
    }

    public State getState() {
        return currentState;
    }
}
