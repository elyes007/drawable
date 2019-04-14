package tn.disguisedtoast.drawable.storyboardModule.tests;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestStoryboard extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = (new FXMLLoader(getClass().getResource("/layouts/storyboard/storyboardView.fxml"))).load();
        primaryStage.setTitle("Storyboard");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}
