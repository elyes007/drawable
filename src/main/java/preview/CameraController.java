package preview;

import code_generation.entities.Box;
import code_generation.entities.DetectedObject;
import code_generation.service.CodeGenerator;
import code_generation.service.ShapeDetectionService;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.VideoInputFrameGrabber;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class CameraController {
    public class RectAttributes{
        public String className;
        public Color color;

        public RectAttributes(String className, Color color){
            this.className = className;
            this.color = color;
        }
    }

    private FlowPane bottomCameraControlPane;
    private BorderPane root;
    private ImageView imgWebCamCapturedImage;
    private boolean stopCamera = false;
    ObjectProperty<Image> imageProperty = new SimpleObjectProperty<Image>();
    private FlowPane webCamPane;
    private Button btnCamreaStop;
    private Button btnCamreaStart;
    private Frame frame;
    private int webcamIndex = -1;
    private Thread thread;
    private FrameGrabber grabber;
    private BufferedImage bufferedFrame;
    private BorderPane drawingPane;
    private RectAttributes[] rectsAttributes = new RectAttributes[]{
            new RectAttributes("edit_text", Color.AQUA),
            new RectAttributes("frame", Color.YELLOW),
            new RectAttributes("button", Color.RED),
            new RectAttributes("image_view", Color.GREEN)
    };

    private Date lastRequestDate;
    private PreviewController previewController;
    private ShapeDetectionService.UploadCallback mUploadCallback = new ShapeDetectionService.UploadCallback() {
        @Override
        public void onUploaded(List<DetectedObject> objects) {
            try {
                Platform.runLater(new Runnable(){
                    @Override
                    public void run() {
                        drawingPane.getChildren().clear();
                        for(DetectedObject detectedObject : objects){
                            Box box = detectedObject.getBox();
                            RectAttributes attributes = rectsAttributes[(int)detectedObject.getClasse()-1];
                            Rectangle rectangle = new Rectangle(box.getxMin()*640,box.getyMin()*480, box.getWidth()*640, box.getHeight()*480);
                            rectangle.setStrokeWidth(3);
                            rectangle.setFill(Color.TRANSPARENT);
                            rectangle.setStroke(attributes.color);

                            VBox toDrawPane = new VBox();
                            Label title = new Label(attributes.className);
                            title.setFont(new Font("Arial", 12));
                            title.setMinWidth(Region.USE_PREF_SIZE);
                            String cssRule = "-fx-background-color: #"+attributes.color.toString().substring(2,8)+";";
                            System.out.println(cssRule);
                            title.setStyle(cssRule);

                            toDrawPane.getChildren().add(title);
                            toDrawPane.getChildren().add(rectangle);

                            toDrawPane.setLayoutX(box.getxMin()*640);
                            toDrawPane.setLayoutY((box.getyMin()*480)-17);


                            drawingPane.getChildren().add(toDrawPane);
                        }
                    }
                });

                CodeGenerator.ParseResult result = CodeGenerator.parse(objects);
                if (result != null) {
                    previewController.update(result);
                    CodeGenerator.generateLayoutFile(result.getLayout());
                }
                File file = getFileFromImage();
                long timeDiff = new Date().getTime() - lastRequestDate.getTime();
                if (timeDiff < 2000) {
                    Thread.sleep(2000 - timeDiff);
                }
                ShapeDetectionService.upload(file, mUploadCallback);
                lastRequestDate = new Date();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("creating file failed");
            } catch (JAXBException e) {
                e.printStackTrace();
                System.out.println("marshalling layout failed");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("Sleeping interrupted");
            }
        }
    };

    private File getFileFromImage() throws IOException {
        File file = new File("frame.jpg");
        ImageIO.write(bufferedFrame, "jpg", file);
        return file;
    }

    private boolean mDidUpload = false;

    public CameraController(int cameraIndex) {
        root = new BorderPane();
        StackPane stackPane = new StackPane();
        webCamPane = new FlowPane();
        webCamPane.setStyle("-fx-background-color: #ccc;");
        imgWebCamCapturedImage = new ImageView();

        drawingPane = new BorderPane();
        drawingPane.setStyle("-fx-border-color: red;");
        drawingPane.setPrefHeight(480);
        drawingPane.setPrefWidth(640);

        stackPane.getChildren().add(imgWebCamCapturedImage);
        stackPane.getChildren().add(drawingPane);
        stackPane.setAlignment(Pos.CENTER);


        webCamPane.setOrientation(Orientation.HORIZONTAL);
        webCamPane.setAlignment(Pos.CENTER);
        webCamPane.getChildren().add(stackPane);

        root.setCenter(webCamPane);
        bottomCameraControlPane = new FlowPane();
        bottomCameraControlPane.setOrientation(Orientation.HORIZONTAL);
        bottomCameraControlPane.setAlignment(Pos.CENTER);
        bottomCameraControlPane.setHgap(20);
        bottomCameraControlPane.setVgap(10);
        bottomCameraControlPane.setPrefHeight(40);
        createCameraControls();
        root.setBottom(bottomCameraControlPane);
        root.setPrefHeight(660);
        root.setPrefWidth(700);

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

                            if (!mDidUpload) {
                                ShapeDetectionService.upload(getFileFromImage(), mUploadCallback);
                                lastRequestDate = new Date();
                                mDidUpload = true;
                            }

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

    public BufferedImage getBufferedFrame() {
        return bufferedFrame;
    }

    public BorderPane getRoot() {
        return root;
    }

    public PreviewController getPreviewController() {
        return previewController;
    }

    public void setPreviewController(PreviewController previewController) {
        this.previewController = previewController;
    }
}
