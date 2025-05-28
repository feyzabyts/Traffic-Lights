module edu.erciyes.demo4birlestirmetrafik {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.erciyes.demo4birlestirmetrafik to javafx.fxml;
    exports edu.erciyes.demo4birlestirmetrafik;
}