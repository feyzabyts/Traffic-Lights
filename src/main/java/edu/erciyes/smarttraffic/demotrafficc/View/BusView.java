package edu.erciyes.smarttraffic.demotrafficc.View;

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
        // Gövde - daha uzun ve yüksek
        body = new Rectangle(100, 50);
        body.setFill(Color.ORANGE);
        body.setX(0);
        body.setY(0);

        // Tekerlekler
        wheel1 = new Circle(12, Color.BLACK);
        wheel1.setCenterX(20);
        wheel1.setCenterY(55);

        wheel2 = new Circle(12, Color.BLACK);
        wheel2.setCenterX(50);
        wheel2.setCenterY(55);

        wheel3 = new Circle(12, Color.BLACK);
        wheel3.setCenterX(80);
        wheel3.setCenterY(55);

        // Label - yazı
        label = new Text("BUS");
        label.setFont(Font.font("Arial", 18));
        label.setFill(Color.BLACK);

        // Yazıyı gövdenin ortasına konumlandır (local koordinatlar)
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

    public void updatePosition(int x, int y) {
        setLayoutX(x);
        setLayoutY(y);
    }
}
