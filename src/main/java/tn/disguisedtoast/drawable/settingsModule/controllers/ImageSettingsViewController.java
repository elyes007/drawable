package tn.disguisedtoast.drawable.settingsModule.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.FileChooser;
import tn.disguisedtoast.drawable.settingsModule.TestMain.Main;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ImageSettingsViewController implements Initializable {
    @FXML public TextField filePath;
    @FXML public Button browseButton;

    private ImageView imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File image = fileChooser.showOpenDialog(Main.globalStage);
            filePath.setText(image.getPath());
        });

        filePath.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                imageView.setImage(new Image("file:///"+newValue));
            }
        });
    }

    public void setImageView(ImageView imageView){
        this.imageView = imageView;
    }
}
