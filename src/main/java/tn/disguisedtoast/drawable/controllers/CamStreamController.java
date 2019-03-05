package tn.disguisedtoast.drawable.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class CamStreamController implements Initializable {

    private boolean stopcamera ;


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

        record_img.addEventHandler(MouseEvent.MOUSE_CLICKED , event -> {

            System.out.println("recording");
            StartCamera();
            event.consume();
        });

        stop_img.addEventHandler(MouseEvent.MOUSE_CLICKED , event -> {

            System.out.println("stop");
            StopCamera();
            event.consume();
        });


    }



    private  void StartCamera(){

        stopcamera = false;
        record_img.setVisible(false);
        stop_img.setVisible(true);


    }
    private void StopCamera(){

        stopcamera = false;
        record_img.setVisible(true);
        stop_img.setVisible(false);


    }
}
