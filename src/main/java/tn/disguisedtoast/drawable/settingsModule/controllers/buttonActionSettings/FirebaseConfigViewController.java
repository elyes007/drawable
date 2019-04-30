package tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.settingsModule.controllers.ButtonSettingsViewController;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class FirebaseConfigViewController implements Initializable {

    private static Stage popupStage;
    @FXML
    private TextField apiKey;
    @FXML
    private TextField projectId;
    @FXML
    private TextField messagingSenderId;
    @FXML
    private Button confirmButton;
    @FXML
    private Hyperlink hyperlink;

    public static void showPopup(ButtonSettingsViewController buttonSettingsViewController) {
        try {
            FXMLLoader loader = new FXMLLoader(FirebaseConfigViewController.class.getResource("/layouts/settingsViews/buttonActionSettings/FirebaseConfigView.fxml"));
            popupStage = new Stage();
            popupStage.setTitle("Firebase Configuration");
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(Drawable.globalStage);
            popupStage.setScene(new Scene(loader.load()));
            popupStage.setResizable(false);
            popupStage.centerOnScreen();
            popupStage.show();
            popupStage.setOnCloseRequest(event -> {
                buttonSettingsViewController.buttonAction.getSelectionModel().select(0);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.confirmButton.setDisable(true);
        this.confirmButton.setOnAction(event -> {
            try {
                JsonObject firebaseConfig = new JsonObject();
                firebaseConfig.addProperty("apiKey", this.apiKey.getText());
                firebaseConfig.addProperty("projectId", this.projectId.getText());
                firebaseConfig.addProperty("messagingSenderId", this.messagingSenderId.getText());
                firebaseConfig.add("platforms", new JsonObject());

                String configPath = Drawable.projectPath + File.separator + "state.json";
                JsonObject globalConfig = new JsonParser().parse(new FileReader(configPath)).getAsJsonObject();
                globalConfig.add("firebase", firebaseConfig);

                Files.write(Paths.get(configPath), new Gson().toJson(globalConfig).getBytes());
                popupStage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.apiKey.textProperty().addListener(this::buttonAtivator);
        this.projectId.textProperty().addListener(this::buttonAtivator);
        this.messagingSenderId.textProperty().addListener(this::buttonAtivator);

        this.hyperlink.setOnAction(event -> {
            Drawable.getInstance().getHostServices().showDocument("https://ionicframework.com/docs/native/facebook#installation");
        });
    }

    private void buttonAtivator(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (this.apiKey.getText().isEmpty() || this.projectId.getText().isEmpty() || this.messagingSenderId.getText().isEmpty()) {
            this.confirmButton.setDisable(true);
        } else {
            this.confirmButton.setDisable(false);
        }
    }
}
