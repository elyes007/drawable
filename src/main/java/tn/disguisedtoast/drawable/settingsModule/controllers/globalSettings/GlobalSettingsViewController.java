package tn.disguisedtoast.drawable.settingsModule.controllers.globalSettings;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GlobalSettingsViewController implements Initializable {

    @FXML
    private ImageView downArrow;
    @FXML
    private ImageView leftArrow;
    @FXML
    private HBox subMenu;
    @FXML
    private VBox themingButton;
    @FXML
    private VBox firebaseButton;

    @FXML
    private BorderPane globalSettingsPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.downArrow.setVisible(false);
        this.subMenu.setVisible(false);

        this.themingButton.setOnMouseClicked(event -> {
            this.subMenu.setVisible(!this.subMenu.isVisible());
            this.downArrow.setVisible(!this.downArrow.isVisible());
            this.leftArrow.setVisible(!this.leftArrow.isVisible());

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/globalSettings/ThemeSettingsView.fxml"));
                this.globalSettingsPane.setCenter(loader.load());
                ((ThemeSettingsViewController) loader.getController()).setTheme("primary");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
