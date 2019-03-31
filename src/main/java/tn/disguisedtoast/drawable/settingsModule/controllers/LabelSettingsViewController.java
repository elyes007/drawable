package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;
import tn.disguisedtoast.drawable.settingsModule.utils.CustomColorPicker;
import tn.disguisedtoast.drawable.settingsModule.utils.FxUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LabelSettingsViewController implements Initializable, SettingsControllerInterface {
    @FXML public TextField textValue;
    @FXML public ToggleButton boldButton;
    @FXML public ToggleButton italicButton;
    @FXML public ToggleButton underlinedButton;
    @FXML public ComboBox textSize;
    @FXML public Label fontColorPane;

    @FXML public Slider horizontalPosition;
    @FXML public Slider verticalPosition;

    private CustomColorPicker textColor;
    private GeneratedElement element;

    private Integer[] textSizes = {10, 12, 14, 18, 24, 36, 48, 64, 72, 96};
    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings(ECSSVersion.CSS30, false));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset (StandardCharsets.UTF_8.name ());
        initTextView();
        initPosition();
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
            element.getElement().text(this.textValue.getText());
            element.getDomElement().setTextContent(this.textValue.getText());
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
            element.getCssRules().addDeclaration(new CSSDeclaration("left", CSSExpression.createSimple(newValue+"%")));
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
            element.getCssRules().addDeclaration(new CSSDeclaration("top", CSSExpression.createSimple(newValue+"%")));
            String cssString = aWriter.getCSSAsString (element.getCssRules());
            element.getElement().attr("style", cssString);
            element.getDomElement().setAttribute("style", cssString);
        });
    }

    public void setLabel(GeneratedElement element) {
        this.element = element;
        System.out.println(this.element);
        this.textValue.setText(element.getElement().text().trim());

        getLabelFontSize();
        getLabelFontColor();
        getLabelIsBold();
        getLabelIsItalic();
        getLabelIsUnderlined();

        getLabelPosition();
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
