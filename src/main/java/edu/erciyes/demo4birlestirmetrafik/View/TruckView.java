package edu.erciyes.demo4birlestirmetrafik.View;

import javafx.scene.paint.Color;

public class TruckView extends VehicleView {
    public TruckView(double x, double y) {
        super(x, y,
                50, 25, Color.YELLOWGREEN, // Gövde: genişlik, yükseklik, renk
                "TRUCK", 12, Color.BLACK,  // Etiket: metin, font boyutu, renk
                3, 6, 30);                 // Tekerlek: sayı, yarıçap, y konumu
    }
}