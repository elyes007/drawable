package tn.disguisedtoast.drawable.detectionModule.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

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







    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setWebCamHolder();


    }

    public  void setWebCamHolder(){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/layouts/detectionViews/WebCamHolder.fxml"));
            Pane pane = loader.load();
            loader.getLocation().openStream();
            WebCamController webCamController = loader.getController();
            webCamController.init(index);
            camHolder.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void StartCamera() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/layouts/detectionViews/WebCamHolder.fxml"));
        Pane pane = loader.load();
        loader.getLocation().openStream();
        WebCamController webCamController = loader.getController();

       webCamController.startWebCamCamera();



    }

    private void StopCamera() throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/layouts/detectionViews/WebCamHolder.fxml"));
        Pane pane = loader.load();
        loader.getLocation().openStream();
        WebCamController webCamController = loader.getController();
        webCamController.stopWebCamCamera();




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

    public void setStopCamera(boolean stop){
        this.stopcamera = stop;

    }






}