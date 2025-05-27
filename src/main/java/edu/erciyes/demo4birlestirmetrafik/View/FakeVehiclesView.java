package edu.erciyes.demo4birlestirmetrafik.View;

import javafx.scene.paint.Color;
import java.util.Random;

public class FakeVehiclesView extends VehicleView {
    public FakeVehiclesView(double x, double y) {
        super(x, y,
                40, 15, Color.BISQUE,      // Gövde: genişlik, yükseklik, renk
                "V", 10, Color.BLACK,   // Etiket: metin, font boyutu, renk
                2, 6, 19);                 // Tekerlek: sayı, yarıçap, y konumu
    }
}