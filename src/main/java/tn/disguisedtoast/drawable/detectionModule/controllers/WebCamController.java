package tn.disguisedtoast.drawable.detectionModule.controllers;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.WindowEvent;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.Box;
import tn.disguisedtoast.drawable.codeGenerationModule.ionic.models.DetectedObject;
import tn.disguisedtoast.drawable.utils.ImageViewPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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


    private ImageView imgWebCamCapturedImage;

    private Frame frame;
    private Thread thread;
    private int webcamIndex = 0;
    private FrameGrabber grabber;
    private BufferedImage bufferedFrame;
    private boolean stopCamera = false;
    ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    private boolean shouldUpload;
    private UploadInterface uploadInterface;
    private NavigationCallback navigationCallback;

    private RectAttributes[] rectsAttributes = new RectAttributes[]{
            new RectAttributes("edit_text", Color.AQUA),
            new RectAttributes("frame", Color.GOLD),
            new RectAttributes("button", Color.RED),
            new RectAttributes("image_view", Color.GREEN),
            new RectAttributes("menu", Color.PURPLE),
            new RectAttributes("text", Color.ORANGE),
    };
    private ImageViewPane imageViewPane;

    //method to get file
    public File getFileFromImage() throws IOException {
        if (bufferedFrame == null) return null;
        File file = new File("frame.jpg");
        ImageIO.write(bufferedFrame, "jpg", file);
        return file;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Drawable.globalStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                stopWebCamCamera();
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void init(int index, UploadInterface uploadInterface, NavigationCallback navigationCallback) {
        this.webcamIndex = index;
        this.uploadInterface = uploadInterface;
        this.navigationCallback = navigationCallback;
        imgWebCamCapturedImage = new ImageView();
        imgWebCamCapturedImage.setPreserveRatio(true);
        imageViewPane = new ImageViewPane(imgWebCamCapturedImage);
        drawingPane = new BorderPane();
        drawingPane.setStyle("-fx-border-color: red");
        StackPane stackPane = (StackPane) this.ImageHolder.getCenter();
        stackPane.getChildren().add(imageViewPane);
        stackPane.getChildren().add(drawingPane);

        System.out.println("we are here");

        setCameraControls();
        //startWebCamStream();
    }


    //method used to start stream
    protected void startWebCamStream() {
        System.out.println("started");
        stopCamera = false;
        if (grabber == null) {
            grabber = new OpenCVFrameGrabber(webcamIndex);
            try {
                grabber.start();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {

                final Java2DFrameConverter paintConverter = new Java2DFrameConverter();
                try {
                    while (!stopCamera) {
                        if (grabber != null && (frame = grabber.grab()) != null) {

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


    protected void startWebCamCamera() {
        record_img.setVisible(false);
        stop_img1.setVisible(true);
        new Thread(() -> {
            startWebCamStream();
            switchShouldUpload();
        }).start();
    }


    protected void stopWebCamCamera() {
        stop_img1.setVisible(false);
        record_img.setVisible(true);
        switchShouldUpload();
        stopCamera = true;
        new Thread(() -> {
            if (grabber != null) {
                try {
                    grabber.release();
                    grabber = null;
                } catch (FrameGrabber.Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //method used to handle on record and stop on click events
    public void setCameraControls() {
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


    //go back to home
    @FXML
    void Back(ActionEvent event) {
        navigationCallback.back();
    }

    // go to settings
    @FXML
    void Confirm(ActionEvent event) {
        navigationCallback.finish();
    }

    public boolean isShouldUpload() {
        return shouldUpload;
    }

    public void setShoudUpload(boolean shouldUpload) {
        this.shouldUpload = shouldUpload;
    }

    public void switchShouldUpload() {
        shouldUpload = !shouldUpload;
        if (shouldUpload) {
            uploadInterface.startUpload();
        }
    }

    public void drawObjects(List<DetectedObject> objects) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                drawingPane.getChildren().clear();
                double aspectRatio = imgWebCamCapturedImage.getImage().getWidth() / imgWebCamCapturedImage.getImage().getHeight();
                double realWidth = Math.min(imgWebCamCapturedImage.getFitWidth(), imgWebCamCapturedImage.getFitHeight() * aspectRatio);
                double realHeight = Math.min(imgWebCamCapturedImage.getFitHeight(), imgWebCamCapturedImage.getFitWidth() / aspectRatio);
                double xOffset = (drawingPane.getWidth() - realWidth) / 2;
                double yOffset = (drawingPane.getHeight() - realHeight) / 2;
                for (DetectedObject detectedObject : objects) {
                    Box box = detectedObject.getBox();
                    RectAttributes attributes = rectsAttributes[(int) detectedObject.getClasse() - 1];
                    Rectangle rectangle = new Rectangle(box.getxMin() * realWidth,
                            box.getyMin() * realHeight,
                            box.getWidth() * realWidth,
                            box.getHeight() * realHeight);
                    rectangle.setStrokeWidth(3);
                    rectangle.setFill(Color.TRANSPARENT);
                    rectangle.setStroke(attributes.color);

                    VBox toDrawPane = new VBox();
                    Label title = new Label(attributes.className);
                    title.setFont(new Font("Arial", 12));
                    title.setMinWidth(Region.USE_PREF_SIZE);
                    String cssRule = "-fx-background-color: #" + attributes.color.toString().substring(2, 8) + ";";
                    System.out.println(cssRule);
                    title.setStyle(cssRule);

                    toDrawPane.getChildren().add(title);
                    toDrawPane.getChildren().add(rectangle);

                    toDrawPane.setLayoutX(box.getxMin() * realWidth + xOffset);
                    toDrawPane.setLayoutY((box.getyMin() * realHeight) - 17 + yOffset);

                    drawingPane.getChildren().add(toDrawPane);
                }
            }
        });
    }

    public class RectAttributes {
        public String className;
        public Color color;

        public RectAttributes(String className, Color color) {
            this.className = className;
            this.color = color;
        }
    }

    public interface NavigationCallback {
        void finish();

        void back();
    }
}