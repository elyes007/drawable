package tn.disguisedtoast.drawable.settingsModule.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {
    @FXML private FlowPane settingsPane;
    @FXML private Pane previewPane;
    @FXML private Button saveButton;

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
                settingsPane.getChildren().add(loader.load());
                ((ButtonSettingsViewController)loader.getController()).setButton(element);
            }else if(element.getElement().getTagName().equals(SupportedComponents.ION_IMG.toString())){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ImageSettingsView.fxml"));
                settingsPane.getChildren().add(loader.load());
                ((ImageSettingsViewController)loader.getController()).setImageView(element);
            }/*else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/EditTextSettingsView.fxml"));
                settingsPane.getChildren().add(loader.load());
                ((EditTextSettingsViewController)loader.getController()).setTextField((TextField)component);
            }*/
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
