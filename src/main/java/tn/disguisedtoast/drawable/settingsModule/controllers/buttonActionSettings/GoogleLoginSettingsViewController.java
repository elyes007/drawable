package tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings;

import com.google.gson.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.jsoup.nodes.Element;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class GoogleLoginSettingsViewController implements Initializable, SettingsControllerInterface {

    @FXML
    private TextField appUrl;
    @FXML private TextField apiKey;

    private JsonObject jsonObject;
    private JsonObject buttonLogObject;

    private Element element;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.appUrl.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(this.buttonLogObject != null){
                this.buttonLogObject.addProperty("url", this.appUrl.getText());
            }else {
                addLogAction();
            }
        });

        this.apiKey.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(this.buttonLogObject != null){
                this.buttonLogObject.addProperty("key", this.apiKey.getText());
            }else {
                addLogAction();
            }
        });
    }

    private void addLogAction(){
        this.buttonLogObject = new JsonObject();
        this.buttonLogObject.addProperty("type", "google");
        this.buttonLogObject.addProperty("url", this.appUrl.getText());
        this.buttonLogObject.addProperty("key", this.apiKey.getText());
        this.buttonLogObject.addProperty("button", this.element.attr("id"));

        this.jsonObject.get("actions").getAsJsonArray().add(this.buttonLogObject);
    }

    public void setElement(Element element){
        this.element = element;
        getLogJsonObject();
        if(this.buttonLogObject != null){
            this.appUrl.setText(this.buttonLogObject.get("url").getAsString());
            this.apiKey.setText(this.buttonLogObject.get("key").getAsString());
        }
    }

    private void getLogJsonObject(){
        try{
            jsonObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder+"/conf.json")).getAsJsonObject();
            JsonArray actionsArray = jsonObject.get("actions").getAsJsonArray();
            List<JsonObject> toDeleteObjects = new ArrayList<>();

            for(JsonElement element : actionsArray){
                JsonObject object = (JsonObject)element;
                if(object.get("button").getAsString().equals(this.element.attr("id")) && object.get("type").getAsString().equals("google")){
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
                if(object.get("button").getAsString().equals(button.attr("id")) && object.get("type").getAsString().equals("google")){
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
            System.out.println("Here");
            Files.write(Paths.get(SettingsViewController.pageFolder+"/conf.json"), new GsonBuilder().create().toJson(jsonObject).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
