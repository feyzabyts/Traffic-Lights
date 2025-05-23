module edu.erciyes.smarttraffic.demotrafficc {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens edu.erciyes.smarttraffic.demotrafficc to javafx.fxml;
    exports edu.erciyes.smarttraffic.demotrafficc;
    exports edu.erciyes.smarttraffic.demotrafficc.View;


}