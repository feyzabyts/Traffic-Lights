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
        // Sağdan başlama için x'i 20 birim sağa kaydırıyoruz
        double startX = x + 20;

        // Bir tık büyütülmüş gövde: 30x15
        body = new Rectangle(30, 15, Color.YELLOW);
        body.setX(startX);
        body.setY(y);

        // Tekerlekler biraz daha büyük, ve pozisyonlar gövdeye göre ayarlandı
        wheel1 = new Circle(5, Color.BLACK);
        wheel1.setCenterX(startX + 6);
        wheel1.setCenterY(y + 15 + 5); // gövdenin altına

        wheel2 = new Circle(5, Color.BLACK);
        wheel2.setCenterX(startX + 24);
        wheel2.setCenterY(y + 15 + 5);

        // Yazı büyütüldü: 9 pt
        label = new Text("TAXİ");
        label.setFont(Font.font("Arial", 9));
        label.setFill(Color.BLACK);

        double textX = startX + (body.getWidth() / 2) - (label.getLayoutBounds().getWidth() / 2);
        double textY = y + (body.getHeight() / 2) + (label.getLayoutBounds().getHeight() / 4);
        label.setX(textX);
        label.setY(textY);

        this.getChildren().addAll(body, label, wheel1, wheel2);
    }

    public void updatePosition(double x, double y) {
        double startX = x + 20;  // sağa kaydırma sabit olarak devam ediyor

        body.setX(startX);
        body.setY(y);

        wheel1.setCenterX(startX + 6);
        wheel1.setCenterY(y + 15 + 5);

        wheel2.setCenterX(startX + 24);
        wheel2.setCenterY(y + 15 + 5);

        double textX = startX + (body.getWidth() / 2) - (label.getLayoutBounds().getWidth() / 2);
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
        return horizontalRoad.getStrokeWidth();
    }
}
