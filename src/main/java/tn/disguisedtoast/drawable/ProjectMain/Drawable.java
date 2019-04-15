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

    private static Application instance;

    public static Application getInstance() {
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        this.globalStage = primaryStage;
        Parent root = (new FXMLLoader(getClass().getResource("/layouts/homeLayouts/HomeLayout.fxml"))).load();
        primaryStage.setTitle("Drawable");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        primaryStage.show();
        height = primaryStage.getScene().getHeight();
        width = primaryStage.getScene().getWidth();
    }


}
