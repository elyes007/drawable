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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.w3c.dom.Element;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;
import tn.disguisedtoast.drawable.settingsModule.utils.CustomColorPicker;
import tn.disguisedtoast.drawable.settingsModule.utils.DomUtils;
import tn.disguisedtoast.drawable.settingsModule.utils.FxUtils;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ToolbarSettingsViewController implements Initializable {
    @FXML public CheckBox hasBackButton;
    @FXML public ComboBox toolbarTheme;
    @FXML public TextField titleText;
    @FXML public ToggleButton titleBold;
    @FXML public ToggleButton titleItalic;
    @FXML public ToggleButton titleUnderlined;
    @FXML public ComboBox titleSize;
    @FXML public Label titleFontColorPane;
    private CustomColorPicker toolbarColorPicker;
    private CustomColorPicker titleColorPicker;

    private GeneratedElement toolbarElement;

    private Integer[] textSizes = {10, 12, 14, 18, 24, 36, 48, 64, 72, 96};
    private String[] themes = {"Custom", "Primary", "Secondary", "Tertiary", "Success", "Warning", "Danger", "Light", "Medium", "Dark"};

    private GeneratedElement titleElement;
    private GeneratedElement buttonsElement;
    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings(ECSSVersion.CSS30, false));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset (StandardCharsets.UTF_8.name ());

        initToolBarThemeAndColor();
        initTextView();

        this.hasBackButton.setOnAction(event -> {
            DomUtils.removeAllChilds(this.buttonsElement.getElement());
            if(this.hasBackButton.isSelected()) {
                Element backButtonElement = this.buttonsElement.getElement().getOwnerDocument().createElement(SupportedComponents.ION_BACK_BUTTON.toString());
                backButtonElement.setAttribute("default-href", "");
                this.buttonsElement.getElement().appendChild(backButtonElement);
            }
        });
    }

    private void initToolBarThemeAndColor() {
        this.toolbarTheme.getItems().addAll(Arrays.asList(this.themes));
        this.toolbarColorPicker = new CustomColorPicker();
        this.toolbarColorPicker.setValue(Color.TRANSPARENT);
        ((HBox)this.toolbarTheme.getParent()).getChildren().add(this.toolbarColorPicker);

        this.toolbarTheme.setOnAction(event -> {
            String selectedTheme = (String)this.toolbarTheme.getValue();
            this.toolbarColorPicker.setValue(Color.TRANSPARENT);
            if(selectedTheme.equals("Custom")) {
                this.toolbarElement.getElement().removeAttribute("color");
                this.toolbarColorPicker.setDisable(false);
            }else {
                this.toolbarElement.getElement().setAttribute("color", selectedTheme.toLowerCase());
                this.toolbarColorPicker.setDisable(true);
            }
        });

        this.toolbarColorPicker.setOnAction(event -> {
            CSSDeclarationList cssRules = CssRuleExtractor.getCssRules(this.toolbarElement.getElement());
            try {
                CSSDeclaration declaration = cssRules.getDeclarationOfPropertyName("--ion-background-color");
                cssRules.removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(!this.toolbarColorPicker.getValue().equals(Color.TRANSPARENT)) {
                cssRules.addDeclaration(new CSSDeclaration("--ion-background-color", CSSExpression.createSimple(FxUtils.toRGBCode(this.toolbarColorPicker.getValue())+" !important")));
            }
            this.toolbarElement.getElement().setAttribute("style", aWriter.getCSSAsString (cssRules));
        });

    }

    private void initTextView() {
        this.titleSize.getItems().add("Default");
        this.titleSize.getItems().addAll(Arrays.asList(textSizes));
        this.titleSize.getSelectionModel().selectFirst();

        this.titleColorPicker = new CustomColorPicker();
        this.titleColorPicker.setTooltip(new Tooltip("Select font color."));
        this.titleFontColorPane.setGraphic(this.titleColorPicker);

        this.titleText.setOnKeyReleased(event -> {
            this.titleElement.getElement().setTextContent(this.titleText.getText());
        });

        this.titleSize.setOnAction(event -> {
            try {
                try {
                    CSSDeclaration declaration = this.titleElement.getCssRules().getDeclarationOfPropertyName("font-size");
                    this.titleElement.getCssRules().removeDeclaration(declaration);
                }catch (NullPointerException e) {
                    System.out.println(e);
                }
                if(!titleSize.getValue().equals("Default") && titleSize.getValue().toString().matches("\\d+(\\.\\d+)?")) {
                    this.titleElement.getCssRules().addDeclaration(new CSSDeclaration("font-size", CSSExpression.createSimple(titleSize.getValue()+"px")));
                } else {
                    titleSize.getSelectionModel().selectFirst();
                }
                this.titleElement.getElement().setAttribute("style", aWriter.getCSSAsString (this.titleElement.getCssRules()));
            }catch (NumberFormatException ex){
                ex.printStackTrace();
                //textSize.setValue((int)button.getFont().getSize());
            }
        });

        this.titleColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = this.titleElement.getCssRules().getDeclarationOfPropertyName("color");
                this.titleElement.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(!newValue.equals(Color.TRANSPARENT)) {
                this.titleElement.getCssRules().addDeclaration(new CSSDeclaration("color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue) +" !important")));
            }
            this.titleElement.getElement().setAttribute("style", aWriter.getCSSAsString (this.titleElement.getCssRules()));
        });

        this.titleBold.setOnAction(event -> {
            try {
                CSSDeclaration declaration = this.titleElement.getCssRules().getDeclarationOfPropertyName("font-weight");
                this.titleElement.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.titleBold.isSelected()) {
                this.titleElement.getCssRules().addDeclaration(new CSSDeclaration("font-weight", CSSExpression.createSimple("bold")));
            }
            this.titleElement.getElement().setAttribute("style", aWriter.getCSSAsString (this.titleElement.getCssRules()));
        });

        this.titleItalic.setOnAction(event -> {
            try {
                CSSDeclaration declaration = this.titleElement.getCssRules().getDeclarationOfPropertyName("font-style");
                this.titleElement.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.titleItalic.isSelected()) {
                this.titleElement.getCssRules().addDeclaration(new CSSDeclaration("font-style", CSSExpression.createSimple("italic")));
            }
            this.titleElement.getElement().setAttribute("style", aWriter.getCSSAsString (this.titleElement.getCssRules()));
        });

        this.titleUnderlined.setOnAction(event -> {
            try {
                CSSDeclaration declaration = this.titleElement.getCssRules().getDeclarationOfPropertyName("text-decoration");
                this.titleElement.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {}
            if(this.titleUnderlined.isSelected()) {
                this.titleElement.getCssRules().addDeclaration(new CSSDeclaration("text-decoration", CSSExpression.createSimple("underline")));
            }
            this.titleElement.getElement().setAttribute("style", aWriter.getCSSAsString (this.titleElement.getCssRules()));
        });
    }

    public void setToolbarElement(GeneratedElement element) {
        this.toolbarElement = element;

        this.titleElement = new GeneratedElement((Element) DomUtils.getChildNode(SupportedComponents.ION_TITLE.toString(), this.toolbarElement.getElement()));

        this.buttonsElement = new GeneratedElement((Element) DomUtils.getChildNode(SupportedComponents.ION_BUTTONS.toString(), this.toolbarElement.getElement()));

        this.hasBackButton.setSelected(DomUtils.getChildNode(SupportedComponents.ION_BACK_BUTTON.toString(), this.buttonsElement.getElement()) != null);

        this.titleText.setText(this.titleElement.getElement().getTextContent());

        getToolbarColor();
        getTitleFontSize();
        getTitleFontColor();
        getTitleIsBold();
        getTitleIsItalic();
        getTitleIsUnderlined();
    }

    private void getToolbarColor() {
        if(this.toolbarElement.getElement().hasAttribute("color")) {
            String theme = this.toolbarElement.getElement().getAttribute("color");
            this.toolbarTheme.setValue(theme.substring(0, 1).toUpperCase() + theme.substring(1));
            this.toolbarColorPicker.setDisable(true);
            return;
        }
        this.toolbarTheme.setValue("Custom");
        this.toolbarColorPicker.setDisable(false);
        try {
            this.toolbarColorPicker.setValue(Color.valueOf(CssRuleExtractor.extractValue(this.toolbarElement.getCssRules(), "--ion-background-color")));
        }catch (NullPointerException e) {
            this.toolbarColorPicker.setValue(Color.TRANSPARENT);
        }
    }

    private void getTitleFontSize() {
        try{
            String fontSizeString = CssRuleExtractor.extractValue(this.titleElement.getCssRules(), "font-size");
            this.titleSize.setValue(Integer.parseInt(fontSizeString.replaceAll("\\D+","")));
        } catch (NullPointerException e){}
    }

    private void getTitleFontColor() {
        try {
            String fontColorString = CssRuleExtractor.extractValue(this.titleElement.getCssRules(), "color");
            this.titleColorPicker.setValue(Color.valueOf(fontColorString));
        }catch (NullPointerException e){
            this.titleColorPicker.setValue(Color.TRANSPARENT);
        }
    }

    private void getTitleIsBold() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(this.titleElement.getCssRules(), "font-weight");
            if(fontWeightString.equals("bold")) {
                this.titleBold.setSelected(true);
                return;
            }
        }catch (NullPointerException e){ }
        this.titleBold.setSelected(false);
    }

    private void getTitleIsItalic() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(this.titleElement.getCssRules(), "font-style");
            if(fontWeightString.equals("italic")) {
                this.titleItalic.setSelected(true);
                return;
            }
        }catch (NullPointerException e){ }
        this.titleItalic.setSelected(false);
    }

    private void getTitleIsUnderlined() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(this.titleElement.getCssRules(), "text-decoration");
            if(fontWeightString.equals("underline")) {
                this.titleUnderlined.setSelected(true);
                return;
            }
        }catch (NullPointerException e){ }
        this.titleUnderlined.setSelected(false);
    }
}
