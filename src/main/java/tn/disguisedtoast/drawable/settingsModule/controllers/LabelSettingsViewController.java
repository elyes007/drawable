package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;
import tn.disguisedtoast.drawable.settingsModule.utils.CustomColorPicker;
import tn.disguisedtoast.drawable.settingsModule.utils.FxUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LabelSettingsViewController implements Initializable, SettingsControllerInterface {
    @FXML
    public ComboBox textValue;
    @FXML public ToggleButton boldButton;
    @FXML public ToggleButton italicButton;
    @FXML public ToggleButton underlinedButton;
    @FXML public ComboBox textSize;
    @FXML public Label fontColorPane;

    @FXML public Slider horizontalPosition;
    @FXML public Slider verticalPosition;
    @FXML
    private Slider horizontalScale;
    @FXML
    private Slider verticalScale;

    @FXML
    private Label posHValue;
    @FXML
    private Label posVValue;
    @FXML
    private Label scaleHValue;
    @FXML
    private Label scaleVValue;

    @FXML
    private Label deleteButton;
    @FXML
    private CheckBox textWrap;

    private CustomColorPicker textColor;
    private GeneratedElement element;

    private Integer[] textSizes = {10, 12, 14, 18, 24, 36, 48, 64, 72, 96};
    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings(ECSSVersion.CSS30, false));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset(StandardCharsets.UTF_8.name());

        this.deleteButton.setOnMouseClicked(event -> {
            this.element.getElement().remove();
            PreviewController.refresh();
            SettingsViewController.getInstance().clearSettingView();
        });

        this.textWrap.setOnAction(event -> {
            try {
                try {
                    CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("white-space");
                    element.getCssRules().removeDeclaration(declaration);
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                if (!this.textWrap.isSelected()) {
                    element.getCssRules().addDeclaration(new CSSDeclaration("white-space", CSSExpression.createSimple("nowrap")));
                }
                String cssString = aWriter.getCSSAsString(element.getCssRules());
                element.getElement().attr("style", cssString);
                element.getDomElement().setAttribute("style", cssString);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        });

        initTextView();
        initPosition();
        initScale();

        try {
            JsonObject globalConfigJson = new JsonParser().parse(new FileReader(Drawable.projectPath + File.separator + "state.json")).getAsJsonObject();
            if (globalConfigJson.has("firebase") && globalConfigJson.get("firebase").getAsJsonObject().get("platforms").getAsJsonObject().has("facebook")) {
                this.textValue.getItems().add("Bind to logged user name");
                this.textValue.getItems().add("Bind to logged user email");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.textValue.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (this.textValue.getValue().equals("Bound to logged user name") || this.textValue.getValue().equals("Bound to logged user email"))
                return;
            try {
                String pageConfPath = SettingsViewController.pageFolder + File.separator + "conf.json";
                JsonObject globalConfigJson = new JsonParser().parse(new FileReader(pageConfPath)).getAsJsonObject();

                if (!this.textValue.getValue().equals("Bind to logged user name") && !this.textValue.getValue().equals("Bind to logged user email")) {
                    this.element.getElement().removeAttr("data-bind");
                    if (globalConfigJson.has("bindings")) {
                        globalConfigJson.getAsJsonObject("bindings").remove(this.element.getElement().id());
                        Files.write(Paths.get(pageConfPath), new Gson().toJson(globalConfigJson).getBytes());
                    }
                    return;
                }

                String binding = "";
                switch (this.textValue.getSelectionModel().getSelectedIndex()) {
                    case 0:
                        String text1 = "Bound to logged user name";
                        Platform.runLater(() -> this.textValue.setValue(text1));
                        element.getElement().text(text1);
                        element.getDomElement().setTextContent(text1);
                        this.element.getElement().attr("data-bind", "name");
                        binding = "name";
                        break;
                    case 1:
                        String text2 = "Bound to logged user email";
                        Platform.runLater(() -> this.textValue.setValue(text2));
                        element.getElement().text(text2);
                        element.getDomElement().setTextContent(text2);
                        this.element.getElement().attr("data-bind", "email");
                        binding = "email";
                        break;
                }

                if (globalConfigJson.has("bindings")) {
                    JsonObject bindingsObject = globalConfigJson.getAsJsonObject("bindings");
                    bindingsObject.addProperty(this.element.getElement().id(), binding);
                } else {
                    JsonObject bindingsObject = new JsonObject();
                    bindingsObject.addProperty(this.element.getElement().id(), binding);
                    globalConfigJson.add("bindings", bindingsObject);
                }
                System.out.println("Writing here " + globalConfigJson);
                Files.write(Paths.get(pageConfPath), new Gson().toJson(globalConfigJson).getBytes());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
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

        this.textValue.getEditor().setOnKeyReleased(event -> {
            element.getElement().text(this.textValue.getEditor().getText());
            element.getDomElement().setTextContent(this.textValue.getEditor().getText());
        });

        this.textSize.setOnAction(event -> {
            try {
                try {
                    CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("font-size");
                    element.getCssRules().removeDeclaration(declaration);
                }catch (NullPointerException e) {
                    System.out.println(e);
                }
                if(!textSize.getValue().equals("Default") && textSize.getValue().toString().matches("\\d+(\\.\\d+)?")) {
                    element.getCssRules().addDeclaration(new CSSDeclaration("font-size", CSSExpression.createSimple(textSize.getValue()+"px")));
                } else {
                    textSize.getSelectionModel().selectFirst();
                }
                String cssString = aWriter.getCSSAsString (element.getCssRules());
                element.getElement().attr("style", cssString);
                element.getDomElement().setAttribute("style", cssString);
            }catch (NumberFormatException ex){
                ex.printStackTrace();
                //textSize.setValue((int)button.getFont().getSize());
            }
        });

        this.textColor.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("color");
                element.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(!newValue.equals(Color.TRANSPARENT)) {
                element.getCssRules().addDeclaration(new CSSDeclaration("color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue) +" !important")));
            }
            String cssString = aWriter.getCSSAsString (element.getCssRules());
            element.getElement().attr("style", cssString);
            element.getDomElement().setAttribute("style", cssString);
        });

        this.boldButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("font-weight");
                element.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.boldButton.isSelected()) {
                element.getCssRules().addDeclaration(new CSSDeclaration("font-weight", CSSExpression.createSimple("bold")));
            }
            String cssString = aWriter.getCSSAsString (element.getCssRules());
            element.getElement().attr("style", cssString);
            element.getDomElement().setAttribute("style", cssString);
        });

        this.italicButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("font-style");
                element.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.italicButton.isSelected()) {
                element.getCssRules().addDeclaration(new CSSDeclaration("font-style", CSSExpression.createSimple("italic")));
            }
            String cssString = aWriter.getCSSAsString (element.getCssRules());
            element.getElement().attr("style", cssString);
            element.getDomElement().setAttribute("style", cssString);
        });

        this.underlinedButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("text-decoration");
                element.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.underlinedButton.isSelected()) {
                element.getCssRules().addDeclaration(new CSSDeclaration("text-decoration", CSSExpression.createSimple("underline")));
            }
            String cssString = aWriter.getCSSAsString (element.getCssRules());
            element.getElement().attr("style", cssString);
            element.getDomElement().setAttribute("style", cssString);
        });
    }

    private void initPosition() {
        horizontalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("left");
                element.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            element.getCssRules().addDeclaration(new CSSDeclaration("left", CSSExpression.createSimple(value + "%")));
            this.posHValue.setText("(" + value + "%)");

            String cssString = aWriter.getCSSAsString (element.getCssRules());
            element.getElement().attr("style", cssString);
            element.getDomElement().setAttribute("style", cssString);
        });

        verticalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("top");
                element.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            element.getCssRules().addDeclaration(new CSSDeclaration("top", CSSExpression.createSimple(newValue+"%")));
            this.posVValue.setText("(" + value + "%)");

            String cssString = aWriter.getCSSAsString (element.getCssRules());
            element.getElement().attr("style", cssString);
            element.getDomElement().setAttribute("style", cssString);
        });
    }

    private void initScale() {
        horizontalScale.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("width");
                element.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            if (value > 0) {
                element.getCssRules().addDeclaration(new CSSDeclaration("width", CSSExpression.createSimple(value + "%")));
                this.scaleHValue.setText("(" + value + "%)");
            } else {
                this.scaleHValue.setText("(Default)");
            }
            String cssRules = aWriter.getCSSAsString(element.getCssRules());
            element.getElement().attr("style", cssRules);
            element.getDomElement().setAttribute("style", cssRules);
        });

        verticalScale.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = element.getCssRules().getDeclarationOfPropertyName("height");
                element.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            if (value > 0) {
                element.getCssRules().addDeclaration(new CSSDeclaration("height", CSSExpression.createSimple(value + "%")));
                this.scaleVValue.setText("(" + value + "%)");
            } else {
                this.scaleVValue.setText("(Default)");
            }
            String cssRules = aWriter.getCSSAsString(element.getCssRules());
            element.getElement().attr("style", cssRules);
            element.getDomElement().setAttribute("style", cssRules);
        });
    }

    public void setLabel(GeneratedElement element) {
        this.element = element;
        System.out.println(this.element);
        this.textValue.setValue(element.getElement().text().trim());
        if (this.element.getElement().hasAttr("data-bind")) {
            if (this.element.getElement().attr("data-bind").equals("name")) {
                this.textValue.getSelectionModel().select(0);
            } else {
                this.textValue.getSelectionModel().select(1);
            }
        }

        try {
            CssRuleExtractor.extractValue(element.getCssRules(), "white-space");
        } catch (Exception e) {
            this.textWrap.setSelected(true);
        }

        getLabelFontSize();
        getLabelFontColor();
        getLabelIsBold();
        getLabelIsItalic();
        getLabelIsUnderlined();

        getLabelPosition();
        getLabelScale();
    }

    private void getLabelPosition() {
        try {
            String horizontalString = CssRuleExtractor.extractValue(element.getCssRules(), "left");
            double hPos = Double.parseDouble(horizontalString.substring(0,horizontalString.length()-1));
            this.horizontalPosition.setValue(hPos);
        }catch (Exception e){
            this.horizontalPosition.setValue(0);
        }

        try {
            String verticalString = CssRuleExtractor.extractValue(element.getCssRules(), "top");
            double vPos = Double.parseDouble(verticalString.substring(0, verticalString.length()-1));
            this.verticalPosition.setValue(vPos);
        }catch (Exception e){
            this.verticalPosition.setValue(0);
        }
    }

    private void getLabelScale() {
        try {
            String horizontalString = CssRuleExtractor.extractValue(element.getCssRules(), "width");
            double hPos = Double.parseDouble(horizontalString.substring(0, horizontalString.length() - 1));
            this.horizontalScale.setValue(hPos);
        } catch (Exception e) {
            this.horizontalScale.setValue(0.1);
            this.horizontalScale.setValue(0);
        }

        try {
            String verticalString = CssRuleExtractor.extractValue(element.getCssRules(), "height");
            double vPos = Double.parseDouble(verticalString.substring(0, verticalString.length() - 1));
            this.verticalScale.setValue(vPos);
        } catch (Exception e) {
            this.verticalScale.setValue(0.1);
            this.verticalScale.setValue(0);
        }
    }

    private void getLabelFontSize() {
        try{
            String fontSizeString = CssRuleExtractor.extractValue(element.getCssRules(), "font-size");
            this.textSize.setValue(Integer.parseInt(fontSizeString.replaceAll("\\D+","")));
        } catch (NullPointerException e){}
    }

    private void getLabelFontColor() {
        try {
            String fontColorString = CssRuleExtractor.extractValue(element.getCssRules(), "color");
            this.textColor.setValue(Color.valueOf(fontColorString));
        }catch (NullPointerException e){
            this.textColor.setValue(Color.TRANSPARENT);
        }
    }

    private void getLabelIsBold() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(element.getCssRules(), "font-weight");
            if(fontWeightString.equals("bold")) {
                this.boldButton.setSelected(true);
                return;
            }
        }catch (NullPointerException e){ }
        this.boldButton.setSelected(false);
    }

    private void getLabelIsItalic() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(element.getCssRules(), "font-style");
            if(fontWeightString.equals("italic")) {
                this.italicButton.setSelected(true);
                return;
            }
        }catch (NullPointerException e){ }
        this.italicButton.setSelected(false);
    }

    private void getLabelIsUnderlined() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(element.getCssRules(), "text-decoration");
            if(fontWeightString.equals("underline")) {
                this.underlinedButton.setSelected(true);
                return;
            }
        }catch (NullPointerException e){ }
        this.underlinedButton.setSelected(false);
    }

    @Override
    public void save() {
        PreviewController.saveDocument();
    }
}
