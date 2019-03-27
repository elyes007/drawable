package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.helger.css.ECSSVersion;
import com.helger.css.ICSSWriterSettings;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpression;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.reader.CSSReaderDeclarationList;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.eclipse.jgit.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;
import tn.disguisedtoast.drawable.settingsModule.utils.CustomColorPicker;
import tn.disguisedtoast.drawable.settingsModule.utils.DomUtils;
import tn.disguisedtoast.drawable.settingsModule.utils.FxUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ButtonSettingsViewController implements Initializable {
    @FXML public BorderPane actionSettingsPane;
    @FXML public TextField textValue;
    @FXML public ComboBox textSize;
    @FXML public Label backgroundColorPane;
    @FXML public ChoiceBox buttonAction;
    @FXML public FlowPane noActionPane;
    @FXML public ComboBox fillType;
    @FXML public ComboBox fillTheme;
    @FXML public ToggleButton boldButton;
    @FXML public ToggleButton italicButton;
    @FXML public ToggleButton underlinedButton;
    @FXML public Label fontColorPane;
    @FXML public TextField iconName;
    @FXML public ToggleButton leftSlot;
    @FXML public ToggleButton rightSlot;
    @FXML public ToggleButton iconOnlySlot;
    @FXML public Slider horizontalPosition;
    @FXML public Slider verticalPosition;

    public CustomColorPicker textColor;
    public CustomColorPicker backgroundColor;

    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings (ECSSVersion.CSS30, false));

    private Integer[] textSizes = {10, 12, 14, 18, 24, 36, 48, 64, 72, 96};
    private String[] themes = {"Default", "Primary", "Secondary", "Tertiary", "Success", "Warning", "Danger", "Light", "Medium", "Dark"};
    private String[] fills = {"Solid", "Outline", "Clear", "None"};
    private String[] actions = {"Select an action", "Navigation", "Login Facebook", "Login Google"};
    private GeneratedElement button;
    private Element iconElement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset (StandardCharsets.UTF_8.name ());

        initTextView();
        initThemeView();
        initBackgroundColorView();
        initIcon();
        setUpPosition();

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
    }

    //Init
    private void initTextView() {
        this.textSize.getItems().add("Default");
        this.textSize.getItems().addAll(Arrays.asList(textSizes));
        this.textSize.getSelectionModel().selectFirst();

        this.textColor = new CustomColorPicker();
        this.textColor.setTooltip(new Tooltip("Select font color."));
        this.fontColorPane.setGraphic(this.textColor);

        this.textValue.setOnKeyReleased(event -> {
            if(this.textValue.getText().isEmpty()) {
                this.leftSlot.setDisable(true);
                this.rightSlot.setDisable(true);
                this.iconOnlySlot.setDisable(false);
                this.iconOnlySlot.setSelected(true);
            } else {
                this.rightSlot.setDisable(false);
                this.leftSlot.setDisable(false);
                this.iconOnlySlot.setDisable(true);
                if(iconElement.getAttribute("slot").equals("end")) {
                    this.rightSlot.setSelected(true);
                }else{
                    this.leftSlot.setSelected(true);
                }
            }
            button.getElement().getLastChild().setNodeValue(textValue.getText());
        });

        this.textSize.setOnAction(event -> {
            try {
                try {
                    CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("font-size");
                    button.getCssRules().removeDeclaration(declaration);
                }catch (NullPointerException e) {
                    System.out.println(e);
                }
                if(!textSize.getValue().equals("Default") && textSize.getValue().toString().matches("\\d+(\\.\\d+)?")) {
                    button.getCssRules().addDeclaration(new CSSDeclaration("font-size", CSSExpression.createSimple(textSize.getValue()+"px")));
                } else {
                    textSize.getSelectionModel().selectFirst();
                }
                button.getElement().setAttribute("style", aWriter.getCSSAsString (button.getCssRules()));
            }catch (NumberFormatException ex){
                ex.printStackTrace();
                //textSize.setValue((int)button.getFont().getSize());
            }
        });

        this.textColor.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("color");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(!newValue.equals(Color.TRANSPARENT)) {
                button.getCssRules().addDeclaration(new CSSDeclaration("color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue) +" !important")));
            }
            button.getElement().setAttribute("style", aWriter.getCSSAsString (button.getCssRules()));
        });

        this.boldButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("font-weight");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.boldButton.isSelected()) {
                button.getCssRules().addDeclaration(new CSSDeclaration("font-weight", CSSExpression.createSimple("bold")));
            }
            button.getElement().setAttribute("style", aWriter.getCSSAsString (button.getCssRules()));
        });

        this.italicButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("font-style");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.italicButton.isSelected()) {
                button.getCssRules().addDeclaration(new CSSDeclaration("font-style", CSSExpression.createSimple("italic")));
            }
            button.getElement().setAttribute("style", aWriter.getCSSAsString (button.getCssRules()));
        });

        this.underlinedButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("text-decoration");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.underlinedButton.isSelected()) {
                button.getCssRules().addDeclaration(new CSSDeclaration("text-decoration", CSSExpression.createSimple("underline")));
            }
            button.getElement().setAttribute("style", aWriter.getCSSAsString (button.getCssRules()));
        });
    }

    private void initThemeView() {
        this.fillTheme.getItems().addAll(Arrays.asList(themes));
        this.fillType.getItems().addAll(Arrays.asList(fills));

        this.fillType.setOnAction(event -> {
            if(this.fillType.getValue() == "None") {
                this.fillTheme.getSelectionModel().select("Default");
                this.fillTheme.setDisable(true);
            } else {
                this.fillTheme.setDisable(false);
            }
            this.button.getElement().setAttribute("fill", this.fillType.getValue().toString().toLowerCase());
        });

        this.fillType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == "Solid") {
                this.backgroundColor.setValue(Color.TRANSPARENT);
                this.backgroundColor.setDisable(true);
            }else {
                this.backgroundColor.setDisable(false);
            }
        });

        this.fillTheme.setOnAction(event -> {
            if(this.fillTheme.getValue() != "Default") {
                this.button.getElement().setAttribute("color", this.fillTheme.getValue().toString().toLowerCase());
            } else {
                this.button.getElement().removeAttribute("color");
            }
        });

        this.fillTheme.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == "Default") {
                this.textColor.setDisable(false);
            }else {
                this.textColor.setValue(Color.TRANSPARENT);

                this.textColor.setDisable(true);
            }
        });
    }

    private void initBackgroundColorView() {
        this.backgroundColor = new CustomColorPicker();
        this.backgroundColor.setTooltip(new Tooltip("Select button background color."));
        this.backgroundColorPane.setGraphic(this.backgroundColor);

        this.backgroundColor.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("background-color");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            if(!newValue.equals(Color.TRANSPARENT)) {
                button.getCssRules().addDeclaration(new CSSDeclaration("background-color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue)+" !important")));
            }
            button.getElement().setAttribute("style", aWriter.getCSSAsString (button.getCssRules()));
        });
    }

    private void initIcon() {

        this.iconName.setOnAction(event -> iconElement.setAttribute("name", this.iconName.getText()));

        this.iconName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                iconElement.setAttribute("name", this.iconName.getText());
            }
        });

        this.leftSlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.rightSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
                ((Element)DomUtils.getChildNode("ION-ICON", this.button.getElement())).setAttribute("slot", "start");
            }
        });

        this.rightSlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.leftSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
                ((Element)DomUtils.getChildNode("ION-ICON", this.button.getElement())).setAttribute("slot", "end");
            }
        });

        this.iconOnlySlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.leftSlot.setSelected(false);
                this.rightSlot.setSelected(false);
                ((Element)DomUtils.getChildNode("ION-ICON", this.button.getElement())).setAttribute("slot", "icon-only");
            }
        });
    }

    private void setUpPosition() {
        horizontalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("left");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            button.getCssRules().addDeclaration(new CSSDeclaration("left", CSSExpression.createSimple(newValue+"%")));
            button.getElement().setAttribute("style", aWriter.getCSSAsString (button.getCssRules()));
        });

        verticalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("top");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            button.getCssRules().addDeclaration(new CSSDeclaration("top", CSSExpression.createSimple(newValue+"%")));
            button.getElement().setAttribute("style", aWriter.getCSSAsString (button.getCssRules()));
        });
    }

    //Setup
    public void setButton(GeneratedElement button){
        this.button = button;

        //Setting the botton text
        String buttonText = button.getElement().getLastChild().getNodeValue().trim();
        this.textValue.setText(buttonText);
        if(buttonText.isEmpty()) {
            this.leftSlot.setDisable(true);
            this.rightSlot.setDisable(true);
            this.iconOnlySlot.setDisable(false);
            this.iconOnlySlot.setSelected(true);
        } else {
            this.rightSlot.setDisable(false);
            this.leftSlot.setDisable(false);
            this.iconOnlySlot.setDisable(true);
        }

        getButtonFontSize();
        getButtonFontColor();
        getButtonIsBold();
        getButtonIsItalic();
        getButtonIsUnderlined();

        //Setting the button look
        getButtonThemeFill();
        getButtonThemeColor();
        getButtonBackgroundColor();

        //Setting the button icon
        getButtonIcon();

        setButtonPosition();
    }

    private void getButtonFontSize() {
        try{
            String fontSizeString = CssRuleExtractor.extractValue(button.getCssRules(), "font-size");
            this.textSize.setValue(Integer.parseInt(fontSizeString.replaceAll("\\D+","")));
        } catch (NullPointerException e){}
    }

    private void getButtonFontColor() {
        try {
            String fontColorString = CssRuleExtractor.extractValue(button.getCssRules(), "color");
            this.textColor.setValue(Color.valueOf(fontColorString));
        }catch (NullPointerException e){
            this.textColor.setValue(Color.TRANSPARENT);
        }
    }

    private void getButtonIsBold() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(button.getCssRules(), "font-weight");
            if(fontWeightString.equals("bold")) {
                this.boldButton.setSelected(true);
                return;
            }
        }catch (NullPointerException e){ }
        this.boldButton.setSelected(false);
    }

    private void getButtonIsItalic() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(button.getCssRules(), "font-style");
            if(fontWeightString.equals("italic")) {
                this.italicButton.setSelected(true);
                return;
            }
        }catch (NullPointerException e){ }
        this.italicButton.setSelected(false);
    }

    private void getButtonIsUnderlined() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(button.getCssRules(), "text-decoration");
            if(fontWeightString.equals("underline")) {
                this.underlinedButton.setSelected(true);
                return;
            }
        }catch (NullPointerException e){ }
        this.underlinedButton.setSelected(false);
    }

    //Fill type: outline, fill, clear, ...
    private void getButtonThemeFill() {
        if(button.getElement().hasAttribute("fill")) {
            this.fillType.getSelectionModel().select(StringUtils.capitalize(button.getElement().getAttribute("fill")));
        }else {
            this.fillType.getSelectionModel().select("Default");
        }
    }

    //Color: primary, secondary, danger, ...
    private void getButtonThemeColor() {
        if(button.getElement().hasAttribute("color")) {
            this.fillTheme.getSelectionModel().select(StringUtils.capitalize(button.getElement().getAttribute("color")));
        }else {
            this.fillTheme.getSelectionModel().select("Default");
        }
    }

    //css background-color rule
    private void getButtonBackgroundColor() {
        try {
            String backgroundString = CssRuleExtractor.extractValue(button.getCssRules(), "background-color");
            this.backgroundColor.setValue(Color.valueOf(backgroundString));
        }catch (NullPointerException e){
            this.backgroundColor.setValue(Color.TRANSPARENT);
        }
    }

    private void getButtonIcon() {
        if((iconElement = ((Element)DomUtils.getChildNode("ION-ICON", this.button.getElement()))) != null) {
            this.iconName.setText(iconElement.getAttribute("name"));
            if(!iconElement.hasAttribute("slot") || iconElement.getAttribute("slot").equals("start")) {
                this.leftSlot.setSelected(true);
                this.rightSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
            }else if(iconElement.getAttribute("slot").equals("right")) {
                this.rightSlot.setSelected(true);
                this.leftSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
            } else {
                this.iconOnlySlot.setSelected(true);
                this.rightSlot.setSelected(false);
                this.leftSlot.setSelected(false);
            }
        }
    }

    private void setButtonPosition() {
        try {
            String horizontalString = CssRuleExtractor.extractValue(button.getCssRules(), "left");
            double hPos = Double.parseDouble(horizontalString.substring(0,horizontalString.length()-1));
            this.horizontalPosition.setValue(hPos);
        }catch (Exception e){
            this.horizontalPosition.setValue(0);
        }

        try {
            String verticalString = CssRuleExtractor.extractValue(button.getCssRules(), "top");
            double vPos = Double.parseDouble(verticalString.substring(0, verticalString.length()-1));
            this.verticalPosition.setValue(vPos);
        }catch (Exception e){
            this.verticalPosition.setValue(0);
        }
    }
}
