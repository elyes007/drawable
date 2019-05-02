package tn.disguisedtoast.drawable.ProjectMain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Drawable extends javafx.application.Application {
    public static Stage globalStage;
    public static double height;
    public static double width;
    public static String projectPath;

    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        globalStage = primaryStage;
        Parent root = (new FXMLLoader(getClass().getResource("/layouts/projectGenerationViews/startView.fxml"))).load();
        globalStage.setTitle("Drawable");
        globalStage.setScene(new Scene(root));
        // primaryStage.setMaximized(true);
        globalStage.show();
        height = globalStage.getScene().getHeight();
        width = globalStage.getScene().getWidth();
    }
}
