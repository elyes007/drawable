package tn.disguisedtoast.drawable.settingsModule.controllers.globalSettings;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;

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
    private VBox primaryButton;
    @FXML
    private VBox secondaryButton;
    @FXML
    private VBox tertiaryButton;
    @FXML
    private VBox successButton;
    @FXML
    private VBox warningButton;
    @FXML
    private VBox dangerButton;
    @FXML
    private VBox darkButton;
    @FXML
    private VBox mediumButton;
    @FXML
    private VBox lightButton;

    @FXML
    private BorderPane globalSettingsPane;
    @FXML
    private Button saveButton;

    private SettingsControllerInterface currentSettingController;
    private VBox selectedButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.downArrow.setVisible(false);
        this.subMenu.setVisible(false);

        this.themingButton.setOnMouseClicked(event -> {
            this.subMenu.setVisible(!this.subMenu.isVisible());
            this.downArrow.setVisible(!this.downArrow.isVisible());
            this.leftArrow.setVisible(!this.leftArrow.isVisible());
        });

        this.primaryButton.setOnMouseClicked(this::themeButtonClicked);
        this.secondaryButton.setOnMouseClicked(this::themeButtonClicked);
        this.tertiaryButton.setOnMouseClicked(this::themeButtonClicked);
        this.successButton.setOnMouseClicked(this::themeButtonClicked);
        this.warningButton.setOnMouseClicked(this::themeButtonClicked);
        this.dangerButton.setOnMouseClicked(this::themeButtonClicked);
        this.darkButton.setOnMouseClicked(this::themeButtonClicked);
        this.mediumButton.setOnMouseClicked(this::themeButtonClicked);
        this.lightButton.setOnMouseClicked(this::themeButtonClicked);

        this.saveButton.setOnAction(event -> {
            if (currentSettingController != null) {
                currentSettingController.save();
            }
        });
    }

    private void themeButtonClicked(MouseEvent event) {
        try {
            if (selectedButton != null) {
                selectedButton.setStyle("");
            }
            String themeName = ((Label) ((VBox) event.getSource()).getChildren().get(0)).getText();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/globalSettings/ThemeSettingsView.fxml"));
            this.globalSettingsPane.setCenter(loader.load());
            currentSettingController = ((ThemeSettingsViewController) loader.getController());
            ((ThemeSettingsViewController) currentSettingController).setTheme(themeName.toLowerCase());
            ((VBox) event.getSource()).setStyle("-fx-background-color: #517d5e;");
            selectedButton = (VBox) event.getSource();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
