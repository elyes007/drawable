package preview;

import com.github.sarxos.webcam.Webcam;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import test.WebCamInfo;

public class StartScene {
    private FlowPane topPane;
    private BorderPane root;
    private int webcamIndex = -1;
    private String cameraListPromptText = "Choose Camera";
    private Scene scene;

    public StartScene(CameraButtonCallback callback) {
        root = new BorderPane();
        topPane = new FlowPane();
        topPane.setAlignment(Pos.CENTER);
        topPane.setHgap(20);
        topPane.setOrientation(Orientation.HORIZONTAL);
        topPane.setPrefHeight(40);
        createTopPanel();

        FlowPane bottomPane = new FlowPane();
        bottomPane.setAlignment(Pos.CENTER);
        Button button = new Button("Next");
        bottomPane.getChildren().add(button);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                callback.onButtonClicked(webcamIndex);
            }
        });

        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.getChildren().add(topPane);
        flowPane.getChildren().add(bottomPane);

        root.setCenter(flowPane);
        scene = new Scene(root);
    }

    private void createTopPanel() {
        int webCamCounter = 0;
        Label lbInfoLabel = new Label("Select Your WebCam Camera");
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();

        topPane.getChildren().add(lbInfoLabel);
        for (Webcam webcam : Webcam.getWebcams()) {
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());
            options.add(webCamInfo);
            webCamCounter++;
        }
        ComboBox<WebCamInfo> cameraOptions = new ComboBox<WebCamInfo>();
        cameraOptions.setItems(options);
        cameraOptions.setPromptText(cameraListPromptText);
        cameraOptions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<WebCamInfo>() {
            @Override
            public void changed(ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
                if (arg2 != null) {
                    System.out.println("WebCam Index: " + arg2.getWebCamIndex() + ": WebCam Name:" + arg2.getWebCamName());
                    webcamIndex = arg2.getWebCamIndex();
                }
            }
        });
        topPane.getChildren().add(cameraOptions);
    }

    public Scene getScene() {
        return scene;
    }

    public interface CameraButtonCallback {
        void onButtonClicked(int webcamIndex);
    }
}
