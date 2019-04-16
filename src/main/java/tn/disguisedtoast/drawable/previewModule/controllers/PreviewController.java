package tn.disguisedtoast.drawable.previewModule.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.previewModule.models.Device;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PreviewController {

    private static PreviewCallBack callBack;
    private static VBox root;
    private static WebView webView;
    private static ComboBox<Device> previewDevice;
    private static String url;

    private final float BUTTON_SIZE = 20;
    public static Document ionicDocument;
    private static String snapshotDestination;
    private static SnapshotCallback snapshotCallback;
    private AppInterface appInterface;

    private PreviewController() {
        Device defaultDevice = Device.devices[0];
        double ratio = defaultDevice.getWidth() / defaultDevice.getHeight();
        double previewHeight = Math.min(Drawable.height - 30 * 2 - BUTTON_SIZE, defaultDevice.getHeight());
        double previewWidth = previewHeight * ratio;
        root = new VBox();
        root.setPrefHeight(previewHeight + BUTTON_SIZE);
        root.setPrefWidth(previewWidth);

        webView = new WebView();
        webView.setPrefHeight(previewHeight + BUTTON_SIZE);
        webView.setPrefWidth(previewWidth);
        webView.getEngine().setJavaScriptEnabled(true);
        webView.getEngine().setUserAgent("Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Mobile Safari/537.36");

        appInterface = new AppInterface();

        webView.getEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                    JSObject win = (JSObject) webView.getEngine().executeScript("window");
                    win.setMember("app", appInterface);
                    webView.getEngine().executeScript("setIsSetting("+(PreviewController.callBack!=null)+");");
                    if(snapshotCallback != null) {
                        snapshot(snapshotCallback);
                    }
                    System.out.println("Here");
                    new Thread(() -> {
                        File file = new File(Paths.get(webView.getEngine().getDocument().getDocumentURI().substring(8)).toUri());
                        while (file.exists()) {
                            try {
                                Files.delete(file.toPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });

        WebConsoleListener.setDefaultListener(new WebConsoleListener(){
            @Override
            public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
                System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message);
            }
        });

        AnchorPane pane = new AnchorPane();
        ImageView skinImageView = new ImageView();

        pane.getChildren().add(webView);
        pane.getChildren().add(skinImageView);


        previewDevice = new ComboBox<>();
        previewDevice.setItems(FXCollections.observableArrayList(Device.devices));
        previewDevice.setPromptText("Choose a device");
        previewDevice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Device>() {
            @Override
            public void changed(ObservableValue<? extends Device> observable, Device oldValue, Device newValue) {
                if(newValue != null){
                    double ratio = newValue.getWidth() / newValue.getHeight();
                    double previewHeight = Math.min(Drawable.height - 30 * 2 - BUTTON_SIZE, newValue.getHeight());
                    double previewWidth = previewHeight * ratio;

                    root.setPrefHeight(previewHeight + BUTTON_SIZE);
                    root.setPrefWidth(previewWidth);

                    pane.setPrefHeight(previewHeight + BUTTON_SIZE);
                    pane.setPrefWidth(previewWidth);

                    webView.getEngine().setUserAgent(newValue.getUserAgent());
                    AnchorPane.setTopAnchor(webView, previewHeight * (newValue.getSkin().getMargins().getTop() / newValue.getHeight()));
                    AnchorPane.setRightAnchor(webView, previewWidth * (newValue.getSkin().getMargins().getRight() / newValue.getWidth()));
                    AnchorPane.setBottomAnchor(webView, previewHeight * (newValue.getSkin().getMargins().getBottom() / newValue.getHeight()));
                    AnchorPane.setLeftAnchor(webView, previewWidth * (newValue.getSkin().getMargins().getLeft() / newValue.getWidth()));

                    try {
                        skinImageView.setImage(new Image(getClass().getResource("/drawable/skins/" + newValue.getSkin().getImageName()).toURI().toString()));
                        skinImageView.setFitHeight(previewHeight);
                        skinImageView.setFitWidth(previewWidth);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    refresh();
                }
            }
        });
        previewDevice.getSelectionModel().select(0);

        root.getChildren().add(previewDevice);
        root.getChildren().add(pane);
    }

    public static Node getView(String url, PreviewCallBack callBack) {
        try{
            PreviewController.callBack = callBack;
            PreviewController.url = url;
            File input = new File(url);
            PreviewController.ionicDocument = Jsoup.parse(input, "UTF-8");
            if( root == null || webView == null ) {
                new PreviewController();
            }
            refresh();
            return root;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void refresh(){
        Platform.runLater(() -> {
            try {
                Path path = Paths.get(Paths.get(ionicDocument.baseUri()).getParent() + "/temp_" + RandomStringUtils.randomAlphanumeric(8) + ".html");
                Files.write(path, ionicDocument.html().getBytes());
                webView.getEngine().load("file:///" + path.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void refreshForCamStream() {
        try {
            File input = new File(url);
            PreviewController.ionicDocument = Jsoup.parse(input, "UTF-8");
            if (root == null || webView == null) {
                new PreviewController();
            }
            Path path = Paths.get(input.getParent() + "/temp_" + RandomStringUtils.randomAlphanumeric(8) + ".html");
            Files.write(path, ionicDocument.html().getBytes());
            Platform.runLater(() -> webView.getEngine().load("file:///" + path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void snapshot(SnapshotCallback callback){
        new Thread(() -> {
            try{
                Thread.sleep(500);
                System.out.println(snapshotDestination);
                Platform.runLater(() -> {
                    if(snapshotDestination != null && !snapshotDestination.isEmpty()) {
                        SnapshotParameters snapshotParameters = new SnapshotParameters();
                        snapshotParameters.setFill(Color.TRANSPARENT);
                        Image image = webView.snapshot(snapshotParameters, null);

                        File outputFile = new File(snapshotDestination);
                        if(outputFile.exists()){
                            outputFile.delete();
                        }
                        BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
                        try {
                            ImageIO.write(bImage, "png", outputFile);

                            JsonObject jsonObject = new JsonParser().parse(new FileReader(Paths.get(snapshotDestination).getParent().toString() + "/conf.json")).getAsJsonObject();
                            jsonObject.addProperty("snapshot", "snapshot.png");
                            Files.write(Paths.get(Paths.get(snapshotDestination).getParent().toString() + "/conf.json"), new Gson().toJson(jsonObject).getBytes());

                            callback.completed();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        snapshotDestination = null;
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public class AppInterface {

        public void setEelement(Object dom) {
            if(dom instanceof Element) {
                org.jsoup.nodes.Element element = PreviewController.ionicDocument.select("#"+((Element) dom).getAttribute("id")).first();
                GeneratedElement generatedElement = new GeneratedElement(element, (Element)dom);
                callBack.clicked(generatedElement);
            }else{
                System.out.println("Not Element");
            }
        }
    }

    public static void saveDocument() {
        try{
            Path path = Paths.get(url);
            Document document = Jsoup.parse(ionicDocument.html());
            document.outputSettings(new Document.OutputSettings().prettyPrint(false));
            Files.write(path, document.html().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveWebView(String url) {
        try {
            Path path = Paths.get(url);
            String html = (String) webView.getEngine().executeScript("document.documentElement.outerHTML");
            Files.write(path, html.getBytes());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public interface PreviewCallBack {
        void clicked(GeneratedElement element);
    }

    public static void saveSnapshot(String destination, SnapshotCallback callback) {
        previewDevice.getSelectionModel().clearSelection();
        snapshotDestination = destination;
        snapshotCallback = callback;
        previewDevice.getSelectionModel().select(0);
    }

    public static void updateClickableElements(){
        webView.getEngine().executeScript("updateClickableElements();");
    }
}
