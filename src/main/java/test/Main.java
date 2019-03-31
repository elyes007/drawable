package test;

import com.github.sarxos.webcam.Webcam;
import javafx.application.Application;
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
import javafx.stage.Stage;

public class Main extends Application {
    private FlowPane topPane;
    private BorderPane root;
    private int webcamIndex = -1;
    private String cameraListPromptText = "Choose Camera";

    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Connecting WebCam Using Sarxos API");
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
                if(webcamIndex != -1){
                    /*VideoScene videoScene = new VideoScene(webcamIndex);
                    Scene scene = videoScene.getScene();
                    primaryStage.setScene(scene);
                    primaryStage.setHeight(700);
                    primaryStage.setWidth(700);
                    primaryStage.centerOnScreen();*/
                }
            }
        });

        FlowPane flowPane = new FlowPane();
        flowPane.setAlignment(Pos.CENTER);
        flowPane.getChildren().add(topPane);
        flowPane.getChildren().add(bottomPane);

        root.setCenter(flowPane);

        primaryStage.setScene(new Scene(root));
        primaryStage.setHeight(200);
        primaryStage.setWidth(400);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    private void createTopPanel() {
        int webCamCounter = 0;
        Label lbInfoLabel = new Label("Select Your WebCam Camera");
        ObservableList<WebCamInfo> options = FXCollections.observableArrayList();

        topPane.getChildren().add(lbInfoLabel);
        for(Webcam webcam:Webcam.getWebcams()){
            WebCamInfo webCamInfo = new WebCamInfo();
            webCamInfo.setWebCamIndex(webCamCounter);
            webCamInfo.setWebCamName(webcam.getName());
            options.add(webCamInfo);
            webCamCounter ++;
        }
        ComboBox<WebCamInfo> cameraOptions = new ComboBox<WebCamInfo>();
        cameraOptions.setItems(options);
        cameraOptions.setPromptText(cameraListPromptText);
        cameraOptions.getSelectionModel().selectedItemProperty().addListener(new  ChangeListener<WebCamInfo>() {
            @Override
            public void changed(ObservableValue<? extends WebCamInfo> arg0, WebCamInfo arg1, WebCamInfo arg2) {
                if (arg2 != null) {
                    System.out.println("WebCam Index: " + arg2.getWebCamIndex()+": WebCam Name:"+ arg2.getWebCamName());
                    webcamIndex = arg2.getWebCamIndex();
                }
            }
        });
        topPane.getChildren().add(cameraOptions);
    }
}
