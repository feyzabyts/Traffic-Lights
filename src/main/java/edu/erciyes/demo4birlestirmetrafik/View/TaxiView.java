package edu.erciyes.demo4birlestirmetrafik.View;

import javafx.scene.paint.Color;

public class TaxiView extends VehicleView {
    public TaxiView(double x, double y) {
        super(x, y,
                30, 15, Color.YELLOW,      // Gövde: genişlik, yükseklik, renk
                "TAXI", 10, Color.BLACK,   // Etiket: metin, font boyutu, renk
                2, 5, 18);                 // Tekerlek: sayı, yarıçap, y konumu
    }
}