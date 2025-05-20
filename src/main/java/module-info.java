module edu.erciyes.demo3traffic {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.erciyes.demo3traffic to javafx.fxml;
    exports edu.erciyes.demo3traffic;
    //exports edu.erciyes.demo3traffic.Model;
    exports edu.erciyes.demo3traffic.Controller;
}