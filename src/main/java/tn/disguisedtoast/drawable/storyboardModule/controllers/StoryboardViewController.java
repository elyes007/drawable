package tn.disguisedtoast.drawable.storyboardModule.controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.javafx.webkit.WebConsoleListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamChooserController;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamStreamViewController;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.utils.EveryWhereLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StoryboardViewController implements Initializable {

    public AnchorPane root;
    public WebView webView;
    public String pagesPath = "D:\\Headquarters\\New folder\\test";
    public JSCallback jsCallback = new JSCallback();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webView.getEngine().setJavaScriptEnabled(true);
        String path = (pagesPath + "/RelatedFiles/storyboard.html").replace("\\", "/");
        webView.getEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject jsobj = (JSObject) webView.getEngine().executeScript("window");
                jsobj.setMember("java", jsCallback);
            }
        });
        WebConsoleListener.setDefaultListener(new WebConsoleListener() {
            @Override
            public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
                System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message);
            }
        });
        webView.getEngine().load("file:///" + path);
    }

    public List<Page> loadPages() {
        File root = new File(pagesPath);
        String[] directories = root.list((dir, name) -> (new File(dir, name).isDirectory()));
        List<Page> pages = new ArrayList<>();
        for (String dir : directories) {
            if (dir.equals("temp")) continue;
            try {
                JsonObject jsonObject = new JsonParser().parse(new FileReader(pagesPath + "/" + dir + "/conf.json")).getAsJsonObject();
                String pageName = jsonObject.get("page").getAsString();
                Page pg = new Page(pageName, pagesPath + "/" + dir);
                pages.add(pg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        pages.sort((p1, p2) -> Integer.parseInt(p1.getFolderName().substring(pagesPath.length() + 5)) > Integer.parseInt(p2.getFolderName().substring(pagesPath.length() + 5)) ? 1 : -1);
        return pages;
    }

    public void search(String text) {
        webView.getEngine().executeScript("search('" + text + "')");
    }

    public class JSCallback implements CamChooserController.CameraButtonCallback {
        private Stage chooserStage;

        public void addPage() {
            EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
            chooserStage = new Stage();
            chooserStage.setTitle("Camera Chooser");
            chooserStage.setScene(new Scene(new CamChooserController(this).getRoot()));
            chooserStage.setHeight(200);
            chooserStage.setWidth(500);
            chooserStage.setResizable(false);
            chooserStage.centerOnScreen();
            chooserStage.show();
            EveryWhereLoader.getInstance().stopLoader(null);
        }

        @Override
        public void onButtonClicked(int webcamIndex) {
            chooserStage.close();
            if (webcamIndex != -1) {
                try {
                    EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/layouts/detectionViews/CamStreamView.fxml"));
                    loader.getLocation().openStream();
                    EveryWhereLoader.getInstance().stopLoader(loader.load());
                    CamStreamViewController controller = loader.getController();
                    controller.init(webcamIndex);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
