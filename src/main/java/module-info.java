module com.example.demo5 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo5 to javafx.fxml;
    exports com.example.demo5;
    exports com.example.demo5.View;
    exports com.example.demo5.Controller;
    exports com.example.demo5.Model;
    opens com.example.demo5.Controller to javafx.fxml;
}