package tn.disguisedtoast.drawable.settingsModule.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ButtonSettingsViewController implements Initializable {
    @FXML public BorderPane actionSettingsPane;
    @FXML public TextField textValue;
    @FXML public ComboBox textSize;
    @FXML public ColorPicker textColor;
    @FXML public ColorPicker backgroundColor;
    @FXML public ChoiceBox buttonAction;
    @FXML public FlowPane noActionPane;

    private Integer[] textSizes = {10, 12, 14, 18, 24, 36, 48, 64, 72, 96};
    private String[] actions = {"Select an action", "Navigation", "Login Facebook", "Login Google"};
    private Button button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.textSize.getItems().addAll(Arrays.asList(textSizes));
        this.buttonAction.getItems().addAll(Arrays.asList(actions));
        this.buttonAction.getSelectionModel().selectFirst();
        this.buttonAction.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int index = ((ChoiceBox)event.getTarget()).getSelectionModel().getSelectedIndex();
                try{
                    switch (index){
                        case 0:
                            actionSettingsPane.setCenter(noActionPane);
                            break;
                        case 1:
                            FXMLLoader navigationLoader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/buttonActionSettings/NavigationSettingsView.fxml"));
                            actionSettingsPane.setCenter(navigationLoader.load());
                            break;
                        case 2:
                            FXMLLoader facebookLoader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/buttonActionSettings/FacebookLoginSettingsView.fxml"));
                            actionSettingsPane.setCenter(facebookLoader.load());
                            break;
                        case 3:
                            FXMLLoader googleLoader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/buttonActionSettings/GoogleLoginSettingsView.fxml"));
                            actionSettingsPane.setCenter(googleLoader.load());
                            break;
                    }
                }catch (IOException ex){
                    System.err.println(ex);
                }
            }
        });

        this.backgroundColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateStyle();
            }
        });

        this.textValue.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                button.setText(textValue.getText());
            }
        });

        this.textSize.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Font font = button.getFont();
                try {
                    Integer value = textSize.getValue() instanceof Integer ? (Integer) textSize.getValue() : Integer.valueOf((String) textSize.getValue());
                    button.setFont(Font.font(font.getFamily(), value));
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                    textSize.setValue((int)button.getFont().getSize());
                }
            }
        });

        this.textColor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateStyle();
            }
        });
    }

    public void updateStyle(){
        String css = "-fx-background-color: rgba("+(backgroundColor.getValue().getRed()*255)+", "+(backgroundColor.getValue().getGreen()*255)+", "+(backgroundColor.getValue().getBlue()*255)+", 255);";
        css += "-fx-text-fill: rgba("+(textColor.getValue().getRed()*255)+", "+(textColor.getValue().getGreen()*255)+", "+(textColor.getValue().getBlue()*255)+", 255);";
        button.setStyle(css);
    }

    public void setButton(Button button){
        this.button = button;
        this.textValue.setText(button.getText());
        this.textSize.setValue((int)button.getFont().getSize());
        this.textColor.setValue((Color)button.getTextFill());
        this.backgroundColor.setValue((Color)button.getBackground().getFills().get(0).getFill());
    }
}
