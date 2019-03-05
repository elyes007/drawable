package tn.disguisedtoast.drawable.settingsModule.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import tn.disguisedtoast.drawable.settingsModule.controllers.testPreview.TestPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsViewController implements Initializable {
    @FXML private FlowPane settingsPane;
    @FXML private Pane previewPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/testPreview/TestPane.fxml"));
            previewPane.getChildren().add(loader.load());
            ((TestPane)loader.getController()).setSettingsViewController(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void setComponent(Node component){
        settingsPane.getChildren().clear();
        try{
            if(component instanceof Button){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ButtonSettingsView.fxml"));
                settingsPane.getChildren().add(loader.load());
                ((ButtonSettingsViewController)loader.getController()).setButton((Button)component);
            }else if(component instanceof ImageView){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/ImageSettingsView.fxml"));
                settingsPane.getChildren().add(loader.load());
                ((ImageSettingsViewController)loader.getController()).setImageView((ImageView)component);
            }else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/EditTextSettingsView.fxml"));
                settingsPane.getChildren().add(loader.load());
                ((EditTextSettingsViewController)loader.getController()).setTextField((TextField)component);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
