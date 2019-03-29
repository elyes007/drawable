package tn.disguisedtoast.drawable.detectionModule.testMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import preview.PreviewScene;
import preview.StartScene;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamStreamViewController;

import java.io.IOException;

public class Main extends Application implements StartScene.CameraButtonCallback {
    private  Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("CamStarter");
        primaryStage.setScene(new StartScene(this).getScene());primaryStage.setHeight(200);
        primaryStage.setWidth(400);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    @Override
    public void onButtonClicked(int webcamIndex) {
        if (webcamIndex != -1) {
            CamStreamViewController camStreamViewController = new CamStreamViewController();
            camStreamViewController.setIndex(webcamIndex);
            CamStreamViewController.showStage();

        }

    }
}
