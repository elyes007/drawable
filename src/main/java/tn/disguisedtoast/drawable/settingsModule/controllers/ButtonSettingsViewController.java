package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.google.gson.*;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import org.eclipse.jgit.util.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings.NavigationSettingsViewController;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;
import tn.disguisedtoast.drawable.settingsModule.utils.CustomColorPicker;
import tn.disguisedtoast.drawable.settingsModule.utils.DomUtils;
import tn.disguisedtoast.drawable.settingsModule.utils.FxUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ButtonSettingsViewController implements Initializable, SettingsControllerInterface {
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
    private GeneratedElement iconElement;
    private SettingsControllerInterface settingsControllerInterface;

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
                            settingsControllerInterface = null;
                            break;
                        case 1:
                            FXMLLoader navigationLoader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/buttonActionSettings/NavigationSettingsView.fxml"));
                            actionSettingsPane.setCenter(navigationLoader.load());
                            settingsControllerInterface = navigationLoader.getController();
                            ((NavigationSettingsViewController) settingsControllerInterface).setElement(button.getElement());
                            break;
                        case 2:
                            FXMLLoader facebookLoader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/buttonActionSettings/FacebookLoginSettingsView.fxml"));
                            actionSettingsPane.setCenter(facebookLoader.load());
                            settingsControllerInterface = facebookLoader.getController();
                            break;
                        case 3:
                            FXMLLoader googleLoader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/buttonActionSettings/GoogleLoginSettingsView.fxml"));
                            actionSettingsPane.setCenter(googleLoader.load());
                            settingsControllerInterface = googleLoader.getController();
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
                if(iconElement.getElement().attr("slot").equals("end")) {
                    this.rightSlot.setSelected(true);
                }else{
                    this.leftSlot.setSelected(true);
                }
            }
            ((TextNode)this.button.getElement().childNodes().get(this.button.getElement().childNodeSize()-1)).text(this.textValue.getText());
            this.button.getDomElement().getLastChild().setTextContent(this.textValue.getText());
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
                String cssRules = aWriter.getCSSAsString (button.getCssRules());
                button.getElement().attr("style", cssRules);
                this.button.getDomElement().setAttribute("style", cssRules);
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
            String cssRules = aWriter.getCSSAsString (button.getCssRules());
            button.getElement().attr("style", cssRules);
            button.getDomElement().setAttribute("style", cssRules);
        });

        this.boldButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("font-weight");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.boldButton.isSelected()) {
                button.getCssRules().addDeclaration(new CSSDeclaration("font-weight", CSSExpression.createSimple("bold")));
            }
            String cssRules = aWriter.getCSSAsString (button.getCssRules());
            button.getElement().attr("style", cssRules);
            button.getDomElement().setAttribute("style", cssRules);
        });

        this.italicButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("font-style");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.italicButton.isSelected()) {
                button.getCssRules().addDeclaration(new CSSDeclaration("font-style", CSSExpression.createSimple("italic")));
            }
            String cssRules = aWriter.getCSSAsString (button.getCssRules());
            button.getElement().attr("style", cssRules);
            button.getDomElement().setAttribute("style", cssRules);
        });

        this.underlinedButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("text-decoration");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.underlinedButton.isSelected()) {
                button.getCssRules().addDeclaration(new CSSDeclaration("text-decoration", CSSExpression.createSimple("underline")));
            }
            String cssRules = aWriter.getCSSAsString (button.getCssRules());
            button.getElement().attr("style", cssRules);
            button.getDomElement().setAttribute("style", cssRules);
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
            String fill = this.fillType.getValue().toString().toLowerCase();
            this.button.getElement().attr("fill", fill);
            this.button.getDomElement().setAttribute("fill", fill);
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
                String fillTheme = this.fillTheme.getValue().toString().toLowerCase();
                this.button.getElement().attr("color", fillTheme);
                this.button.getDomElement().setAttribute("color", fillTheme);
            } else {
                this.button.getElement().removeAttr("color");
                this.button.getDomElement().removeAttribute("color");
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
            String cssRules = aWriter.getCSSAsString (button.getCssRules());
            button.getElement().attr("style", cssRules);
            button.getDomElement().setAttribute("style", cssRules);
        });
    }

    private void initIcon() {

        this.iconName.setOnAction(event -> {
            iconElement.getElement().attr("name", this.iconName.getText());
            iconElement.getDomElement().setAttribute("name", this.iconName.getText());
        });

        this.iconName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue) {
                iconElement.getElement().attr("name", this.iconName.getText());
                iconElement.getDomElement().setAttribute("name", this.iconName.getText());
            }
        });

        this.leftSlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.rightSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
                iconElement.getElement().attr("slot", "start");
                iconElement.getDomElement().setAttribute("slot", "start");
            }
        });

        this.rightSlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.leftSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
                iconElement.getElement().attr("slot", "end");
                iconElement.getDomElement().setAttribute("slot", "end");
            }
        });

        this.iconOnlySlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.leftSlot.setSelected(false);
                this.rightSlot.setSelected(false);
                iconElement.getElement().attr("slot", "icon-only");
                this.button.getDomElement().setAttribute("slot", "icon-only");
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
            String cssRules = aWriter.getCSSAsString (button.getCssRules());
            button.getElement().attr("style", cssRules);
            button.getDomElement().setAttribute("style", cssRules);
        });

        verticalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("top");
                button.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            button.getCssRules().addDeclaration(new CSSDeclaration("top", CSSExpression.createSimple(newValue+"%")));
            String cssRules = aWriter.getCSSAsString (button.getCssRules());
            button.getElement().attr("style", cssRules);
            button.getDomElement().setAttribute("style", cssRules);
        });
    }

    //Setup
    public void setButton(GeneratedElement button){
        this.button = button;

        //Setting the botton text
        String buttonText = button.getElement().text();
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

        if(NavigationSettingsViewController.getNavigationSetting(button.getElement())){
            this.buttonAction.getSelectionModel().select(1);
        }
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
        if(button.getElement().hasAttr("fill")) {
            this.fillType.getSelectionModel().select(StringUtils.capitalize(button.getElement().attr("fill")));
        }else {
            this.fillType.getSelectionModel().select("Default");
        }
    }

    //Color: primary, secondary, danger, ...
    private void getButtonThemeColor() {
        if(button.getElement().hasAttr("color")) {
            this.fillTheme.getSelectionModel().select(StringUtils.capitalize(button.getElement().attr("color")));
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
        Element iconJsoupElement = this.button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
        if(iconJsoupElement != null) {
            iconElement = new GeneratedElement(iconJsoupElement,
                    (org.w3c.dom.Element) DomUtils.getChildNode(SupportedComponents.ION_ICON.toString().toUpperCase(), this.button.getDomElement()));
            this.iconName.setText(iconElement.getElement().attr("name"));
            if(!iconElement.getElement().hasAttr("slot") || iconElement.getElement().attr("slot").equals("start")) {
                this.leftSlot.setSelected(true);
                this.rightSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
            }else if(iconElement.getElement().attr("slot").equals("right")) {
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

    @Override
    public void save() {
        if(settingsControllerInterface != null) {
            settingsControllerInterface.save();
        }else{
            deleteActionElement();
        }
        PreviewController.saveDocument();
    }

    private void deleteActionElement(){
        try{
            String configPath = SettingsViewController.pageFolder+"/conf.json";
            JsonObject jsonObject = new JsonParser().parse(new FileReader(configPath)).getAsJsonObject();
            JsonArray actionsArray = jsonObject.get("actions").getAsJsonArray();
            List<JsonObject> toDeleteObjects = new ArrayList<>();

            for(JsonElement element : actionsArray){
                JsonObject object = (JsonObject)element;
                if(object.get("button").getAsString().equals(button.getElement().attr("id"))){
                    toDeleteObjects.add(object);
                }
            }
            for(JsonObject o : toDeleteObjects) {
                actionsArray.remove(o);
            }
            Files.write(Paths.get(configPath), new Gson().toJson(jsonObject).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
