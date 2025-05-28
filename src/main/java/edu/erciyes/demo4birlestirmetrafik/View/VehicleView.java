package edu.erciyes.demo4birlestirmetrafik.View;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public abstract class VehicleView extends Group {
    protected Rectangle body;
    protected List<Circle> wheels;
    protected Text label;

    public VehicleView(double x, double y, double bodyWidth, double bodyHeight, Color bodyColor,
                   String labelText, double fontSize, Color labelColor, int wheelCount,
                   double wheelRadius, double wheelY) {
        // Gövde
        body = new Rectangle(bodyWidth, bodyHeight);
        body.setFill(bodyColor);
        body.setX(0);
        body.setY(0);

        // Tekerlekler
        wheels = new ArrayList<>();
        double wheelSpacing = bodyWidth / (wheelCount + 1);
        for (int i = 0; i < wheelCount; i++) {
            Circle wheel = new Circle(wheelRadius, Color.BLACK);
            wheel.setCenterX(wheelSpacing * (i + 1));
            wheel.setCenterY(wheelY);
            wheels.add(wheel);
        }

        // Etiket
        label = new Text(labelText);
        label.setFont(Font.font("Arial", fontSize));
        label.setFill(labelColor);

        // Yazıyı gövdenin ortasına konumlandır
        double textX = (body.getWidth() - label.getLayoutBounds().getWidth()) / 2;
        double textY = (body.getHeight() + label.getLayoutBounds().getHeight()) / 2;
        label.setX(textX);
        label.setY(textY);

        // Grup içine ekle
        this.getChildren().addAll(body, label);
        this.getChildren().addAll(wheels);

        // Başlangıç konumu
        setLayoutX(x);
        setLayoutY(y);
    }

    public void updatePosition(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }
}