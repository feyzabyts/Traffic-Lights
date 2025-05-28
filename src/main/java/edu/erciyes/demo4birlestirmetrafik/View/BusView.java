package edu.erciyes.demo4birlestirmetrafik.View;

import javafx.scene.paint.Color;

public class BusView extends VehicleView {
    public BusView(double x, double y) {
        super(x, y,
                40, 20, Color.ORANGE,      // Gövde: genişlik, yükseklik, renk
                "BUS", 12, Color.BLACK,    // Etiket: metin, font boyutu, renk
                3, 5, 23);                 // Tekerlek: sayı, yarıçap, y konumu
    }
}