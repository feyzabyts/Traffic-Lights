package edu.erciyes.smarttraffic.demotrafficc.View;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TaxiView extends Group {

    private Rectangle body;
    private Circle wheel1, wheel2;
    private Text label;
    private Line verticalRoad, horizontalRoad;

    public TaxiView(double x, double y) {
        double centerX = 300;  // Örnek değer, sahneye göre ayarlayabilirsin
        double centerY = 300;

        // Aracın gövdesi (dikdörtgen)
        body = new Rectangle(60, 30, Color.YELLOW);
        body.setX(x);
        body.setY(y);

        // Tekerlekler (daireler)
        wheel1 = new Circle(10, Color.BLACK);
        wheel1.setCenterX(x + 15);
        wheel1.setCenterY(y + 35);

        wheel2 = new Circle(10, Color.BLACK);
        wheel2.setCenterX(x + 45);
        wheel2.setCenterY(y + 35);

        // TAXI yazısı
        label = new Text("TAXİ");
        label.setFont(Font.font("Arial", 18));
        label.setFill(Color.BLACK);

        // Yazıyı body'nin içine ortalamak için konumlandır
        double textX = x + (body.getWidth() / 2) - (label.getLayoutBounds().getWidth() / 2);
        double textY = y + (body.getHeight() / 2) + (label.getLayoutBounds().getHeight() / 4);
        label.setX(textX);
        label.setY(textY);

        // Grup içine ekle
        this.getChildren().addAll(body, label, wheel1, wheel2);

    }

    public void updatePosition(double x, double y) {
        body.setX(x);
        body.setY(y);

        wheel1.setCenterX(x + 15);
        wheel1.setCenterY(y + 35);

        wheel2.setCenterX(x + 45);
        wheel2.setCenterY(y + 35);

        double textX = x + (body.getWidth() / 2) - (label.getLayoutBounds().getWidth() / 2);
        double textY = y + (body.getHeight() / 2) + (label.getLayoutBounds().getHeight() / 4);
        label.setX(textX);
        label.setY(textY);
    }

    public double getBodyX() {
        return body.getX();
    }

    public double getBodyWidth() {
        return body.getWidth();
    }

    public double getRoadWidth() {
        return horizontalRoad.getStrokeWidth();  // veya verticalRoad.getStrokeWidth()
    }
}
