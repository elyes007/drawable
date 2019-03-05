package tn.disguisedtoast.drawable.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;
import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = new File("src/main/java/tn/disguisedtoast/drawable/views/CamStreamLayout.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("Drawable");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
