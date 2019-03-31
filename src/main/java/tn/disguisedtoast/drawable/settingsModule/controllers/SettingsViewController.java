package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.utils.JsonUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {
    @FXML private AnchorPane settingsPane;
    @FXML private Pane previewPane;
    @FXML private Button saveButton;
    @FXML private Button finishButton;

    private SettingsControllerInterface currentController;
    public static String pageFolder;

    public static void showStage(String pageFolder) {
        try{
            SettingsViewController.pageFolder = pageFolder;
            FXMLLoader loader = new FXMLLoader(SettingsViewController.class.getResource("/layouts/settingsViews/SettingsView.fxml"));
            Pane pane = loader.load();
            Drawable.globalStage.setScene(new Scene(pane));
            //SettingsViewController controller = loader.getController();
            /*controller.settingsStage = new Stage();
            controller.settingsStage.setScene(new Scene(pane));

            controller.settingsStage.show();*/
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String htmlPath = JsonUtils.getValue("html", pageFolder + "/conf.json");
        String path = pageFolder + "/" + htmlPath;
        Node previewRoot = PreviewController.getView(path, element -> setComponent(element));
        previewPane.getChildren().add(previewRoot);

        this.saveButton.setDisable(true);
        this.saveButton.setOnAction(event -> currentController.save());
        this.finishButton.setOnAction(event -> {
            try{
                PreviewController.saveSnapshot(pageFolder + File.separator + "snapshot.png");
                JsonObject jsonObject = new JsonParser().parse(new FileReader(pageFolder+"/conf.json")).getAsJsonObject();
                jsonObject.addProperty("snapshot", "snapshot.png");
                Files.write(Paths.get(pageFolder+"/conf.json"), new Gson().toJson(jsonObject).getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    public void setComponent(GeneratedElement element){
        settingsPane.getChildren().clear();
        try{
            if(element.getElement().tagName().equals(SupportedComponents.ION_BUTTON.toString())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ButtonSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);

                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((ButtonSettingsViewController) currentController).setButton(element);
            }else if(element.getElement().tagName().equals(SupportedComponents.ION_IMG.toString())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ImageSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((ImageSettingsViewController)currentController).setImageView(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.ION_ITEM.toString()) && element.getElement().select(SupportedComponents.ION_INPUT.toString()).size() != 0){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/EditTextSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((EditTextSettingsViewController)currentController).setTextField(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.ION_LABEL.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/LabelSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((LabelSettingsViewController)currentController).setLabel(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.ION_TOOLBAR.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ToolbarSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((ToolbarSettingsViewController)currentController).setToolbarElement(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.BODY.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/GlobalPageSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                currentController = loader.getController();
                ((GlobalPageSettingsViewController)currentController).setBodyGeneratedElement(element);
            }

            //HomeController.primaryStage.sizeToScene();
            if(currentController == null) {
                this.saveButton.setDisable(true);
            }else{
                this.saveButton.setDisable(false);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
