package com.example.spacemerchant;

import javafx.application.Application;
import javafx.css.Stylesheet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.text.html.StyleSheet;
import java.io.File;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1020, 768);
        scene.getStylesheets().add("styles.css");
        stage.setTitle("Space-merchant");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}