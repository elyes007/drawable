package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import org.eclipse.jgit.util.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings.FacebookLoginSettingsViewController;
import tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings.GoogleLoginSettingsViewController;
import tn.disguisedtoast.drawable.settingsModule.controllers.buttonActionSettings.NavigationSettingsViewController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;
import tn.disguisedtoast.drawable.settingsModule.models.IonIcon;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;
import tn.disguisedtoast.drawable.settingsModule.utils.CustomColorPicker;
import tn.disguisedtoast.drawable.settingsModule.utils.FxUtils;
import tn.disguisedtoast.drawable.settingsModule.utils.IconComboboxCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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
    @FXML public ToggleButton leftSlot;
    @FXML public ToggleButton rightSlot;
    @FXML public ToggleButton iconOnlySlot;
    @FXML public Slider horizontalPosition;
    @FXML public Slider verticalPosition;
    @FXML public ComboBox icon;
    @FXML
    private Slider horizontalScale;
    @FXML
    private Label posHValue;
    @FXML
    private Slider verticalScale;
    @FXML
    private Label posVValue;
    @FXML
    private Label scaleHValue;
    @FXML
    private Label scaleVValue;
    @FXML
    private Label deleteButton;

    public CustomColorPicker textColor;
    public CustomColorPicker backgroundColor;

    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings (ECSSVersion.CSS30, false));

    private Integer[] textSizes = {10, 12, 14, 18, 24, 36, 48, 64, 72, 96};
    private String[] themes = {"Default", "Primary", "Secondary", "Tertiary", "Success", "Warning", "Danger", "Light", "Medium", "Dark"};
    private String[] fills = {"Solid", "Outline", "Clear"};
    private String[] actions = {"Select an action", "Navigation", "Login Facebook"};
    private GeneratedElement button;
    private SettingsControllerInterface settingsControllerInterface;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset (StandardCharsets.UTF_8.name ());

        this.deleteButton.setOnMouseClicked(event -> {
            this.button.getElement().remove();
            PreviewController.refresh();
            SettingsViewController.getInstance().clearSettingView();
        });

        initTextView();
        initThemeView();
        initBackgroundColorView();
        initIcon();
        setUpPosition();
        setUpScale();

        this.buttonAction.getItems().addAll(Arrays.asList(actions));
        this.buttonAction.getSelectionModel().selectFirst();
        this.buttonAction.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int index = (Integer) newValue;
            try {
                switch (index) {
                    case 0:
                        actionSettingsPane.setCenter(noActionPane);
                        settingsControllerInterface = null;
                        button.getElement().removeAttr("[routerlink]");
                        break;
                    case 1:
                        FXMLLoader navigationLoader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/buttonActionSettings/NavigationSettingsView.fxml"));
                        actionSettingsPane.setCenter(navigationLoader.load());
                        settingsControllerInterface = navigationLoader.getController();
                        ((NavigationSettingsViewController) settingsControllerInterface).setElement(button);
                        break;
                    case 2:
                        FXMLLoader facebookLoader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/buttonActionSettings/FacebookLoginSettingsView.fxml"));
                        actionSettingsPane.setCenter(facebookLoader.load());
                        settingsControllerInterface = facebookLoader.getController();
                        ((FacebookLoginSettingsViewController) settingsControllerInterface).setElement(button.getElement(), ButtonSettingsViewController.this);
                        System.out.println("Here FB");
                        button.getElement().removeAttr("[routerLink]");
                        //save();
                        break;
                    case 3:
                        FXMLLoader googleLoader = new FXMLLoader(getClass().getResource("/layouts/settingsViews/buttonActionSettings/GoogleLoginSettingsView.fxml"));
                        actionSettingsPane.setCenter(googleLoader.load());
                        settingsControllerInterface = googleLoader.getController();
                        ((GoogleLoginSettingsViewController) settingsControllerInterface).setElement(button.getElement());
                        System.out.println("here G");
                        button.getElement().removeAttr("[routerLink]");
                        //save();
                        break;
                }
            } catch (IOException ex) {
                System.err.println(ex);
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

        this.textValue.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty()) {
                this.iconOnlySlot.setSelected(true);
                this.leftSlot.setSelected(false);
                this.rightSlot.setSelected(false);
            } else if(this.iconOnlySlot.isSelected()) {
                this.leftSlot.setSelected(true);
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
        icon.setButtonCell(new IconComboboxCell());
        icon.setCellFactory(param -> new IconComboboxCell());
        icon.getItems().addAll(IonIcon.ionIcons);
        icon.setPromptText("Select an icon");

        icon.setOnAction(event -> {
            Element ionIcon = button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
            if(ionIcon != null){
                ionIcon.remove();
                button.getDomElement().removeChild(button.getDomElement().getElementsByTagName(SupportedComponents.ION_ICON.toString().toUpperCase()).item(0));
            }
            if(!((IonIcon)icon.getValue()).getName().equals("None")){
                this.leftSlot.setDisable(false);
                this.rightSlot.setDisable(false);
                this.iconOnlySlot.setDisable(false);

                ionIcon = new Element(SupportedComponents.ION_ICON.toString());
                ionIcon.attr("slot", "start");
                ionIcon.attr("name", ((IonIcon)icon.getValue()).getName());

                org.w3c.dom.Element ionIconElement = button.getDomElement().getOwnerDocument().createElement(SupportedComponents.ION_ICON.toString().toUpperCase());
                ionIconElement.setAttribute("slot", "start");
                ionIconElement.setAttribute("name", ((IonIcon)icon.getValue()).getName());

                button.getElement().insertChildren(0, ionIcon);
                button.getDomElement().insertBefore(ionIconElement, button.getDomElement().getLastChild());

                this.leftSlot.setSelected(true);
            }else {
                this.leftSlot.setSelected(false);
                this.rightSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);

                this.leftSlot.setDisable(true);
                this.rightSlot.setDisable(true);
                this.iconOnlySlot.setDisable(true);
            }
        });

        this.leftSlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.rightSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
                Element ionElement = this.button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
                if(ionElement != null){
                    ionElement.attr("slot", "start");
                    ((org.w3c.dom.Element)this.button.getDomElement().getElementsByTagName(SupportedComponents.ION_ICON.toString().toUpperCase()).item(0)).setAttribute("slot", "start");
                }
                if(this.textValue.getText().isEmpty()) {
                    this.textValue.setText("Button");
                }
            } else if(!this.rightSlot.isSelected() && !this.iconOnlySlot.isSelected()) {
                this.leftSlot.setSelected(true);
            }
        });

        this.rightSlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.leftSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
                Element ionElement = this.button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
                if(ionElement != null){
                    ionElement.attr("slot", "end");
                    ((org.w3c.dom.Element)this.button.getDomElement().getElementsByTagName(SupportedComponents.ION_ICON.toString().toUpperCase()).item(0)).setAttribute("slot", "end");
                }
                if(this.textValue.getText().isEmpty()) {
                    this.textValue.setText("Button");
                }
            } else if(!this.leftSlot.isSelected() && !this.iconOnlySlot.isSelected()) {
                this.rightSlot.setSelected(true);
            }
        });

        this.iconOnlySlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue) {
                this.leftSlot.setSelected(false);
                this.rightSlot.setSelected(false);
                Element ionElement = this.button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
                if(ionElement != null) {
                    ionElement.attr("slot", "icon-only");
                    ((org.w3c.dom.Element)this.button.getDomElement().getElementsByTagName(SupportedComponents.ION_ICON.toString().toUpperCase()).item(0)).setAttribute("slot", "icon-only");
                    this.textValue.setText("");
                }
            }else if(!this.leftSlot.isSelected() && !this.rightSlot.isSelected()) {
                    this.iconOnlySlot.setSelected(true);
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
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            button.getCssRules().addDeclaration(new CSSDeclaration("left", CSSExpression.createSimple(value + "%")));
            this.posHValue.setText("(" + value + "%)");
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
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            button.getCssRules().addDeclaration(new CSSDeclaration("top", CSSExpression.createSimple(value + "%")));
            this.posVValue.setText("(" + value + "%)");
            String cssRules = aWriter.getCSSAsString (button.getCssRules());
            button.getElement().attr("style", cssRules);
            button.getDomElement().setAttribute("style", cssRules);
        });
    }

    private void setUpScale() {
        horizontalScale.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("width");
                button.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            if (value > 0) {
                button.getCssRules().addDeclaration(new CSSDeclaration("width", CSSExpression.createSimple(value + "%")));
                this.scaleHValue.setText("(" + value + "%)");
            } else {
                this.scaleHValue.setText("(Default)");
            }
            String cssRules = aWriter.getCSSAsString(button.getCssRules());
            button.getElement().attr("style", cssRules);
            button.getDomElement().setAttribute("style", cssRules);
        });

        verticalScale.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = button.getCssRules().getDeclarationOfPropertyName("height");
                button.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            if (value > 0) {
                button.getCssRules().addDeclaration(new CSSDeclaration("height", CSSExpression.createSimple(value + "%")));
                this.scaleVValue.setText("(" + value + "%)");
            } else {
                this.scaleVValue.setText("(Default)");
            }
            String cssRules = aWriter.getCSSAsString(button.getCssRules());
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
            this.iconOnlySlot.setDisable(false);
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
        setButtonScale();


        if (this.button.getElement().hasAttr("[routerLink]")) {
            this.buttonAction.getSelectionModel().select(1);
        } else {
            try {
                JsonObject pageSettingsObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder + "/conf.json")).getAsJsonObject();
                if (pageSettingsObject.getAsJsonObject("actions").has(this.button.getElement().id())) {
                    JsonObject buttonActionObject = pageSettingsObject.getAsJsonObject("actions").getAsJsonObject(this.button.getElement().id());
                    if (buttonActionObject.get("platform").getAsString().equals("facebook")) {
                        this.buttonAction.getSelectionModel().select(2);
                    } else {
                        this.buttonAction.getSelectionModel().select(3);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
            String iconName = iconJsoupElement.attr("name");
            this.icon.getSelectionModel().select(new IonIcon(iconName));

            if(!iconJsoupElement.hasAttr("slot") || iconJsoupElement.attr("slot").equals("start")) {
                this.leftSlot.setSelected(true);
                this.rightSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
            }else if(iconJsoupElement.attr("slot").equals("right")) {
                this.rightSlot.setSelected(true);
                this.leftSlot.setSelected(false);
                this.iconOnlySlot.setSelected(false);
            } else {
                this.iconOnlySlot.setSelected(true);
                this.rightSlot.setSelected(false);
                this.leftSlot.setSelected(false);
            }
        }else {
            this.leftSlot.setDisable(true);
            this.rightSlot.setDisable(true);
            this.iconOnlySlot.setDisable(true);
        }
    }

    private void setButtonPosition() {
        try {
            String horizontalString = CssRuleExtractor.extractValue(button.getCssRules(), "left");
            double hPos = Double.parseDouble(horizontalString.substring(0,horizontalString.length()-1));
            this.horizontalPosition.setValue(hPos);
        }catch (Exception e){
            this.horizontalPosition.setValue(0.1);
            this.horizontalPosition.setValue(0);
        }

        try {
            String verticalString = CssRuleExtractor.extractValue(button.getCssRules(), "top");
            double vPos = Double.parseDouble(verticalString.substring(0, verticalString.length()-1));
            this.verticalPosition.setValue(vPos);
        }catch (Exception e){
            this.verticalPosition.setValue(0.1);
            this.verticalPosition.setValue(0);
        }
    }

    private void setButtonScale() {
        try {
            String horizontalString = CssRuleExtractor.extractValue(button.getCssRules(), "width");
            double hPos = Double.parseDouble(horizontalString.substring(0, horizontalString.length() - 1));
            this.horizontalScale.setValue(hPos);
        } catch (Exception e) {
            this.horizontalScale.setValue(0.1);
            this.horizontalScale.setValue(0);
        }

        try {
            String verticalString = CssRuleExtractor.extractValue(button.getCssRules(), "height");
            double vPos = Double.parseDouble(verticalString.substring(0, verticalString.length() - 1));
            this.verticalScale.setValue(vPos);
        } catch (Exception e) {
            this.verticalScale.setValue(0.1);
            this.verticalScale.setValue(0);
        }
    }

    @Override
    public void save() {
        if(settingsControllerInterface != null) {
            settingsControllerInterface.save();
        }else{
            System.out.println("Here");
            deleteActionElement();
        }
        PreviewController.saveDocument();
    }

    private void deleteActionElement(){
        try {
            String pageConfPath = SettingsViewController.pageFolder + File.separator + "conf.json";
            JsonObject jsonObject = new JsonParser().parse(new FileReader(pageConfPath)).getAsJsonObject();
            JsonObject actionObject = jsonObject.getAsJsonObject("actions");
            Set<Map.Entry<String, JsonElement>> entries = new HashSet<>(actionObject.entrySet());
            for (Map.Entry<String, JsonElement> entry : entries) {
                actionObject.remove(entry.getKey());
                if (entry.getValue().getAsJsonObject().has("platform") && entry.getValue().getAsJsonObject().get("platform").getAsString().equals("facebook")) {
                    String globalSettingsPath = Drawable.projectPath + File.separator + "state.json";
                    JsonObject globalObject = new JsonParser().parse(new FileReader(globalSettingsPath)).getAsJsonObject();
                    if (globalObject.has("firebase") && globalObject.get("firebase").getAsJsonObject().get("platforms").getAsJsonObject().has("facebook")) {
                        globalObject.get("firebase").getAsJsonObject().get("platforms").getAsJsonObject().remove("facebook");

                        Files.write(Paths.get(globalSettingsPath), new Gson().toJson(globalObject).getBytes());
                    }
                }
            }

            if (this.button.getElement().hasAttr("(click)")) {
                this.button.getElement().removeAttr("(click)");
            }

            Files.write(Paths.get(pageConfPath), new Gson().toJson(jsonObject).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


        /*try{
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
        }*/
    }
}
