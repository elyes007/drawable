package tn.disguisedtoast.drawable.detectionModule.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.generation.CodeGenerator;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.DetectedObject;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.FailedToCreateHtmlFromIonApp;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.NoDetectedObjects;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.exceptions.NoFramesDetected;
import tn.disguisedtoast.drawable.codeGenerationModule.shapeDetection.ShapeDetectionService;
import tn.disguisedtoast.drawable.detectionModule.testMain.Main;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CamStreamViewController implements Initializable, UploadInterface {

    @FXML
    public BorderPane root;
    @FXML
    private StackPane camHolder;
    @FXML
    private AnchorPane previewHolder;

    private WebCamController webCamController;
    private Date lastRequestDate;
    private ShapeDetectionService.UploadCallback mUploadCallback = new ShapeDetectionService.UploadCallback() {
        @Override
        public void onUploaded(List<DetectedObject> objects) {
            try {
                webCamController.drawObjects(objects);

                String htmlPath = CodeGenerator.generateTempHtml(CodeGenerator.parse(objects));

                File file = webCamController.getFileFromImage();
                long timeDiff = new Date().getTime() - lastRequestDate.getTime();
                if (!webCamController.isShouldUpload()) {
                    return;
                }
                if (timeDiff < 2000) {
                    Thread.sleep(2000 - timeDiff);
                }
                ShapeDetectionService.upload(file, mUploadCallback);
                lastRequestDate = new Date();
            } catch (IOException | NoDetectedObjects | NoFramesDetected | FailedToCreateHtmlFromIonApp e) {
                e.printStackTrace();
                System.out.println("creating file failed");
            } catch (JAXBException e) {
                e.printStackTrace();
                System.out.println("marshalling layout failed");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Sleeping interrupted");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    };

    private BorderPane drawingPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void init(int camIndex) {
        setWebCamHolder(camIndex);
        Main.primaryStage.setScene(new Scene(root));
        Main.primaryStage.sizeToScene();
    }

    //used to call webcamstream into preview holder
    public void setWebCamHolder(int camIndex) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/layouts/detectionViews/WebCamHolder.fxml"));
            Pane pane = loader.load();
            loader.getLocation().openStream();
            webCamController = loader.getController();
            webCamController.init(camIndex, this);
            camHolder.getChildren().add(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //used to call preview into preview holder
    public void setPreviewHolder(){


    }

    @Override
    public void startUpload() {
        try {
            File file = webCamController.getFileFromImage();
            ShapeDetectionService.upload(file, mUploadCallback);
            lastRequestDate = new Date();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}