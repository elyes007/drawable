package tn.disguisedtoast.drawable.storyboardModule.controllers;

import com.google.gson.JsonArray;
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
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamChooserController;
import tn.disguisedtoast.drawable.detectionModule.controllers.CamStreamViewController;
import tn.disguisedtoast.drawable.homeModule.models.Page;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;
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

        public void addNavigation(String source, String buttonId, String dest) {
            try {
                String path = (Drawable.projectPath + "&RelatedFiles&pages&" + source + "&" + source + ".html")
                        .replace("&", File.separator);
                File file = new File(path);
                Document document = Jsoup.parse(file, "UTF-8");
                document.body().select("#" + buttonId).attr("[routerLink]", dest);
                FileUtils.write(file, document.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getButtons(String pageName) {
            try {
                String path = (Drawable.projectPath + "&RelatedFiles&pages&" + pageName + "&" + pageName + ".html")
                        .replace("&", File.separator);
                File file = new File(path);
                Document document = Jsoup.parse(file, "UTF-8");
                ArrayList<Element> buttons = document.body().select("ion-button");
                if (buttons.isEmpty()) return "[]";
                StringBuilder sb = new StringBuilder("[");
                for (Element button : buttons) {
                    if (button.hasAttr("[routerLink]")) continue;
                    sb.append("{\"id\":\"").append(button.id()).append("\",\"title\":\"").append(button.html())
                            .append("\"},");
                }
                if (sb.charAt(0) == '[') return "[]";
                sb.deleteCharAt(sb.length() - 1).append("]");
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return "[]";
            }
        }

        public void updateStoryboard(String page, String x, String y) {
            String path = (Drawable.projectPath + "&RelatedFiles&storyboard.json").replace("&", File.separator);
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            JsonArray storyboard = new JsonParser().parse(fileReader).getAsJsonArray();
            for (int i = 0; i < storyboard.size(); i++) {
                JsonObject object = (JsonObject) storyboard.get(i);
                if (object.get("page").getAsString().equals(page)) {
                    object.addProperty("x", x);
                    object.addProperty("y", y);
                }
            }
            try {
                FileUtils.writeStringToFile(new File(path), storyboard.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        public void openPage(String pageFolder) {
            try {
                EveryWhereLoader.getInstance().showLoader(Drawable.globalStage);
                FXMLLoader loader = new FXMLLoader(SettingsViewController.class.getResource("/layouts/settingsViews/SettingsView.fxml"));
                EveryWhereLoader.getInstance().stopLoader(loader.load());
                SettingsViewController controller = loader.getController();
                controller.init((Drawable.projectPath + "&RelatedFiles&pages&" + pageFolder).replace("&", File.separator));
            } catch (IOException e) {
                e.printStackTrace();
                EveryWhereLoader.getInstance().stopLoader(null);
            }
        }

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
