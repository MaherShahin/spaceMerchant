module com.example.guigame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.spacemerchant to javafx.fxml;
    opens com.example.spacemerchant.Model to javafx.base;
    exports com.example.spacemerchant;
    exports com.example.spacemerchant.Controller;
    opens com.example.spacemerchant.Controller to javafx.fxml;
    exports com.example.spacemerchant.View;
    opens com.example.spacemerchant.View to javafx.fxml;
}