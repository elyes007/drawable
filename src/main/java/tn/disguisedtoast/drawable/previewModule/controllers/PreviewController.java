package tn.disguisedtoast.drawable.previewModule.controllers;

import com.sun.javafx.webkit.WebConsoleListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.w3c.dom.Element;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.previewModule.models.Device;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class PreviewController {

    private static PreviewCallBack callBack;
    private static VBox root;
    private static WebView webView;
    private static String url;

    private final float BUTTON_SIZE = 20;
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
                }
            });

        WebConsoleListener.setDefaultListener(new WebConsoleListener(){
            @Override
            public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
                System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message);
            }
        });

        ComboBox<Device> cameraOptions = new ComboBox<>();
        cameraOptions.setItems(FXCollections.observableArrayList(Device.devices));
        cameraOptions.setPromptText("Choose a device");
        cameraOptions.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Device>() {
            @Override
            public void changed(ObservableValue<? extends Device> observable, Device oldValue, Device newValue) {
                if(newValue != null){
                    root.setPrefHeight(newValue.getHeight() + BUTTON_SIZE);
                    root.setPrefWidth(newValue.getWidth());

                    webView.setPrefHeight(newValue.getHeight() + BUTTON_SIZE);
                    webView.setPrefWidth(newValue.getWidth());

                    webView.getEngine().setUserAgent(newValue.getUserAgent());
                    webView.getEngine().reload();
                }
            }
        });

        root.getChildren().add(cameraOptions);
        root.getChildren().add(webView);
    }

    public static Node getView(String url, PreviewCallBack callBack) {
        PreviewController.callBack = callBack;
        PreviewController.url = url;
        if( root == null || webView == null ) {
            new PreviewController();
        }
        webView.getEngine().load(url);
        return root;
    }

    public class AppInterface {

        public void setEelement(Object dom) {
            if(dom instanceof Element) {
                GeneratedElement element = new GeneratedElement((Element) dom);
                callBack.clicked(element);
            }else{
                System.out.println("Not Element");
            }
        }
    }

    public static void saveDocument() {
        try {
            DOMSource domSource = new DOMSource(webView.getEngine().getDocument());
            StreamResult result = new StreamResult(url);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.transform(domSource, result);
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    public interface PreviewCallBack {
        void clicked(GeneratedElement element);
    }
}
