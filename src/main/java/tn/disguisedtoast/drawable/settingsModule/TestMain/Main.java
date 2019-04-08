package tn.disguisedtoast.drawable.settingsModule.TestMain;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String args[]){
        launch(args);
    }

    public static Stage globalStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        globalStage = primaryStage;

        /*FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/SettingsView.fxml"));
        primaryStage.setScene(new Scene(loader.load()));
        //primaryStage.setResizable(false);
        primaryStage.show();*/
        //ettingsViewController.showStage(System.getProperty("user.dir") + "/src/main/RelatedFiles/generated_views/pages/page1");
    }
}
