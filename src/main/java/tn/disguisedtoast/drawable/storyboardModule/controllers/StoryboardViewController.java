package tn.disguisedtoast.drawable.storyboardModule.controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

public class StoryboardViewController implements Initializable {

    public AnchorPane root;
    public WebView webView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webView.getEngine().setJavaScriptEnabled(true);
        String path = getClass().getResource("/storyboardModule/storysample.html").getPath();
        webView.getEngine().load("file:///" + path);
    }
}
