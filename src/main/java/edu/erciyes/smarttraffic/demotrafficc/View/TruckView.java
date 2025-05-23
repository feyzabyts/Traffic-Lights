package edu.erciyes.smarttraffic.demotrafficc.View;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class TruckView extends Group {
    private Rectangle body;
    private Circle wheel1, wheel2;

    public TruckView(double x, double y) {
        // Gövde
        body = new Rectangle(40, 20);
        body.setFill(Color.DARKGRAY);
        body.setX(11);
        body.setY(0);

        // Tekerlekler
        wheel1 = new Circle(7, Color.BLACK);
        wheel1.setCenterX(20);
        wheel1.setCenterY(26);

        wheel2 = new Circle(7, Color.BLACK);
        wheel2.setCenterX(43);
        wheel2.setCenterY(26);

        // TruckView Group'a ekle
        this.getChildren().addAll(body, wheel1, wheel2);

        // Başlangıç konumu
        setLayoutX(x);
        setLayoutY(y);
    }

    public void updatePosition(int x, int y) {
        setLayoutX(x);
        setLayoutY(y);
    }
}
