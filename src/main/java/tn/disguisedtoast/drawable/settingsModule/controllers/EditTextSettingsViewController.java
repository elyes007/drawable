package tn.disguisedtoast.drawable.settingsModule.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditTextSettingsViewController implements Initializable {
    @FXML public TextField placeholder;
    @FXML public TextField value;

    private TextField textField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        placeholder.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                textField.setPromptText(newValue);
            }
        });

        value.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                textField.setText(newValue);
            }
        });
    }

    public void setTextField(TextField textField){
        this.textField = textField;
    }
}
