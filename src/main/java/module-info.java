module com.example.demo122 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo122 to javafx.fxml;
    exports com.example.demo122;
    exports com.example.demo122.viEW;
    exports com.example.demo122.Model;
}