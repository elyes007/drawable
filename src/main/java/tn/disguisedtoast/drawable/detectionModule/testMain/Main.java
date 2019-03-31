package tn.disguisedtoast.drawable.detectionModule.testMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import preview.CamChooserController;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamStreamViewController;

import java.io.IOException;

public class Main extends Application implements CamChooserController.CameraButtonCallback {
    public static Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        primaryStage.setTitle("CamStarter");
        primaryStage.setScene(new Scene(new CamChooserController(this).getRoot()));
        primaryStage.setHeight(200);
        primaryStage.setWidth(400);
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    @Override
    public void onButtonClicked(int webcamIndex) {
        if (webcamIndex != -1) {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/layouts/detectionViews/CamStreamView.fxml"));
                loader.load();
                loader.getLocation().openStream();
                CamStreamViewController controller = loader.getController();
                controller.init(webcamIndex);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
