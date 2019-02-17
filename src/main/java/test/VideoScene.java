package test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacv.*;

import java.awt.image.BufferedImage;

public class VideoScene {
    private FlowPane bottomCameraControlPane;
    private BorderPane root;
    private ImageView imgWebCamCapturedImage;
    private boolean stopCamera = false;
    ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    private BorderPane webCamPane;
    private Button btnCamreaStop;
    private Button btnCamreaStart;
    private Frame frame;
    private int webcamIndex = -1;
    private Scene scene;
    private Thread thread;
    private FrameGrabber grabber;
    private BufferedImage bufferedFrame;

    public VideoScene(int cameraIndex){
        root = new BorderPane();
        webCamPane = new BorderPane();
        webCamPane.setStyle("-fx-background-color: #ccc;");
        imgWebCamCapturedImage = new ImageView();
        webCamPane.setCenter(imgWebCamCapturedImage);
        root.setCenter(webCamPane);
        bottomCameraControlPane = new FlowPane();
        bottomCameraControlPane.setOrientation(Orientation.HORIZONTAL);
        bottomCameraControlPane.setAlignment(Pos.CENTER);
        bottomCameraControlPane.setHgap(20);
        bottomCameraControlPane.setVgap(10);
        bottomCameraControlPane.setPrefHeight(40);
        createCameraControls();
        root.setBottom(bottomCameraControlPane);

        scene = new Scene(root);

        this.webcamIndex = cameraIndex;

        grabber = new VideoInputFrameGrabber(webcamIndex);
        try {
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        startWebCamStream();
    }


    protected void startWebCamStream() {
        stopCamera = false;
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                final OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
                final OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
                final Java2DFrameConverter paintConverter = new Java2DFrameConverter();
                try {
                    while (!stopCamera) {
                        if((frame = grabber.grab()) != null){
                            opencv_core.IplImage img = converter.convert(frame);
                            opencv_core.cvFlip(img, img, 1);
                            frame = grabberConverter.convert(img);
                            bufferedFrame = paintConverter.getBufferedImage(frame,1);
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

    private void createCameraControls() {
        btnCamreaStop = new Button();
        btnCamreaStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                stopWebCamCamera();
            }
        });
        btnCamreaStop.setText("Stop Camera");
        btnCamreaStart = new Button();
        btnCamreaStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                startWebCamCamera();
            }
        });
        btnCamreaStart.setDisable(true);
        btnCamreaStart.setText("Start Camera");
        bottomCameraControlPane.getChildren().add(btnCamreaStart);
        bottomCameraControlPane.getChildren().add(btnCamreaStop);
    }

    protected void startWebCamCamera() {
        stopCamera = false;
        startWebCamStream();
        btnCamreaStop.setDisable(false);
        btnCamreaStart.setDisable(true);
    }

    protected void stopWebCamCamera() {
        stopCamera = true;
        btnCamreaStart.setDisable(false);
        btnCamreaStop.setDisable(true);
    }

    public Scene getScene(){
        return scene;
    }

    public BufferedImage getBufferedFrame(){
        return bufferedFrame;
    }
}
