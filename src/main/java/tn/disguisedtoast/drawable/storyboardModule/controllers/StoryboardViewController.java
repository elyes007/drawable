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
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
    public JSCallback jsCallback = new JSCallback();
    public static String pagesPath = (Drawable.projectPath + "&RelatedFiles&pages").replace("&", File.separator);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        webView.getEngine().setJavaScriptEnabled(true);
        String path = (Drawable.projectPath + "/RelatedFiles/storyboard.html").replace("\\", "/");
        webView.getEngine().getLoadWorker().stateProperty().addListener((ObservableValue<? extends Worker.State> ov, Worker.State oldState, Worker.State newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                JSObject jsobj = (JSObject) webView.getEngine().executeScript("window");
                jsobj.setMember("java", jsCallback);
                webView.requestFocus();
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

    public static List<Page> loadPages() {
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

    public static class JSCallback implements CamChooserController.CameraButtonCallback {
        private Stage chooserStage;

        public void removeNav(String src, String dest, String button) {
            String path = (Drawable.projectPath + "&RelatedFiles&pages&" + src).replace("&", File.separator);
            FileReader reader = null;
            try {
                reader = new FileReader(path + "/conf.json");
                JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
                JsonArray actions = jsonObject.get("actions").getAsJsonArray();
                for (int i = 0; i < actions.size(); i++) {
                    String actionDest = actions.get(i).getAsJsonObject().get("dest").getAsString();
                    String actionButton = actions.get(i).getAsJsonObject().get("button").getAsString();
                    if (actionDest.equals(dest) && actionButton.equals(button)) {
                        actions.remove(i);
                        //remove routerLink
                        String htmlPath = (path + "&" + src + ".html")
                                .replace("&", File.separator);
                        File file = new File(htmlPath);
                        String routerLink = String.format("['/%s']", dest.toLowerCase());
                        Document document = Jsoup.parse(file, "UTF-8");
                        document.body().getElementsByAttributeValue("[routerLink]", routerLink)
                                .forEach(btn -> btn.removeAttr("[routerLink]"));
                        FileUtils.write(file, document.toString());
                        break;
                    }
                }
                FileUtils.write(new File(path + "/conf.json"), jsonObject.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void deletePage(String page) {
            System.gc();
            //remove page directory
            String path = (Drawable.projectPath + "&RelatedFiles&pages&" + page).replace("&", File.separator);
            try {
                FileUtils.deleteDirectory(new File(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //remove from other pages' conf files
            File root = new File(pagesPath);
            String[] directories = root.list((dir, name) -> (new File(dir, name).isDirectory()));
            for (String dir : directories) {
                if (dir.equals("temp")) continue;
                FileReader reader = null;
                try {
                    reader = new FileReader(pagesPath + "/" + dir + "/conf.json");
                    JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();
                    JsonArray actions = jsonObject.get("actions").getAsJsonArray();
                    for (int i = 0; i < actions.size(); i++) {
                        String dest = actions.get(i).getAsJsonObject().get("dest").getAsString();
                        if (dest.equals(page)) {
                            actions.remove(i--);
                            //remove routerLink
                            String htmlPath = (Drawable.projectPath + "&RelatedFiles&pages&" + dir + "&" + dir + ".html")
                                    .replace("&", File.separator);
                            File file = new File(htmlPath);
                            String routerLink = String.format("['/%s']", dest.toLowerCase());
                            Document document = Jsoup.parse(file, "UTF-8");
                            document.body().getElementsByAttributeValue("[routerLink]", routerLink)
                                    .forEach(button -> button.removeAttr("[routerLink]"));
                            FileUtils.write(file, document.toString());
                        }
                    }
                    FileUtils.write(new File(pagesPath + "/" + dir + "/conf.json"), jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            //remove from storyboard
            path = (Drawable.projectPath + "&RelatedFiles&storyboard.json").replace("&", File.separator);
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            JsonObject storyboard = new JsonParser().parse(fileReader).getAsJsonObject();
            JsonArray pages = storyboard.get("pages").getAsJsonArray();
            for (int i = 0; i < pages.size(); i++) {
                JsonObject object = (JsonObject) pages.get(i);
                if (object.get("page").getAsString().equals(page)) {
                    pages.remove(i);
                    break;
                }
            }
            try {
                FileUtils.writeStringToFile(new File(path), storyboard.toString());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        public String getPath() {
            return StringUtils.substringBeforeLast(getClass().getResource("/storyboardModule/delete.svg").getPath(),
                    "/");
        }

        public void addNavigation(String source, String buttonId, String dest) {
            try {
                //update html
                String htmlPath = (Drawable.projectPath + "&RelatedFiles&pages&" + source + "&" + source + ".html")
                        .replace("&", File.separator);
                File file = new File(htmlPath);
                String routerLink = String.format("['/%s']", dest.toLowerCase());
                Document document = Jsoup.parse(file, "UTF-8");
                document.body().select("#" + buttonId).attr("[routerLink]", routerLink);
                FileUtils.write(file, document.toString());
                //update conf.json
                String confPath = (Drawable.projectPath + "&RelatedFiles&pages&" + source + "&conf.json")
                        .replace("&", File.separator);
                FileReader fileReader = new FileReader(confPath);
                JsonObject json = new JsonParser().parse(fileReader).getAsJsonObject();
                JsonArray actions = json.get("actions").getAsJsonArray();
                //remove old action for same button
                for (int i = 0; i < actions.size(); i++) {
                    JsonObject act = actions.get(i).getAsJsonObject();
                    if (act.get("button").getAsString().equals(buttonId)) {
                        actions.remove(i);
                        break;
                    }
                }
                //add new action
                JsonObject action = new JsonObject();
                action.addProperty("type", "nav");
                action.addProperty("dest", dest);
                action.addProperty("button", buttonId);
                actions.add(action);
                FileUtils.writeStringToFile(new File(confPath), json.toString());
            } catch (IOException e) {
                e.printStackTrace();
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
            JsonObject storyboard = new JsonParser().parse(fileReader).getAsJsonObject();
            JsonArray pages = storyboard.get("pages").getAsJsonArray();
            for (int i = 0; i < pages.size(); i++) {
                JsonObject object = (JsonObject) pages.get(i);
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

        public void updateStoryboardZoom(int zoom) {
            String path = (Drawable.projectPath + "&RelatedFiles&storyboard.json").replace("&", File.separator);
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            JsonObject storyboard = new JsonParser().parse(fileReader).getAsJsonObject();
            storyboard.addProperty("zoom", zoom);
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
