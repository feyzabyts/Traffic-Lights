package com.example.demo155.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TruckView extends Group {
    private Rectangle body;
    private Circle wheel1, wheel2;

    public TruckView(double x, double y) {
        body = new Rectangle(60, 40);
        body.setFill(Color.DARKGRAY);
        body.setX(x);
        body.setY(y);

        wheel1 = new Circle(10, Color.BLACK);
        wheel1.setCenterX(x + 15);
        wheel1.setCenterY(y + 45);

        wheel2 = new Circle(10, Color.BLACK);
        wheel2.setCenterX(x + 45);
        wheel2.setCenterY(y + 45);

        this.getChildren().addAll(body, wheel1, wheel2);
    }

    public void updatePosition(double x, double y) {
        body.setX(x);
        body.setY(y);
        wheel1.setCenterX(x + 15);
        wheel1.setCenterY(y + 45);
        wheel2.setCenterX(x + 45);
        wheel2.setCenterY(y + 45);
    }
}