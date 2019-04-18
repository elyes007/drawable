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
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.w3c.dom.NodeList;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;
import tn.disguisedtoast.drawable.settingsModule.models.IonIcon;
import tn.disguisedtoast.drawable.settingsModule.utils.CssRuleExtractor;
import tn.disguisedtoast.drawable.settingsModule.utils.CustomColorPicker;
import tn.disguisedtoast.drawable.settingsModule.utils.FxUtils;
import tn.disguisedtoast.drawable.settingsModule.utils.IconComboboxCell;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.ResourceBundle;

public class TabSettingsController implements Initializable, SettingsControllerInterface {
    private final CSSWriter aWriter = new CSSWriter(new CSSWriterSettings(ECSSVersion.CSS30, false));

    @FXML
    public TextField textValue;
    @FXML
    public ComboBox textSize;
    @FXML
    public ToggleButton boldButton;
    @FXML
    public ToggleButton italicButton;
    @FXML
    public ToggleButton underlinedButton;
    @FXML
    public Label fontColorPane;
    @FXML
    public ComboBox icon;
    @FXML
    public Label backgroundColorPane;
    @FXML
    public Label colorPane;
    @FXML
    public Label selectedColorPane;
    @FXML
    public Label rippleColorPane;
    public CustomColorPicker textColorPicker;
    public CustomColorPicker backgroundColorPicker;
    public CustomColorPicker colorPicker;
    public CustomColorPicker selectedColorPicker;
    public CustomColorPicker rippleColorPicker;
    @FXML
    private ComboBox barPosition;
    @FXML
    private Label deleteTabButton;
    private Integer[] textSizes = {10, 12, 14, 18, 24, 36, 48, 64, 72, 96};
    private GeneratedElement tab;
    private GeneratedElement tabLabel;
    private GeneratedElement tabElement;
    private GeneratedElement tabBarElement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset(StandardCharsets.UTF_8.name());

        barPosition.getItems().add("Top");
        barPosition.getItems().add("Bottom");
        barPosition.getSelectionModel().select(0);
        barPosition.setOnAction(event -> {
            this.tabBarElement.getElement().attr("slot", barPosition.getValue().toString().toLowerCase());
            this.tabBarElement.getDomElement().setAttribute("slot", barPosition.getValue().toString().toLowerCase());
        });

        deleteTabButton.setOnMouseClicked(event -> {
            tabElement.getElement().remove();
            tabElement.getDomElement().getParentNode().removeChild(tabElement.getDomElement());
            tab.getElement().remove();
            tab.getDomElement().getParentNode().removeChild(tab.getDomElement());
        });

        initTextView();
        initIcon();
        initBackgroundColorView();
        initColorView();
        initSelectedColorView();
        initRippleColorView();
    }

    //Init
    private void initTextView() {
        this.textSize.getItems().add("Default");
        this.textSize.getItems().addAll(Arrays.asList(textSizes));
        this.textSize.getSelectionModel().selectFirst();

        this.textColorPicker = new CustomColorPicker();
        this.textColorPicker.setTooltip(new Tooltip("Select font color."));
        this.fontColorPane.setGraphic(this.textColorPicker);

        this.textValue.textProperty().addListener((observable, oldValue, newValue) -> {
            this.tabLabel.getElement().text(this.textValue.getText());
            this.tabLabel.getDomElement().setTextContent(this.textValue.getText());
        });

        this.textSize.setOnAction(event -> {
            try {
                try {
                    CSSDeclaration declaration = this.tabLabel.getCssRules().getDeclarationOfPropertyName("font-size");
                    this.tabLabel.getCssRules().removeDeclaration(declaration);
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                if (!textSize.getValue().equals("Default") && textSize.getValue().toString().matches("\\d+(\\.\\d+)?")) {
                    this.tabLabel.getCssRules().addDeclaration(new CSSDeclaration("font-size", CSSExpression.createSimple(textSize.getValue() + "px")));
                } else {
                    textSize.getSelectionModel().selectFirst();
                }
                String cssRules = aWriter.getCSSAsString(this.tabLabel.getCssRules());
                this.tabLabel.getElement().attr("style", cssRules);
                this.tabLabel.getDomElement().setAttribute("style", cssRules);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        });

        this.textColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = this.tabLabel.getCssRules().getDeclarationOfPropertyName("color");
                this.tabLabel.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
            }
            if (!newValue.equals(Color.TRANSPARENT)) {
                this.tabLabel.getCssRules().addDeclaration(new CSSDeclaration("color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue) + " !important")));
            }
            String cssRules = aWriter.getCSSAsString(this.tabLabel.getCssRules());
            this.tabLabel.getElement().attr("style", cssRules);
            this.tabLabel.getDomElement().setAttribute("style", cssRules);
        });

        this.boldButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = this.tabLabel.getCssRules().getDeclarationOfPropertyName("font-weight");
                this.tabLabel.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
            }
            if (this.boldButton.isSelected()) {
                this.tabLabel.getCssRules().addDeclaration(new CSSDeclaration("font-weight", CSSExpression.createSimple("bold")));
            }
            String cssRules = aWriter.getCSSAsString(this.tabLabel.getCssRules());
            this.tabLabel.getElement().attr("style", cssRules);
            this.tabLabel.getDomElement().setAttribute("style", cssRules);
        });

        this.italicButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = this.tabLabel.getCssRules().getDeclarationOfPropertyName("font-style");
                this.tabLabel.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
            }
            if (this.italicButton.isSelected()) {
                this.tabLabel.getCssRules().addDeclaration(new CSSDeclaration("font-style", CSSExpression.createSimple("italic")));
            }
            String cssRules = aWriter.getCSSAsString(this.tabLabel.getCssRules());
            this.tabLabel.getElement().attr("style", cssRules);
            this.tabLabel.getDomElement().setAttribute("style", cssRules);
        });

        this.underlinedButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = this.tabLabel.getCssRules().getDeclarationOfPropertyName("text-decoration");
                this.tabLabel.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
            }
            if (this.underlinedButton.isSelected()) {
                this.tabLabel.getCssRules().addDeclaration(new CSSDeclaration("text-decoration", CSSExpression.createSimple("underline")));
            }
            String cssRules = aWriter.getCSSAsString(this.tabLabel.getCssRules());
            this.tabLabel.getElement().attr("style", cssRules);
            this.tabLabel.getDomElement().setAttribute("style", cssRules);
        });
    }

    private void initIcon() {
        icon.setButtonCell(new IconComboboxCell());
        icon.setCellFactory(param -> new IconComboboxCell());
        icon.getItems().addAll(IonIcon.ionIcons);
        icon.setPromptText("Select an icon");

        icon.setOnAction(event -> {
            Element ionIcon = tab.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
            System.out.println(ionIcon);
            if (ionIcon != null) {
                ionIcon.remove();
                tab.getDomElement().removeChild(tab.getDomElement().getElementsByTagName(SupportedComponents.ION_ICON.toString().toUpperCase()).item(0));
            }
            if (!((IonIcon) icon.getValue()).getName().equals("None")) {
                ionIcon = new Element(SupportedComponents.ION_ICON.toString());
                ionIcon.attr("name", ((IonIcon) icon.getValue()).getName());

                org.w3c.dom.Element ionIconElement = tab.getDomElement().getOwnerDocument().createElement(SupportedComponents.ION_ICON.toString().toUpperCase());
                ionIconElement.setAttribute("name", ((IonIcon) icon.getValue()).getName());

                tab.getElement().appendChild(ionIcon);
                tab.getDomElement().appendChild(ionIconElement);
            }
        });
    }

    private void initBackgroundColorView() {
        this.backgroundColorPicker = new CustomColorPicker();
        this.backgroundColorPicker.setTooltip(new Tooltip("Select button background color."));
        this.backgroundColorPane.setGraphic(this.backgroundColorPicker);

        this.backgroundColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = tab.getCssRules().getDeclarationOfPropertyName("--background");
                tab.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            if (!newValue.equals(Color.TRANSPARENT)) {
                tab.getCssRules().addDeclaration(new CSSDeclaration("--background", CSSExpression.createSimple(FxUtils.toRGBCode(newValue))));
            }
            String cssRules = aWriter.getCSSAsString(tab.getCssRules());
            tab.getElement().attr("style", cssRules);
            tab.getDomElement().setAttribute("style", cssRules);
        });
    }

    private void initColorView() {
        this.colorPicker = new CustomColorPicker();
        this.colorPicker.setTooltip(new Tooltip("Select button background color."));
        this.colorPane.setGraphic(this.colorPicker);

        this.colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = tab.getCssRules().getDeclarationOfPropertyName("--color");
                tab.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            if (!newValue.equals(Color.TRANSPARENT)) {
                tab.getCssRules().addDeclaration(new CSSDeclaration("--color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue))));
            }
            String cssRules = aWriter.getCSSAsString(tab.getCssRules());
            tab.getElement().attr("style", cssRules);
            tab.getDomElement().setAttribute("style", cssRules);
        });
    }

    private void initSelectedColorView() {
        this.selectedColorPicker = new CustomColorPicker();
        this.selectedColorPicker.setTooltip(new Tooltip("Select button background color."));
        this.selectedColorPane.setGraphic(this.selectedColorPicker);

        this.selectedColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = tab.getCssRules().getDeclarationOfPropertyName("--color-selected");
                tab.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            if (!newValue.equals(Color.TRANSPARENT)) {
                tab.getCssRules().addDeclaration(new CSSDeclaration("--color-selected", CSSExpression.createSimple(FxUtils.toRGBCode(newValue))));
            }
            String cssRules = aWriter.getCSSAsString(tab.getCssRules());
            tab.getElement().attr("style", cssRules);
            tab.getDomElement().setAttribute("style", cssRules);
        });
    }

    private void initRippleColorView() {
        this.rippleColorPicker = new CustomColorPicker();
        this.rippleColorPicker.setTooltip(new Tooltip("Select button background color."));
        this.rippleColorPane.setGraphic(this.rippleColorPicker);

        this.rippleColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = tab.getCssRules().getDeclarationOfPropertyName("--ripple-color");
                tab.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
                System.out.println(e);
            }
            if (!newValue.equals(Color.TRANSPARENT)) {
                tab.getCssRules().addDeclaration(new CSSDeclaration("--ripple-color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue))));
            }
            String cssRules = aWriter.getCSSAsString(tab.getCssRules());
            tab.getElement().attr("style", cssRules);
            tab.getDomElement().setAttribute("style", cssRules);
        });
    }

    //Setup
    public void setTabElement(GeneratedElement tab) {
        this.tab = tab;
        this.tabLabel = new GeneratedElement(tab.getElement().selectFirst("ion-label"), (org.w3c.dom.Element) tab.getDomElement().getElementsByTagName("ion-label").item(0));

        this.tabBarElement = new GeneratedElement(tab.getElement().parent(), (org.w3c.dom.Element) tab.getDomElement().getParentNode());

        String tabName = tab.getElement().attr("tab");
        NodeList nodeList = tab.getDomElement().getOwnerDocument().getElementsByTagName("ion-tab");
        org.w3c.dom.Element element = null;
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i) instanceof org.w3c.dom.Element && (element = (org.w3c.dom.Element) nodeList.item(i)).getAttribute("tab").equals(tabName)) {
                break;
            }
        }
        if (element != null) {
            this.tabElement = new GeneratedElement(PreviewController.ionicDocument.selectFirst("ion-tab[tab=" + tabName + "]"), element);
        }

        //Setting the botton text
        String buttonText = this.tab.getElement().text();
        this.textValue.setText(buttonText);

        getButtonFontSize();
        getButtonFontColor();
        getButtonIsBold();
        getButtonIsItalic();
        getButtonIsUnderlined();

        //Setting the button look
        getTabBackgroundColor();
        getTabColor();
        getTabRippleColor();
        getTabSelectedColor();

        //Setting the button icon
        getTabIcon();

        getTabPosition();
    }

    private void getTabPosition() {
        this.barPosition.getSelectionModel().select(StringUtils.capitalize(this.tabBarElement.getElement().attr("slot")));
    }


    private void getButtonFontSize() {
        try {
            String fontSizeString = CssRuleExtractor.extractValue(tabLabel.getCssRules(), "font-size");
            this.textSize.setValue(Integer.parseInt(fontSizeString.replaceAll("\\D+", "")));
        } catch (NullPointerException e) {
        }
    }

    private void getButtonFontColor() {
        try {
            String fontColorString = CssRuleExtractor.extractValue(tabLabel.getCssRules(), "color");
            this.textColorPicker.setValue(Color.valueOf(fontColorString));
        } catch (NullPointerException e) {
            this.textColorPicker.setValue(Color.TRANSPARENT);
        }
    }

    private void getButtonIsBold() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(tabLabel.getCssRules(), "font-weight");
            if (fontWeightString.equals("bold")) {
                this.boldButton.setSelected(true);
                return;
            }
        } catch (NullPointerException e) {
        }
        this.boldButton.setSelected(false);
    }

    private void getButtonIsItalic() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(tabLabel.getCssRules(), "font-style");
            if (fontWeightString.equals("italic")) {
                this.italicButton.setSelected(true);
                return;
            }
        } catch (NullPointerException e) {
        }
        this.italicButton.setSelected(false);
    }

    private void getButtonIsUnderlined() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(tabLabel.getCssRules(), "text-decoration");
            if (fontWeightString.equals("underline")) {
                this.underlinedButton.setSelected(true);
                return;
            }
        } catch (NullPointerException e) {
        }
        this.underlinedButton.setSelected(false);
    }

    //css background-color rule
    private void getTabBackgroundColor() {
        try {
            String backgroundString = CssRuleExtractor.extractValue(tab.getCssRules(), "--background");
            this.backgroundColorPicker.setValue(Color.valueOf(backgroundString));
        } catch (NullPointerException e) {
            this.backgroundColorPicker.setValue(Color.TRANSPARENT);
        }
    }

    private void getTabColor() {
        try {
            String backgroundString = CssRuleExtractor.extractValue(tab.getCssRules(), "--color");
            this.colorPicker.setValue(Color.valueOf(backgroundString));
        } catch (NullPointerException e) {
            this.colorPicker.setValue(Color.TRANSPARENT);
        }
    }

    private void getTabSelectedColor() {
        try {
            String backgroundString = CssRuleExtractor.extractValue(tab.getCssRules(), "--color-selected");
            this.selectedColorPicker.setValue(Color.valueOf(backgroundString));
        } catch (NullPointerException e) {
            this.selectedColorPicker.setValue(Color.TRANSPARENT);
        }
    }

    private void getTabRippleColor() {
        try {
            String backgroundString = CssRuleExtractor.extractValue(tab.getCssRules(), "--ripple-color");
            this.rippleColorPicker.setValue(Color.valueOf(backgroundString));
        } catch (NullPointerException e) {
            this.rippleColorPicker.setValue(Color.TRANSPARENT);
        }
    }

    private void getTabIcon() {
        Element iconJsoupElement = this.tab.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
        if (iconJsoupElement != null) {
            String iconName = iconJsoupElement.attr("name");
            this.icon.getSelectionModel().select(new IonIcon(iconName));
        }
    }

    @Override
    public void save() {
        PreviewController.saveDocument();
    }
}
