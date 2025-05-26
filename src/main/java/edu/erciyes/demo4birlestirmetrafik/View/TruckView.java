package edu.erciyes.demo4birlestirmetrafik.View;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TruckView extends Group {
    private Rectangle body;
    private Circle wheel1, wheel2, wheel3;
    private Text label;

    public TruckView(double x, double y) {
        // Gövde
        body = new Rectangle(50, 25);
        body.setFill(Color.YELLOWGREEN);
        body.setX(0);
        body.setY(0);

        // Tekerlekler
        wheel1 = new Circle(6, Color.BLACK);
        wheel1.setCenterX(12);
        wheel1.setCenterY(30);

        wheel2 = new Circle(6, Color.BLACK);
        wheel2.setCenterX(25);
        wheel2.setCenterY(30);

        wheel3 = new Circle(6, Color.BLACK);
        wheel3.setCenterX(38);
        wheel3.setCenterY(30);

        // Label - yazı
        label = new Text("TRUCK");
        label.setFont(Font.font("Arial", 12));
        label.setFill(Color.WHITE);

        // Yazıyı gövdenin ortasına konumlandır
        double textX = (body.getWidth() - label.getLayoutBounds().getWidth()) / 2;
        double textY = (body.getHeight() + label.getLayoutBounds().getHeight()) / 2;
        label.setX(textX);
        label.setY(textY);

        // Grup içine ekle
        this.getChildren().addAll(body, label, wheel1, wheel2, wheel3);

        // Başlangıç konumu
        setLayoutX(x);
        setLayoutY(y);
    }

    public void updatePosition(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }
}