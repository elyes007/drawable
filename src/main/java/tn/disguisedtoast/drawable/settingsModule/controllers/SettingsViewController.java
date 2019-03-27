package tn.disguisedtoast.drawable.settingsModule.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.utils.DomUtils;

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
        Node previewRoot = PreviewController.getView(getClass().getResource("/generated_views/ion-test.html").toExternalForm(), element -> setComponent(element));
        previewPane.getChildren().add(previewRoot);
        this.saveButton.setOnAction(event -> {
            PreviewController.saveDocument();
        });
    }

    public void setComponent(GeneratedElement element){
        settingsPane.getChildren().clear();
        try{
            if(element.getElement().getTagName().equals(SupportedComponents.ION_BUTTON.toString())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ButtonSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);

                settingsPane.getChildren().add(pane);
                ((ButtonSettingsViewController)loader.getController()).setButton(element);
            }else if(element.getElement().getTagName().equals(SupportedComponents.ION_IMG.toString())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ImageSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                ((ImageSettingsViewController)loader.getController()).setImageView(element);
            } else if(element.getElement().getTagName().equals(SupportedComponents.ION_ITEM.toString()) && DomUtils.getChildNode(SupportedComponents.ION_INPUT.toString(), element.getElement()) != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/EditTextSettingsView.fxml"));
                Pane pane = loader.load();
                AnchorPane.setTopAnchor(pane, 0.0);
                AnchorPane.setLeftAnchor(pane, 0.0);
                AnchorPane.setRightAnchor(pane, 0.0);
                settingsPane.getChildren().add(pane);
                ((EditTextSettingsViewController)loader.getController()).setTextField(element);
            }

            /*else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/EditTextSettingsView.fxml"));
                settingsPane.getChildren().add(loader.load());
                ((EditTextSettingsViewController)loader.getController()).setTextField((TextField)component);
            }*/

            this.settingsStage.sizeToScene();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
