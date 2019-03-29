package tn.disguisedtoast.drawable.detectionModule.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import preview.CameraController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CamStreamViewController implements Initializable {

    private Stage camStream;
    private boolean stopcamera;
    private Scene scene;
    private    WebCamController webCamController;
    private int index;



    public static void showStage(){

        try{
            FXMLLoader loader = new FXMLLoader(CamStreamViewController.class.getResource("/layouts/detectionViews/CamStreamView.fxml"));
            Pane pane = loader.load();
            CamStreamViewController camStreamViewController = loader.getController();

            camStreamViewController.camStream = new Stage();
            camStreamViewController.camStream.setScene(new Scene(pane));

            camStreamViewController.camStream.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @FXML
    private StackPane camHolder;

    @FXML
    private ImageView camera;

    @FXML
    private Button back;

    @FXML
    private Button confirm;

    @FXML
    private ImageView record_img;

    @FXML
    private ImageView stop_img;

    @FXML
    void BackAction(ActionEvent event) {

    }

    @FXML
    void ConfirmAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setWebCamHolder();




    }

    public  void setWebCamHolder(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/detectionViews/WebCamHolder.fxml"));
        try {
            WebCamController webCamController = new WebCamController(index);
            webCamController.setindex(index);
            Pane pane = loader.load();

            camHolder.getChildren().add(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void StartCamera() {

        stopcamera = false;
        record_img.setVisible(false);
        stop_img.setVisible(true);


    }

    private void StopCamera() {

        stopcamera = false;
        record_img.setVisible(true);
        stop_img.setVisible(false);


    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void  setIndex(int index){
        this.index = index;
    }


}

