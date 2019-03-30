package tn.disguisedtoast.drawable.detectionModule.controllers;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.VideoInputFrameGrabber;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WebCamController implements Initializable {



private BorderPane drawingPane;
    @FXML
    private BorderPane ImageHolder;

    @FXML
    private Button confirm;

    @FXML
    private Button back;

    @FXML
    private ImageView record_img;

    @FXML
    private ImageView stop_img1;

    @FXML
    void Back(ActionEvent event) {

    }

    @FXML
    void Confirm(ActionEvent event) {

    }





    private javafx.scene.image.ImageView imgWebCamCapturedImage;

    private Frame frame;
    private Thread thread;
    private Scene scene;
    private BorderPane root;
    private int webcamIndex = 0 ;
    private FrameGrabber grabber;
    private BufferedImage bufferedFrame;
    private boolean stopCamera = false;
    ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();


    //method to get file
    private File getFileFromImage() throws IOException {
        File file = new File("frame.jpg");
        ImageIO.write(bufferedFrame, "jpg", file);
        return file;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {



    }

    public void init(int index) {
        this.webcamIndex = index;
        imgWebCamCapturedImage = new ImageView();
        imgWebCamCapturedImage.setFitWidth(575);
        imgWebCamCapturedImage.setFitHeight(400);
        drawingPane = new BorderPane();
        drawingPane.setStyle("-fx-border-color: red;");
        drawingPane.setPrefHeight(575);
        drawingPane.setPrefWidth(400);
        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(imgWebCamCapturedImage);
        stackPane.getChildren().add(drawingPane);
        stackPane.setAlignment(Pos.CENTER);


        this.ImageHolder.setCenter(stackPane);

        System.out.println("we are here");

        grabber = new VideoInputFrameGrabber(index);
        try {
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        setCameraControls();
        startWebCamStream();

    }
    public  void setImageStream(){

        this.ImageHolder.getChildren().add(imgWebCamCapturedImage);
        System.out.println("setting");





    }
    public BorderPane getRoot() {
        return root;
    }

    protected void startWebCamStream() {
        System.out.println("started");
        stopCamera = false;



        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {

                final Java2DFrameConverter paintConverter = new Java2DFrameConverter();
                try {
                    while (!stopCamera) {
                        if ((frame = grabber.grab()) != null) {

                            bufferedFrame = paintConverter.getBufferedImage(frame, 1);


                            final Image mainImage = SwingFXUtils.toFXImage(bufferedFrame, null);
                            imageProperty.set(mainImage);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
        imgWebCamCapturedImage.imageProperty().bind(imageProperty);
        }



    public  void setindex(int index){

        this.webcamIndex = index;
    }

    protected void startWebCamCamera() {
        stopCamera = false;
        startWebCamStream();
        stop_img1.setVisible(true);
        record_img.setVisible(false);


    }

    protected void stopWebCamCamera() {
        stopCamera = true;
        stop_img1.setVisible(false);
        record_img.setVisible(true);


    }
    public void setCameraControls(){
        record_img.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                startWebCamCamera();

            }
        });

        stop_img1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                stopWebCamCamera();

            }
        });

    }


}