module com.example.guigame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.guigame to javafx.fxml;
    opens com.example.guigame.Model to javafx.base;
    exports com.example.guigame;
}