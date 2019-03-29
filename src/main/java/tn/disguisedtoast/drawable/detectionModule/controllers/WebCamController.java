package tn.disguisedtoast.drawable.detectionModule.controllers;

import code_generation.service.ShapeDetectionService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
import java.util.Date;
import java.util.ResourceBundle;

public class WebCamController implements Initializable {

    @FXML
    private StackPane holder;




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

    public  WebCamController(int index){

        imgWebCamCapturedImage = new ImageView();
        imgWebCamCapturedImage.setFitWidth(355);
        imgWebCamCapturedImage.setFitHeight(412);
        this.holder.getChildren().add(imgWebCamCapturedImage);

        System.out.println("we are here");

        grabber = new VideoInputFrameGrabber(index);
        try {
            grabber.start();
        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        startWebCamStream();

    }
    public  void setImageStream(){

        holder.getChildren().add(imgWebCamCapturedImage);
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
                //final OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
                //final OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
                final Java2DFrameConverter paintConverter = new Java2DFrameConverter();
                try {
                    while (!stopCamera) {
                        if ((frame = grabber.grab()) != null) {
                            //opencv_core.IplImage img = converter.convert(frame);
                            //opencv_core.cvFlip(img, img, 1);
                            //frame = grabberConverter.convert(img);
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


}
