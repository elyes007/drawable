package tn.disguisedtoast.drawable.previewModule.controllers;

import com.sun.javafx.webkit.WebConsoleListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.previewModule.models.Device;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PreviewController {

    private static PreviewCallBack callBack;
    private static VBox root;
    private static WebView webView;
    private static String url;

    private final float BUTTON_SIZE = 20;
    public static Document ionicDocument;
    private AppInterface appInterface;

    private PreviewController() {
        root = new VBox();
        root.setPrefHeight(731 + BUTTON_SIZE);
        root.setPrefWidth(411);

        webView = new WebView();
        webView.setPrefHeight(731 + BUTTON_SIZE);
        webView.setPrefWidth(411);
        webView.getEngine().setJavaScriptEnabled(true);
        webView.getEngine().setUserAgent("Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Mobile Safari/537.36");

        appInterface = new AppInterface();

        webView.getEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    JSObject win = (JSObject) webView.getEngine().executeScript("window");
                    win.setMember("app", appInterface);
                    webView.getEngine().executeScript("setIsSetting("+(PreviewController.callBack!=null)+");");
                    webView.getScene().getWindow().sizeToScene();
                    try {
                        Files.delete(Paths.get(webView.getEngine().getDocument().getDocumentURI().substring(8)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        WebConsoleListener.setDefaultListener(new WebConsoleListener(){
            @Override
            public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
                System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message);
            }
        });

        ComboBox<Device> previewDevice = new ComboBox<>();
        previewDevice.setItems(FXCollections.observableArrayList(Device.devices));
        previewDevice.setPromptText("Choose a device");
        previewDevice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Device>() {
            @Override
            public void changed(ObservableValue<? extends Device> observable, Device oldValue, Device newValue) {
                if(newValue != null){
                    root.setPrefHeight(newValue.getHeight() + BUTTON_SIZE);
                    root.setPrefWidth(newValue.getWidth());

                    webView.setPrefHeight(newValue.getHeight() + BUTTON_SIZE);
                    webView.setPrefWidth(newValue.getWidth());

                    webView.getEngine().setUserAgent(newValue.getUserAgent());
                    refresh();
                }
            }
        });

        root.getChildren().add(previewDevice);
        root.getChildren().add(webView);
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
        try{
            Path path = Paths.get(Paths.get(ionicDocument.baseUri()).getParent() + "/temp_"+ RandomStringUtils.randomAlphanumeric(8)+".html");
            Files.write(path, ionicDocument.html().getBytes());
            webView.getEngine().load("file:///"+path.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public Image snapshot() {
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        return this.webView.snapshot(snapshotParameters, null);
    }
}
