package preview;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class PreviewScene {

    private Scene scene;
    private final PreviewController previewController;

    public PreviewScene(int webcamIndex) throws IOException {
        AnchorPane root = new AnchorPane();
        root.setPrefWidth(1300);
        root.setPrefHeight(700);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/preview.fxml"));
        loader.load();
        loader.getLocation().openStream();
        previewController = loader.getController();
        root.getChildren().add(previewController.getRoot());
        previewController.getRoot().setLayoutX(850);
        previewController.getRoot().setLayoutY(30);

        CameraController cameraController = new CameraController(webcamIndex);
        cameraController.setPreviewController(previewController);
        root.getChildren().add(cameraController.getRoot());
        cameraController.getRoot().setLayoutX(0);
        cameraController.getRoot().setLayoutY(0);

        scene = new Scene(root);
    }

    public void start() {
        //previewController.start();
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }
}
