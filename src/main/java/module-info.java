module com.example.demo155 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo155 to javafx.fxml;
    exports com.example.demo155;
    exports com.example.demo155.Model;
    exports com.example.demo155.view;
    exports com.example.demo155.controller;
    opens com.example.demo155.controller to javafx.fxml;

}