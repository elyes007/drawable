package tn.disguisedtoast.drawable.settingsModule.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.utils.DomUtils;
import tn.disguisedtoast.drawable.utils.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {
    @FXML private AnchorPane settingsPane;
    @FXML private Pane previewPane;
    @FXML private Button saveButton;

    private Stage settingsStage;
    private SettingsControllerInterface currentController;
    public static String pageFolder;

    public static void showStage(String pageFolder) {
        try{
            SettingsViewController.pageFolder = pageFolder;
            FXMLLoader loader = new FXMLLoader(SettingsViewController.class.getResource("/layouts/settingsViews/SettingsView.fxml"));
            Pane pane = loader.load();
            SettingsViewController controller = loader.getController();
            System.out.println(controller);
            controller.settingsStage = new Stage();
            controller.settingsStage.setScene(new Scene(pane));

            controller.settingsStage.show();
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
        this.saveButton.setOnAction(event -> currentController.save());
    }

    public void setComponent(GeneratedElement element){
        settingsPane.getChildren().clear();
        try{
            System.out.println(element.getElement().tagName());
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

            this.settingsStage.sizeToScene();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
