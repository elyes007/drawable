package tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings;

import com.google.gson.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.jsoup.nodes.Element;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsControllerInterface;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ResourceBundle;

public class NavigationSettingsViewController implements Initializable, SettingsControllerInterface {
    @FXML public ComboBox pagesList;

    private JsonObject jsonObject;
    private JsonObject buttonNavObject;
    private Element element;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String parentDirPath = System.getProperty("user.dir")+"/src/main/RelatedFiles/generated_views/pages";
        File file = new File(parentDirPath);
        String[] directories = file.list((dir, name) -> (new File(dir, name).isDirectory() && !name.equals(Paths.get(SettingsViewController.pageFolder).getFileName().toString())));

        this.pagesList.setPromptText("Select a destination page");
        this.pagesList.getItems().addAll(Arrays.asList(directories));

        this.pagesList.setOnAction(event -> {
            String pageName = (String)this.pagesList.getValue();
            if(this.buttonNavObject != null){
                this.buttonNavObject.addProperty("dest", pageName);
            }else {
                this.buttonNavObject = new JsonObject();
                this.buttonNavObject.addProperty("type", "nav");
                this.buttonNavObject.addProperty("dest", pageName);
                this.buttonNavObject.addProperty("button", this.element.attr("id"));

                this.jsonObject.get("actions").getAsJsonArray().add(buttonNavObject);
            }
        });
    }

    public void setElement(Element element) {
        this.element = element;
        getNavJsonObject();
        if(this.buttonNavObject != null){
            this.pagesList.setValue(buttonNavObject.get("dest").getAsString());
        }
    }

    private void getNavJsonObject(){
        try{
            jsonObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder+"/conf.json")).getAsJsonObject();
            JsonArray actionsArray = jsonObject.get("actions").getAsJsonArray();

            for(JsonElement element : actionsArray){
                JsonObject object = (JsonObject)element;
                if(object.get("button").getAsString().equals(this.element.attr("id")) && object.get("type").getAsString().equals("nav")){
                    buttonNavObject = object;
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean getNavigationSetting(Element button){
        try{
            JsonObject jsonObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder+"/conf.json")).getAsJsonObject();
            JsonArray actionsArray = jsonObject.get("actions").getAsJsonArray();

            for(JsonElement element : actionsArray){
                JsonObject object = (JsonObject)element;
                if(object.get("button").getAsString().equals(button.attr("id")) && object.get("type").getAsString().equals("nav")){
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
            Files.write(Paths.get(SettingsViewController.pageFolder+"/conf.json"), new GsonBuilder().create().toJson(jsonObject).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
