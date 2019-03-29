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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {
    @FXML private AnchorPane settingsPane;
    @FXML private Pane previewPane;
    @FXML private Button saveButton;

    private Stage settingsStage;

    public static void showStage() {
        try{
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
        String path = System.getProperty("user.dir")+"\\src\\main\\RelatedFiles\\generated_views\\ion-test.html";
        Node previewRoot = PreviewController.getView(path, element -> setComponent(element));
        previewPane.getChildren().add(previewRoot);
        this.saveButton.setOnAction(event -> {
            PreviewController.saveDocument();
        });
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
                ((ButtonSettingsViewController)loader.getController()).setButton(element);
            }else if(element.getElement().tagName().equals(SupportedComponents.ION_IMG.toString())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ImageSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                ((ImageSettingsViewController)loader.getController()).setImageView(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.ION_ITEM.toString()) && element.getElement().select(SupportedComponents.ION_INPUT.toString()).size() != 0){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/EditTextSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                ((EditTextSettingsViewController)loader.getController()).setTextField(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.ION_LABEL.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/LabelSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                ((LabelSettingsViewController)loader.getController()).setLabel(element);
            } else if(element.getElement().tagName().equals(SupportedComponents.ION_TOOLBAR.toString())) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ToolbarSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                ((ToolbarSettingsViewController)loader.getController()).setToolbarElement(element);
            }

            this.settingsStage.sizeToScene();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
