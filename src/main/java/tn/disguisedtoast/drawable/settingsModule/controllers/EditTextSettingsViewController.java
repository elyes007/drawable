package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;
import tn.disguisedtoast.drawable.settingsModule.utils.CustomColorPicker;
import tn.disguisedtoast.drawable.settingsModule.utils.DomUtils;
import tn.disguisedtoast.drawable.settingsModule.utils.FxUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ResourceBundle;

public class EditTextSettingsViewController implements Initializable, SettingsControllerInterface {
    @FXML public Slider horizontalPosition;
    @FXML public Slider verticalPosition;

    /* LABEL SETTING */
    @FXML public CheckBox hasLabel;
    @FXML public ComboBox labelPosition;
    @FXML public TextField labelText;
    @FXML public ToggleButton labelBold;
    @FXML public ToggleButton labelItalic;
    @FXML public ToggleButton labelUnderlined;
    @FXML public ComboBox labelSize;
    @FXML public Label labelFontColorPane;
    @FXML public Label labelPositionLabel;
    @FXML public Label labelTextLabel;
    public ColorPicker labelColor;

    /* INPUT SETTING */
    @FXML public ComboBox inputType;
    @FXML public TextField inputText;
    @FXML public ToggleButton inputBold;
    @FXML public ToggleButton inputItalic;
    @FXML public ToggleButton inputUnderlined;
    @FXML public ComboBox inputSize;
    @FXML public Label inputFontColorPane;
    @FXML public TextField inputPlaceholder;

    @FXML
    private Slider horizontalScale;
    @FXML
    private Label posHValue;
    @FXML
    private Label posVValue;
    @FXML
    private Label scaleHValue;
    @FXML
    private Label deleteButton;

    public ColorPicker inputColor;

    private Integer[] textSizes = {10, 12, 14, 18, 24, 36, 48, 64, 72, 96};
    private String[] inputTypes = {"Text", "Password", "Email", "Number", "Search", "Tel", "URL"};
    private String[] labelPositions = {"Undefined", "Floating", "Fixed", "Stacked"};

    private GeneratedElement generatedElement;

    private GeneratedElement inputGeneratedElement;

    private GeneratedElement labeleGeneratedElement;
    /*private CSSDeclarationList labelElementCssRules;
    private Element labelElement;*/
    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings(ECSSVersion.CSS30, false));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset (StandardCharsets.UTF_8.name ());

        this.deleteButton.setOnMouseClicked(event -> {
            this.generatedElement.getElement().remove();
            PreviewController.refresh();
            SettingsViewController.getInstance().clearSettingView();
        });

        initInputTextView();
        initInputTypesView();
        initInputPlaceholderView();

        initLabelTextView();
        initHasLabelView();
        initLabelPosition();

        setUpPosition();
        setUpScale();
    }


    private void initInputTextView() {
        this.inputSize.getItems().add("Default");
        this.inputSize.getItems().addAll(Arrays.asList(textSizes));
        this.inputSize.getSelectionModel().selectFirst();

        this.inputColor = new CustomColorPicker();
        this.inputColor.setTooltip(new Tooltip("Select font color."));
        this.inputFontColorPane.setGraphic(this.inputColor);

        this.inputText.setOnKeyReleased(event -> {
            this.inputGeneratedElement.getElement().attr("value", inputText.getText());
            this.inputGeneratedElement.getDomElement().setAttribute("value", inputText.getText());
        });

        this.inputSize.setOnAction(event -> {
            try {
                CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(inputGeneratedElement.getElement());
                try {
                    CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("font-size");
                    cssRules.removeDeclaration(declaration);
                }catch (NullPointerException e) {
                    System.out.println(e);
                }
                if(!inputSize.getValue().equals("Default") && inputSize.getValue().toString().matches("\\d+(\\.\\d+)?")) {
                    cssRules.addDeclaration(new CSSDeclaration("font-size", CSSExpression.createSimple(inputSize.getValue()+"px")));
                } else {
                    inputSize.getSelectionModel().selectFirst();
                }
                String cssString = aWriter.getCSSAsString(cssRules);
                this.inputGeneratedElement.getElement().attr("style", cssString);
                this.inputGeneratedElement.getDomElement().setAttribute("style", cssString);
            }catch (NumberFormatException ex){
                ex.printStackTrace();
                //textSize.setValue((int)button.getFont().getSize());
            }
        });

        this.inputColor.valueProperty().addListener((observable, oldValue, newValue) -> {
            CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.inputGeneratedElement.getElement());
            try {
                CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("color");
                cssRules.removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(!this.inputColor.getValue().equals(Color.TRANSPARENT)) {
                cssRules.addDeclaration(new CSSDeclaration("color", CSSExpression.createSimple(FxUtils.toRGBCode(this.inputColor.getValue())+" !important")));
            }
            String cssString = aWriter.getCSSAsString (cssRules);
            this.inputGeneratedElement.getElement().attr("style", cssString);
            this.inputGeneratedElement.getDomElement().setAttribute("style", cssString);
        });

        this.inputBold.setOnAction(event -> {
            CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.inputGeneratedElement.getElement());
            try {
                CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("font-weight");
                cssRules.removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.inputBold.isSelected()) {
                cssRules.addDeclaration(new CSSDeclaration("font-weight", CSSExpression.createSimple("bold")));
            }
            String cssString = aWriter.getCSSAsString (cssRules);
            this.inputGeneratedElement.getElement().attr("style", cssString);
            this.inputGeneratedElement.getDomElement().setAttribute("style", cssString);
        });

        this.inputItalic.setOnAction(event -> {
            CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.inputGeneratedElement.getElement());
            try {
                CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("font-style");
                cssRules.removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.inputItalic.isSelected()) {
                cssRules.addDeclaration(new CSSDeclaration("font-style", CSSExpression.createSimple("italic")));
            }
            String cssString = aWriter.getCSSAsString (cssRules);
            this.inputGeneratedElement.getElement().attr("style", cssString);
            this.inputGeneratedElement.getDomElement().setAttribute("style", cssString);
        });

        this.inputUnderlined.setOnAction(event -> {
            CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.inputGeneratedElement.getElement());
            try {
                CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("text-decoration");
                cssRules.removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(inputUnderlined.isSelected()) {
                cssRules.addDeclaration(new CSSDeclaration("text-decoration", CSSExpression.createSimple("underline")));
            }
            String cssString = aWriter.getCSSAsString (cssRules);
            this.inputGeneratedElement.getElement().attr("style", cssString);
            this.inputGeneratedElement.getDomElement().setAttribute("style", cssString);
        });
    }
    private void initInputTypesView() {
        this.inputType.getItems().addAll(Arrays.asList(this.inputTypes));
        this.inputType.getSelectionModel().selectFirst();

        this.inputType.setOnAction(event -> {
            this.inputGeneratedElement.getElement().attr("type", this.inputType.getValue().toString());
            this.inputGeneratedElement.getDomElement().setAttribute("type", this.inputType.getValue().toString());
        });
    }
    private void initInputPlaceholderView() {
        this.inputPlaceholder.setOnKeyReleased(event -> {
            this.inputGeneratedElement.getElement().attr("placeholder", this.inputPlaceholder.getText());
            this.inputGeneratedElement.getDomElement().setAttribute("placeholder", this.inputPlaceholder.getText());
        });
    }


    private void initLabelTextView() {
        this.labelSize.getItems().add("Default");
        this.labelSize.getItems().addAll(Arrays.asList(textSizes));
        this.labelSize.getSelectionModel().selectFirst();

        this.labelColor = new CustomColorPicker();
        this.labelColor.setTooltip(new Tooltip("Select font color."));
        this.labelFontColorPane.setGraphic(this.labelColor);

        this.labelText.setOnKeyReleased(event -> {
            labeleGeneratedElement.getElement().text(this.labelText.getText());
            labeleGeneratedElement.getDomElement().setTextContent(this.labelText.getText());
        });

        this.labelSize.setOnAction(event -> {
            try {
                CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.labeleGeneratedElement.getElement());
                try {
                    CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("font-size");
                    cssRules.removeDeclaration(declaration);
                }catch (NullPointerException e) {
                    System.out.println(e);
                }
                if(!labelSize.getValue().equals("Default") && labelSize.getValue().toString().matches("\\d+(\\.\\d+)?")) {
                    cssRules.addDeclaration(new CSSDeclaration("font-size", CSSExpression.createSimple(labelSize.getValue()+"px")));
                } else {
                    labelSize.getSelectionModel().selectFirst();
                }
                String cssString = aWriter.getCSSAsString (cssRules);
                this.labeleGeneratedElement.getElement().attr("style", cssString);
                this.labeleGeneratedElement.getDomElement().setAttribute("style", cssString);
            }catch (NumberFormatException ex){
                ex.printStackTrace();
            }
        });

        this.labelColor.valueProperty().addListener((observable, oldValue, newValue) -> {
            CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.labeleGeneratedElement.getElement());
            try {
                CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("color");
                cssRules.removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(!newValue.equals(Color.TRANSPARENT)) {
                cssRules.addDeclaration(new CSSDeclaration("color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue)+" !important")));
            }
            String cssString = aWriter.getCSSAsString (cssRules);
            this.labeleGeneratedElement.getElement().attr("style", cssString);
            this.labeleGeneratedElement.getDomElement().setAttribute("style", cssString);
        });

        this.labelBold.setOnAction(event -> {
            CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.labeleGeneratedElement.getElement());
            try {
                CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("font-weight");
                cssRules.removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.labelBold.isSelected()) {
                cssRules.addDeclaration(new CSSDeclaration("font-weight", CSSExpression.createSimple("bold")));
            }
            String cssString = aWriter.getCSSAsString (cssRules);
            this.labeleGeneratedElement.getElement().attr("style", cssString);
            this.labeleGeneratedElement.getDomElement().setAttribute("style", cssString);
        });

        this.labelItalic.setOnAction(event -> {
            CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.labeleGeneratedElement.getElement());
            try {
                CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("font-style");
                cssRules.removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.labelItalic.isSelected()) {
                cssRules.addDeclaration(new CSSDeclaration("font-style", CSSExpression.createSimple("italic")));
            }
            String cssString = aWriter.getCSSAsString (cssRules);
            this.labeleGeneratedElement.getElement().attr("style", cssString);
            this.labeleGeneratedElement.getDomElement().setAttribute("style", cssString);
        });

        this.labelUnderlined.setOnAction(event -> {
            CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.labeleGeneratedElement.getElement());
            try {
                CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("text-decoration");
                cssRules.removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(labelUnderlined.isSelected()) {
                cssRules.addDeclaration(new CSSDeclaration("text-decoration", CSSExpression.createSimple("underline")));
            }
            String cssString = aWriter.getCSSAsString (cssRules);
            this.labeleGeneratedElement.getElement().attr("style", cssString);
            this.labeleGeneratedElement.getDomElement().setAttribute("style", cssString);
        });
    }
    private void initLabelPosition() {
        this.labelPosition.getItems().addAll(Arrays.asList(labelPositions));
        this.labelPosition.getSelectionModel().selectFirst();

        this.labelPosition.setOnAction(event -> {
            String positionString = this.labelPosition.getValue().toString().toLowerCase();
            this.labeleGeneratedElement.getElement().attr("position", positionString);
            this.labeleGeneratedElement.getDomElement().setAttribute("position", positionString);
        });
    }
    private void initHasLabelView() {
        this.hasLabel.setOnAction(event -> {
            disable_enableLabelParams(!this.hasLabel.isSelected());
            if(!this.hasLabel.isSelected()){
                resetLabelParams();
                this.labeleGeneratedElement.getElement().text("");
                this.labeleGeneratedElement.getDomElement().setTextContent("");
            }else{
                this.labeleGeneratedElement.getElement().text("Input Label");
                this.labeleGeneratedElement.getDomElement().setTextContent("Input Label");
                labelText.setText("Input Label");
            }
        });
    }

    private void resetLabelParams() {
        this.labelPosition.getSelectionModel().selectFirst();
        this.labelText.setText("");
        this.labelBold.setSelected(false);
        this.labelItalic.setSelected(false);
        this.labelUnderlined.setSelected(false);
        this.labelSize.getSelectionModel().selectFirst();
        this.labelColor.setValue(Color.TRANSPARENT);
    }

    private void setUpPosition() {
        horizontalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = this.generatedElement.getCssRules().getDeclarationOfPropertyName("left");
                this.generatedElement.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            this.generatedElement.getCssRules().addDeclaration(new CSSDeclaration("left", CSSExpression.createSimple(value + "%")));
            this.posHValue.setText("(" + value + "%)");
            String cssString = aWriter.getCSSAsString (generatedElement.getCssRules());
            this.generatedElement.getElement().attr("style", cssString);
            this.generatedElement.getDomElement().setAttribute("style", cssString);
        });

        verticalPosition.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = generatedElement.getCssRules().getDeclarationOfPropertyName("top");
                generatedElement.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            generatedElement.getCssRules().addDeclaration(new CSSDeclaration("top", CSSExpression.createSimple(value + "%")));
            this.posVValue.setText("(" + value + "%)");
            String cssString = aWriter.getCSSAsString (generatedElement.getCssRules());
            generatedElement.getElement().attr("style", cssString);
            generatedElement.getDomElement().setAttribute("style", cssString);
        });
    }

    private void setUpScale() {
        horizontalScale.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = generatedElement.getCssRules().getDeclarationOfPropertyName("width");
                generatedElement.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            double value = Math.round(newValue.doubleValue() * 10) / 10.0;
            if (value > 0) {
                generatedElement.getCssRules().addDeclaration(new CSSDeclaration("width", CSSExpression.createSimple(value + "%")));
                this.scaleHValue.setText("(" + value + "%)");
            } else {
                this.scaleHValue.setText("(Default)");
            }
            String cssRules = aWriter.getCSSAsString(generatedElement.getCssRules());
            generatedElement.getElement().attr("style", cssRules);
            generatedElement.getDomElement().setAttribute("style", cssRules);
        });
    }


    public void setTextField(GeneratedElement element){
        this.generatedElement = element;

        this.inputGeneratedElement = new GeneratedElement(
                this.generatedElement.getElement().selectFirst(SupportedComponents.ION_INPUT.toString()),
                (Element) DomUtils.getChildNode(SupportedComponents.ION_INPUT.toString().toUpperCase(), this.generatedElement.getDomElement())
        );

        this.labeleGeneratedElement = new GeneratedElement(
                this.generatedElement.getElement().selectFirst(SupportedComponents.ION_LABEL.toString()),
                (Element) DomUtils.getChildNode(SupportedComponents.ION_LABEL.toString().toUpperCase(), this.generatedElement.getDomElement())
        );

        this.inputText.setText(this.inputGeneratedElement.getElement().attr("value"));
        this.labelText.setText(this.labeleGeneratedElement.getElement().text());
        //System.out.println(DomUtils.getChildNode("#text", this.labelElement).getNodeValue());

        /* INPUT PARAMS */
        getEditTextFontSize();
        getEditTextFontColor();
        getEditTextIsBold();
        getEditTextIsItalic();
        getEditTextIsUnderlined();
        getInputType();
        getInputPlaceholder();

        /* LABEL PARAMS */
        getLabelFontSize();
        getLabelFontColor();
        getLabelIsBold();
        getLabelIsItalic();
        getLabelIsUnderlined();
        getHasLabel();
        getLabelPosition();
        getEditTextScale();

        setEditTextPosition();
    }

    private void getHasLabel() {
        boolean withLabel = this.labeleGeneratedElement.getElement() != null;
        this.hasLabel.setSelected(withLabel);
        disable_enableLabelParams(!withLabel);
    }

    private void getLabelPosition() {
        String labelPositionString = this.labeleGeneratedElement.getElement().attr("position");
        if(labelPositionString == null || labelPositionString.isEmpty()) {
            this.labelPosition.getSelectionModel().selectFirst();
            return;
        }
        this.labelPosition.setValue(labelPositionString.substring(0, 1).toUpperCase() + labelPositionString.substring(1));
    }

    private void getEditTextScale() {
        try {
            String horizontalString = CssRuleExtractor.extractValue(generatedElement.getCssRules(), "width");
            double hPos = Double.parseDouble(horizontalString.substring(0, horizontalString.length() - 1));
            this.horizontalScale.setValue(hPos);
        } catch (Exception e) {
            this.horizontalScale.setValue(0.1);
            this.horizontalScale.setValue(0);
        }
    }

    private void disable_enableLabelParams(boolean active) {
        this.labelPosition.setDisable(active);
        this.labelText.setDisable(active);
        this.labelBold.setDisable(active);
        this.labelItalic.setDisable(active);
        this.labelUnderlined.setDisable(active);
        this.labelSize.setDisable(active);
        this.labelColor.setDisable(active);
        this.labelPositionLabel.setDisable(active);
        this.labelTextLabel.setDisable(active);
    }

    private void getInputType() {
        String type = this.inputGeneratedElement.getElement().attr("type");
        if(type == null || type.isEmpty()) {
            type = "text";
        }
        this.inputType.setValue(type.substring(0, 1).toUpperCase() + type.substring(1));
    }

    private void getInputPlaceholder() {
        this.inputPlaceholder.setText(this.inputGeneratedElement.getElement().attr("placeholder"));
    }

    private void getEditTextFontSize() {
        try{
            String fontSizeString = CssRuleExtractor.extractValue(this.inputGeneratedElement.getCssRules(), "font-size");
            this.inputSize.setValue(Integer.parseInt(fontSizeString.replaceAll("\\D+","")));
        } catch (NullPointerException e){
        }
    }

    private void getEditTextFontColor() {
        try {
            String fontColorString = CssRuleExtractor.extractValue(this.inputGeneratedElement.getCssRules(), "color");
            System.out.println(fontColorString);
            this.inputColor.setValue(Color.valueOf(fontColorString));
        }catch (NullPointerException e){
            this.inputColor.setValue(Color.TRANSPARENT);
        }
    }

    private void getEditTextIsBold() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(this.inputGeneratedElement.getCssRules(), "font-weight");
            if(fontWeightString.equals("bold")) {
                this.inputBold.setSelected(true);
                return;
            }
        }catch (NullPointerException e){
        }
        this.inputBold.setSelected(false);
    }

    private void getEditTextIsItalic() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(this.inputGeneratedElement.getCssRules(), "font-style");
            if(fontWeightString.equals("italic")) {
                this.inputItalic.setSelected(true);
                return;
            }
        }catch (NullPointerException e){
        }
        this.inputItalic.setSelected(false);
    }

    private void getEditTextIsUnderlined() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(this.inputGeneratedElement.getCssRules(), "text-decoration");
            if(fontWeightString.equals("underline")) {
                this.inputUnderlined.setSelected(true);
                return;
            }
        }catch (NullPointerException e){
        }
        this.inputUnderlined.setSelected(false);
    }

    private void getLabelFontSize() {
        try{
            String fontSizeString = CssRuleExtractor.extractValue(labeleGeneratedElement.getCssRules(), "font-size");
            this.labelSize.setValue(Integer.parseInt(fontSizeString.replaceAll("\\D+","")));
        } catch (NullPointerException e){
        }
    }

    private void getLabelFontColor() {
        try {
            String fontColorString = CssRuleExtractor.extractValue(labeleGeneratedElement.getCssRules(), "color");
            this.labelColor.setValue(Color.valueOf(fontColorString));
        }catch (NullPointerException e){
            this.labelColor.setValue(Color.TRANSPARENT);
        }
    }

    private void getLabelIsBold() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(labeleGeneratedElement.getCssRules(), "font-weight");
            if(fontWeightString.equals("bold")) {
                this.labelBold.setSelected(true);
                return;
            }
        }catch (NullPointerException e){
        }
        this.labelBold.setSelected(false);
    }

    private void getLabelIsItalic() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(labeleGeneratedElement.getCssRules(), "font-style");
            if(fontWeightString.equals("italic")) {
                this.labelItalic.setSelected(true);
                return;
            }
        }catch (NullPointerException e){
        }
        this.labelItalic.setSelected(false);
    }

    private void getLabelIsUnderlined() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(labeleGeneratedElement.getCssRules(), "text-decoration");
            if(fontWeightString.equals("underline")) {
                this.labelUnderlined.setSelected(true);
                return;
            }
        }catch (NullPointerException e){
        }
        this.labelUnderlined.setSelected(false);
    }

    private void setEditTextPosition() {
        try {
            String horizontalString = CssRuleExtractor.extractValue(generatedElement.getCssRules(), "left");
            double hPos = Double.parseDouble(horizontalString.substring(0,horizontalString.length()-1));
            this.horizontalPosition.setValue(hPos);
        }catch (Exception e){
            this.horizontalPosition.setValue(0);
        }

        try {
            String verticalString = CssRuleExtractor.extractValue(generatedElement.getCssRules(), "top");
            double vPos = Double.parseDouble(verticalString.substring(0, verticalString.length()-1));
            this.verticalPosition.setValue(vPos);
        }catch (Exception e){
            this.verticalPosition.setValue(0);
        }
    }

    @Override
    public void save() {
        PreviewController.saveDocument();
    }
}
