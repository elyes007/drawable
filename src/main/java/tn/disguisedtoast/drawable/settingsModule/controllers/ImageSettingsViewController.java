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
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.w3c.dom.Element;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.settingsModule.TestMain.Main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class ImageSettingsViewController implements Initializable {
    @FXML public TextField filePath;
    @FXML public Button browseButton;

    private String generatedViewsPath = "/generated_views/assets/drawable";

    private GeneratedElement imageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generatedViewsPath = getClass().getResource("/generated_views").getPath();

        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Images", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png")
            );
            File image = fileChooser.showOpenDialog(Main.globalStage);
            if(image != null) {
                filePath.setText(image.getPath());
            }
        });

        filePath.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.equals(oldValue)) {
                    try {
                        String path = generatedViewsPath + "/" + newValue;
                        System.out.println(path);
                        File file = new File(path);
                        if (!file.exists()) {
                            String oldImagePath = imageView.getElement().getAttribute("src");
                            if(!oldImagePath.equals("assets/drawable/placeholder.png")){
                                new File(generatedViewsPath + "/" + oldImagePath).delete();
                            }
                            String newImagePath = "assets/drawable/"+RandomStringUtils.randomAlphanumeric(8)+".png";
                            FileUtils.copyFile(new File(newValue), new File(generatedViewsPath + "/" + newImagePath));
                            imageView.getElement().setAttribute("src", newImagePath);
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }



                /*String path = "";
                if(newValue.trim().isEmpty()){
                    path = DRAWABLES_PATH + "/placeholder.png";
                } else {
                    try {
                        File file = new File(newValue);

                        FileUtils.copyFile(file, new File(getClass().getResource("/generated_views/assets/drawable")+"/"+ RandomStringUtils.randomAlphanumeric(8)+".png"));
                    }catch (Exception e) {
                        System.out.println(e);
                    }
                    System.out.println("Drawable Path:  " + getClass().getResource("/generated_views/assets/drawable")+"/"+ RandomStringUtils.randomAlphanumeric(8)+".png");
                }
                File file = new File((newValue.isEmpty() ? "./assets/drawable/placeholder.png" : newValue));
                System.out.println(newValue);
                System.out.println(file.toURI().toString());
                imageView.getElement().setAttribute("src", file.toURI().toString());*/
            }
        });
    }

    public void setImageView(GeneratedElement imageView){
        this.imageView = imageView;
        filePath.setText(this.imageView.getElement().getAttribute("src"));
    }
}
