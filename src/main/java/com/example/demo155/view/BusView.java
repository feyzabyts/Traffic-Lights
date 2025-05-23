package com.example.demo155.view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BusView extends Group {
    private Rectangle body;
    private Circle wheel1, wheel2, wheel3;
    private Text label;

    public BusView(double x, double y) {
        body = new Rectangle(100, 50);
        body.setFill(Color.ORANGE);
        body.setX(x);
        body.setY(y);

        wheel1 = new Circle(12, Color.BLACK);
        wheel1.setCenterX(x + 20);
        wheel1.setCenterY(y + 55);

        wheel2 = new Circle(12, Color.BLACK);
        wheel2.setCenterX(x + 50);
        wheel2.setCenterY(y + 55);

        wheel3 = new Circle(12, Color.BLACK);
        wheel3.setCenterX(x + 80);
        wheel3.setCenterY(y + 55);

        label = new Text("BUS");
        label.setFont(Font.font("Arial", 18));
        label.setFill(Color.BLACK);

        double textX = x + (body.getWidth() - label.getLayoutBounds().getWidth()) / 2;
        double textY = y + (body.getHeight() + label.getLayoutBounds().getHeight()) / 2;
        label.setX(textX);
        label.setY(textY);

        this.getChildren().addAll(body, label, wheel1, wheel2, wheel3);
    }

    public void updatePosition(double x, double y) {
        body.setX(x);
        body.setY(y);
        wheel1.setCenterX(x + 20);
        wheel1.setCenterY(y + 55);
        wheel2.setCenterX(x + 50);
        wheel2.setCenterY(y + 55);
        wheel3.setCenterX(x + 80);
        wheel3.setCenterY(y + 55);
        double textX = x + (body.getWidth() - label.getLayoutBounds().getWidth()) / 2;
        double textY = y + (body.getHeight() + label.getLayoutBounds().getHeight()) / 2;
        label.setX(textX);
        label.setY(textY);
    }
}