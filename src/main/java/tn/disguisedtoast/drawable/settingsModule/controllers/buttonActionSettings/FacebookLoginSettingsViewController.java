package tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings;

import com.google.gson.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.mozilla.javascript.CompilerEnvirons;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;
import tn.disguisedtoast.drawable.settingsModule.models.LogInConfigs;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.FunctionElement;
import tn.disguisedtoast.drawable.utils.typescriptParser.models.ImportElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FacebookLoginSettingsViewController implements Initializable, SettingsControllerInterface {

    @FXML
    private TextField appName;
    @FXML
    private TextField appId;
    @FXML
    private TextField packageName;
    @FXML
    private ComboBox destination;
    @FXML
    private Label infoLabel;
    @FXML
    private Hyperlink infoHyperlink;

    private JsonObject jsonObject;
    private JsonObject buttonLogObject;

    private Element element;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.appName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(this.buttonLogObject != null){
                this.buttonLogObject.addProperty("appName", this.appName.getText());
            }else {
                addLogAction();
            }
        });

        this.appId.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(this.buttonLogObject != null){
                this.buttonLogObject.addProperty("appId", this.appId.getText());
            }else {
                addLogAction();
            }
        });

        this.infoLabel.setOnMouseClicked(event -> {
            Bounds boundsInScene = this.infoLabel.localToScene(this.infoLabel.getBoundsInLocal());
            this.infoLabel.getTooltip().show(this.infoLabel, boundsInScene.getMaxX(), boundsInScene.getMaxY() + this.infoLabel.getHeight());
            this.infoLabel.getTooltip().setAutoHide(true);
        });

        this.infoHyperlink.setOnAction(event -> {
            Drawable.getInstance().getHostServices().showDocument("https://ionicframework.com/docs/native/facebook#installation");
        });

        try {
            String id = Jsoup.parse(new File(System.getProperty("user.dir") + "/src/main/RelatedFiles/ionic_project/config.xml"), "UTF-8").selectFirst("widget").attr("id");
            this.packageName.setText(id);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addLogAction(){
        this.buttonLogObject = new JsonObject();
        this.buttonLogObject.addProperty("type", "facebook");
        this.buttonLogObject.addProperty("appName", this.appName.getText());
        this.buttonLogObject.addProperty("appId", this.appId.getText());
        this.buttonLogObject.addProperty("button", this.element.attr("id"));

        JsonElement actionsElement = this.jsonObject.get("actions");
        if (actionsElement == null) return;
        actionsElement.getAsJsonArray().add(this.buttonLogObject);
    }

    public void setElement(Element element){
        this.element = element;
        getLogJsonObject();
        if(this.buttonLogObject != null){
            this.appName.setText(this.buttonLogObject.get("appName").getAsString());
            this.appId.setText(this.buttonLogObject.get("appId").getAsString());
        }
    }

    private void getLogJsonObject(){
        try{
            jsonObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder+"/conf.json")).getAsJsonObject();
            JsonElement actionElement = jsonObject.get("actions");
            if (actionElement == null) return;
            JsonArray actionsArray = actionElement.getAsJsonArray();
            List<JsonObject> toDeleteObjects = new ArrayList<>();

            for(JsonElement element : actionsArray){
                JsonObject object = (JsonObject)element;
                if(object.get("button").getAsString().equals(this.element.attr("id")) && object.get("type").getAsString().equals("facebook")){
                    buttonLogObject = object;
                }else if(object.get("button").getAsString().equals(this.element.attr("id"))) {
                    toDeleteObjects.add(object);
                }
            }
            for(JsonObject jo : toDeleteObjects){
                actionsArray.remove(jo);
            }
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean getLogSetting(Element button){
        try{
            JsonObject jsonObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder+"/conf.json")).getAsJsonObject();
            JsonArray actionsArray = jsonObject.get("actions").getAsJsonArray();

            for(JsonElement element : actionsArray){
                JsonObject object = (JsonObject)element;
                if(object.get("button").getAsString().equals(button.attr("id")) && object.get("type").getAsString().equals("facebook")){
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void save() {
        try{
            Path confFilePath = Paths.get(SettingsViewController.pageFolder + "/conf.json");
            Files.write(confFilePath, new GsonBuilder().create().toJson(jsonObject).getBytes());

            JsonObject jsonObject = new JsonParser().parse(new FileReader(confFilePath.toString())).getAsJsonObject();
            String pageName = jsonObject.get("page").getAsString().toLowerCase();
            String ionicPagePath = System.getProperty("user.dir") + "/src/main/RelatedFiles/ionic_project/src/app/" + pageName + "/" + pageName + ".page.ts";
            System.out.println(ionicPagePath);

            CompilerEnvirons env = new CompilerEnvirons();
            env.setRecoverFromErrors(true);
            env.setGenerateDebugInfo(true);
            env.setRecordingComments(true);

            ImportElement importElement = new ImportElement("@ionic-native/facebook/ngx");
            importElement.getDependencies().add("Facebook");
            importElement.getDependencies().add("FacebookLoginResponse");
            //TypeScriptParser.addImport(Paths.get(ionicPagePath), importElement);
            //TypeScriptParser.removeImport(Paths.get(ionicPagePath), "@ionic-native/facebook/ngx");

            //TypeScriptParser.addParameterToFunction(Paths.get(ionicPagePath), "constructor", "fb", "Facebook");
            //TypeScriptParser.addParameterToFunction(Paths.get(ionicPagePath), "ngOnInit", "fb", "Facebook");

            FunctionElement functionElement = new FunctionElement("async fbLogin");
            functionElement.setBodyLines(LogInConfigs.getFacebookFunctionBody());

            //TypeScriptParser.addFunction(Paths.get(ionicPagePath), functionElement);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
