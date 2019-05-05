package tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class NavigationSettingsViewController implements Initializable, SettingsControllerInterface {
    @FXML public ComboBox pagesList;

    private JsonObject jsonObject;
    private JsonObject buttonNavObject;
    private GeneratedElement buttonGeneratedElement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String parentDirPath = (Drawable.projectPath + "&RelatedFiles&pages").replace("&", File.separator);
        File file = new File(parentDirPath);
        String[] directories = file.list((dir, name) -> (new File(dir, name).isDirectory() && !name.equals(Paths.get(SettingsViewController.pageFolder).getFileName().toString())) && !name.equals("temp"));

        /*List<String> pages = Arrays.stream(Objects.requireNonNull(file.listFiles((dir, name) -> (new File(dir, name).isDirectory() && !name.equals(Paths.get(SettingsViewController.pageFolder).getFileName().toString())) && !name.equals("temp")))).map(file1 -> {
            File[] files = file1.listFiles();
            if (files == null) return "";
            try {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().equals("conf.json")) {
                        JsonObject pageConf = new JsonParser().parse(new FileReader(files[i])).getAsJsonObject();
                        return pageConf.get("page").getAsString();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return "";
        }).collect(Collectors.toList());*/


        this.pagesList.setPromptText("Select a destination page");
        this.pagesList.getItems().addAll(directories);

        this.pagesList.setOnAction(event -> {
            String pageName = (String)this.pagesList.getValue();

            if(this.buttonNavObject != null){
                this.buttonNavObject.addProperty("dest", pageName);
            }else {
                this.buttonNavObject = new JsonObject();
                this.buttonNavObject.addProperty("dest", pageName);

                this.jsonObject.getAsJsonObject("actions").add(this.buttonGeneratedElement.getElement().id(), buttonNavObject);
            }
        });
    }

    public void setElement(GeneratedElement element) {
        this.buttonGeneratedElement = element;
        getNavJsonObject();
        if(this.buttonNavObject != null){
            this.pagesList.setValue(buttonNavObject.get("dest").getAsString());
        }
    }

    private void getNavJsonObject(){
        try{
            jsonObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder+"/conf.json")).getAsJsonObject();

            JsonObject actions = jsonObject.getAsJsonObject("actions");

            if (actions.has(this.buttonGeneratedElement.getElement().id())) {
                JsonObject buttonAction = actions.getAsJsonObject(this.buttonGeneratedElement.getElement().id());
                if (buttonAction.has("dest")) {
                    buttonNavObject = buttonAction;
                } else {
                    actions.remove(this.buttonGeneratedElement.getElement().id());
                }
            }
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        if (this.pagesList.getValue() == null) {
            this.buttonGeneratedElement.getElement().removeAttr("[routerlink]");

            if (this.jsonObject.getAsJsonObject("actions").has(this.buttonGeneratedElement.getElement().id())) {
                this.jsonObject.getAsJsonObject("actions").remove(this.buttonGeneratedElement.getElement().id());
            }
            return;
        }

        try{
            Files.write(Paths.get(SettingsViewController.pageFolder+"/conf.json"), new GsonBuilder().create().toJson(jsonObject).getBytes());
            this.buttonGeneratedElement.getElement().attr("[routerlink]", this.pagesList.getValue().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
