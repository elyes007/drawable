package tn.disguisedtoast.drawable.settingsModule.controllers.menuSettings;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import org.eclipse.jgit.util.StringUtils;
import org.jsoup.nodes.Element;
import tn.disguisedtoast.drawable.ProjectMain.Drawable;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.controllers.SettingsViewController;
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
import java.util.Arrays;
import java.util.ResourceBundle;

public class MenuButtonSettingsController implements Initializable, SettingsControllerInterface {
    private final CSSWriter aWriter = new CSSWriter(new CSSWriterSettings(ECSSVersion.CSS30, false));
    @FXML
    public BorderPane actionSettingsPane;
    @FXML
    public TextField textValue;
    @FXML
    public ComboBox textSize;
    @FXML
    public Label backgroundColorPane;
    @FXML
    public ChoiceBox buttonAction;
    @FXML
    public FlowPane noActionPane;
    @FXML
    public ComboBox fillType;
    @FXML
    public ComboBox fillTheme;
    @FXML
    public ToggleButton boldButton;
    @FXML
    public ToggleButton italicButton;
    @FXML
    public ToggleButton underlinedButton;
    @FXML
    public Label fontColorPane;
    @FXML
    public ToggleButton leftSlot;
    @FXML
    public ToggleButton rightSlot;
    @FXML
    public ToggleButton defaultSlot;
    @FXML
    public ComboBox icon;
    @FXML
    public Label deleteButton;
    @FXML
    public BorderPane destinationPane;
    @FXML
    public ComboBox pagesList;

    public CustomColorPicker textColor;
    private Integer[] textSizes = {10, 12, 14, 18, 24, 36, 48, 64, 72, 96};
    private String[] themes = {"Default", "Primary", "Secondary", "Tertiary", "Success", "Warning", "Danger", "Light", "Medium", "Dark"};
    private String[] fills = {"Solid", "None"};
    private String[] actions = {"Select an action", "Navigation"};

    private GeneratedElement button;
    private GeneratedElement buttonLabel;
    private JsonObject buttonNavObject;
    private JsonObject jsonObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset(StandardCharsets.UTF_8.name());

        actionSettingsPane.getChildren().remove(destinationPane);

        deleteButton.setOnMouseClicked(event -> {
            this.button.getElement().remove();
            this.button.getDomElement().getParentNode().removeChild(this.button.getDomElement());
            SettingsViewController.getInstance().clearSettingView();
        });

        buttonAction.getItems().addAll(actions);
        buttonAction.getSelectionModel().select(0);
        this.buttonAction.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            int index = (Integer) newValue;
            switch (index) {
                case 0:
                    this.button.getElement().removeAttr("[routerlink]");
                    if (jsonObject.getAsJsonObject("actions").has(this.button.getElement().id())) {
                        jsonObject.getAsJsonObject("actions").remove(this.button.getElement().id());
                    }
                    actionSettingsPane.setCenter(noActionPane);
                    break;
                case 1:
                    actionSettingsPane.setCenter(destinationPane);
                    break;
            }
        });

        String parentDirPath = (Drawable.projectPath + "&RelatedFiles&pages").replace("&", File.separator);
        File file = new File(parentDirPath);
        String[] directories = file.list((dir, name) -> (new File(dir, name).isDirectory() && !name.equals(Paths.get(SettingsViewController.pageFolder).getFileName().toString())) && !name.equals("temp"));

        /*
        List<String> pages = Arrays.stream(Objects.requireNonNull(file.listFiles((dir, name) -> (new File(dir, name).isDirectory() && !name.equals(Paths.get(SettingsViewController.pageFolder).getFileName().toString())) && !name.equals("temp")))).map(file1 -> {
            File[] files = file1.listFiles();
            if (files == null) return "";
            try {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].getName().equals("conf.json")) {
                        JsonObject pageConf = new JsonParser().parse(new FileReader(files[i])).getAsJsonObject();
                        return pageConf.get("page").getAsString();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return "";
        }).collect(Collectors.toList());*/


        this.pagesList.setPromptText("Select a destination page");
        this.pagesList.getItems().addAll(directories);

        this.pagesList.setOnAction(event -> {
            String pageName = (String) this.pagesList.getValue();
            if (this.buttonNavObject != null) {
                this.buttonNavObject.addProperty("dest", pageName);
            } else {
                this.buttonNavObject = new JsonObject();
                this.buttonNavObject.addProperty("dest", pageName);

                this.jsonObject.getAsJsonObject("actions").add(this.button.getElement().id(), buttonNavObject);
            }
            this.button.getElement().attr("[routerlink]", this.pagesList.getValue().toString());
        });


        initTextView();
        initThemeView();
        initIcon();
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
            this.buttonLabel.getElement().text(this.textValue.getText());
            this.buttonLabel.getDomElement().setTextContent(this.textValue.getText());
        });

        this.textSize.setOnAction(event -> {
            try {
                try {
                    CSSDeclaration declaration = this.buttonLabel.getCssRules().getDeclarationOfPropertyName("font-size");
                    this.buttonLabel.getCssRules().removeDeclaration(declaration);
                } catch (NullPointerException e) {
                    System.out.println(e);
                }
                if (!textSize.getValue().equals("Default") && textSize.getValue().toString().matches("\\d+(\\.\\d+)?")) {
                    this.buttonLabel.getCssRules().addDeclaration(new CSSDeclaration("font-size", CSSExpression.createSimple(textSize.getValue() + "px")));
                } else {
                    textSize.getSelectionModel().selectFirst();
                }
                String cssRules = aWriter.getCSSAsString(this.buttonLabel.getCssRules());
                this.buttonLabel.getElement().attr("style", cssRules);
                this.buttonLabel.getDomElement().setAttribute("style", cssRules);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        });

        this.textColor.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = this.buttonLabel.getCssRules().getDeclarationOfPropertyName("color");
                this.buttonLabel.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
            }
            if (!newValue.equals(Color.TRANSPARENT)) {
                this.buttonLabel.getCssRules().addDeclaration(new CSSDeclaration("color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue) + " !important")));
            }
            String cssRules = aWriter.getCSSAsString(this.buttonLabel.getCssRules());
            this.buttonLabel.getElement().attr("style", cssRules);
            this.buttonLabel.getDomElement().setAttribute("style", cssRules);
        });

        this.boldButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = this.buttonLabel.getCssRules().getDeclarationOfPropertyName("font-weight");
                this.buttonLabel.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
            }
            if (this.boldButton.isSelected()) {
                this.buttonLabel.getCssRules().addDeclaration(new CSSDeclaration("font-weight", CSSExpression.createSimple("bold")));
            }
            String cssRules = aWriter.getCSSAsString(this.buttonLabel.getCssRules());
            this.buttonLabel.getElement().attr("style", cssRules);
            this.buttonLabel.getDomElement().setAttribute("style", cssRules);
        });

        this.italicButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = this.buttonLabel.getCssRules().getDeclarationOfPropertyName("font-style");
                this.buttonLabel.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
            }
            if (this.italicButton.isSelected()) {
                this.buttonLabel.getCssRules().addDeclaration(new CSSDeclaration("font-style", CSSExpression.createSimple("italic")));
            }
            String cssRules = aWriter.getCSSAsString(this.buttonLabel.getCssRules());
            this.buttonLabel.getElement().attr("style", cssRules);
            this.buttonLabel.getDomElement().setAttribute("style", cssRules);
        });

        this.underlinedButton.setOnAction(event -> {
            try {
                CSSDeclaration declaration = this.buttonLabel.getCssRules().getDeclarationOfPropertyName("text-decoration");
                this.buttonLabel.getCssRules().removeDeclaration(declaration);
            } catch (NullPointerException e) {
            }
            if (this.underlinedButton.isSelected()) {
                this.buttonLabel.getCssRules().addDeclaration(new CSSDeclaration("text-decoration", CSSExpression.createSimple("underline")));
            }
            String cssRules = aWriter.getCSSAsString(this.buttonLabel.getCssRules());
            this.buttonLabel.getElement().attr("style", cssRules);
            this.buttonLabel.getDomElement().setAttribute("style", cssRules);
        });
    }

    private void initThemeView() {
        this.fillTheme.getItems().addAll(Arrays.asList(themes));
        this.fillType.getItems().addAll(Arrays.asList(fills));

        this.fillType.setOnAction(event -> {
            if (this.fillType.getValue() == "None") {
                this.fillTheme.getSelectionModel().select("Default");
                this.fillTheme.setDisable(true);
            } else {
                this.fillTheme.setDisable(false);
            }
            String fill = this.fillType.getValue().toString().toLowerCase();
            this.button.getElement().attr("fill", fill);
            this.button.getDomElement().setAttribute("fill", fill);
        });

        this.fillTheme.setOnAction(event -> {
            if (this.fillTheme.getValue() != "Default") {
                String fillTheme = this.fillTheme.getValue().toString().toLowerCase();
                this.button.getElement().attr("color", fillTheme);
                this.button.getDomElement().setAttribute("color", fillTheme);
            } else {
                this.button.getElement().removeAttr("color");
                this.button.getDomElement().removeAttribute("color");
            }
        });
    }

    private void initIcon() {
        icon.setButtonCell(new IconComboboxCell());
        icon.setCellFactory(param -> new IconComboboxCell());
        icon.getItems().addAll(IonIcon.ionIcons);
        icon.setPromptText("Select an icon");

        icon.setOnAction(event -> {
            Element ionIcon = button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
            System.out.println(ionIcon);
            if (ionIcon != null) {
                ionIcon.remove();
                button.getDomElement().removeChild(button.getDomElement().getElementsByTagName(SupportedComponents.ION_ICON.toString().toUpperCase()).item(0));
            }
            if (!((IonIcon) icon.getValue()).getName().equals("None")) {
                this.leftSlot.setDisable(false);
                this.rightSlot.setDisable(false);
                this.defaultSlot.setDisable(false);

                ionIcon = new Element(SupportedComponents.ION_ICON.toString());
                ionIcon.attr("name", ((IonIcon) icon.getValue()).getName());

                org.w3c.dom.Element ionIconElement = button.getDomElement().getOwnerDocument().createElement(SupportedComponents.ION_ICON.toString().toUpperCase());
                ionIconElement.setAttribute("name", ((IonIcon) icon.getValue()).getName());

                button.getElement().appendChild(ionIcon);
                button.getDomElement().appendChild(ionIconElement);

                this.defaultSlot.setSelected(true);
            } else {
                this.leftSlot.setSelected(false);
                this.rightSlot.setSelected(false);
                this.defaultSlot.setSelected(false);

                this.leftSlot.setDisable(true);
                this.rightSlot.setDisable(true);
                this.defaultSlot.setDisable(true);
            }
        });

        this.leftSlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.rightSlot.setSelected(false);
                this.defaultSlot.setSelected(false);

                Element ionElement = this.button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
                if (ionElement != null) {
                    ionElement.attr("slot", "start");
                    ((org.w3c.dom.Element) this.button.getDomElement().getElementsByTagName(SupportedComponents.ION_ICON.toString().toUpperCase()).item(0)).setAttribute("slot", "start");
                }
            } else if (!this.rightSlot.isSelected() && !this.defaultSlot.isSelected()) {
                this.leftSlot.setSelected(true);
            }
        });

        this.rightSlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.leftSlot.setSelected(false);
                this.defaultSlot.setSelected(false);

                Element ionElement = this.button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
                if (ionElement != null) {
                    ionElement.attr("slot", "end");
                    ((org.w3c.dom.Element) this.button.getDomElement().getElementsByTagName(SupportedComponents.ION_ICON.toString().toUpperCase()).item(0)).setAttribute("slot", "end");
                }
            } else if (!this.leftSlot.isSelected() && !this.defaultSlot.isSelected()) {
                this.rightSlot.setSelected(true);
            }
        });

        this.defaultSlot.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.leftSlot.setSelected(false);
                this.rightSlot.setSelected(false);

                Element ionElement = this.button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
                if (ionElement != null) {
                    ionElement.removeAttr("slot");
                    ((org.w3c.dom.Element) this.button.getDomElement().getElementsByTagName(SupportedComponents.ION_ICON.toString().toUpperCase()).item(0)).removeAttribute("slot");
                }
            } else if (!this.leftSlot.isSelected() && !this.rightSlot.isSelected() && !this.defaultSlot.isSelected()) {
                this.defaultSlot.setSelected(true);
            }
        });
    }

    //Setup
    public void setMenuButton(GeneratedElement button) {
        this.button = button;
        this.buttonLabel = new GeneratedElement(button.getElement().selectFirst("ion-label"), (org.w3c.dom.Element) button.getDomElement().getElementsByTagName("ion-label").item(0));
        getNavJsonObject();
        //Setting the botton text
        String buttonText = this.buttonLabel.getElement().text();
        this.textValue.setText(buttonText);

        getButtonFontSize();
        getButtonFontColor();
        getButtonIsBold();
        getButtonIsItalic();
        getButtonIsUnderlined();

        //Setting the button look
        getButtonThemeFill();
        getButtonThemeColor();

        //Setting the button icon
        getButtonIcon();

        if (this.button.getElement().hasAttr("[routerlink]")) {
            this.buttonAction.getSelectionModel().select(1);
            if (this.buttonNavObject != null) {
                this.pagesList.setValue(this.buttonNavObject.get("dest").getAsString());
            }
        }
    }

    private void getNavJsonObject() {
        try {
            jsonObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder + "/conf.json")).getAsJsonObject();

            JsonObject actions = jsonObject.getAsJsonObject("actions");

            if (actions.has(this.button.getElement().id())) {
                JsonObject buttonAction = actions.getAsJsonObject(this.button.getElement().id());
                if (buttonAction.has("dest")) {
                    buttonNavObject = buttonAction;
                } else {
                    actions.remove(this.button.getElement().id());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getButtonFontSize() {
        try {
            String fontSizeString = CssRuleExtractor.extractValue(buttonLabel.getCssRules(), "font-size");
            this.textSize.setValue(Integer.parseInt(fontSizeString.replaceAll("\\D+", "")));
        } catch (NullPointerException e) {
        }
    }

    private void getButtonFontColor() {
        try {
            String fontColorString = CssRuleExtractor.extractValue(buttonLabel.getCssRules(), "color");
            this.textColor.setValue(Color.valueOf(fontColorString));
        } catch (NullPointerException e) {
            this.textColor.setValue(Color.TRANSPARENT);
        }
    }

    private void getButtonIsBold() {
        try {
            String fontWeightString = CssRuleExtractor.extractValue(buttonLabel.getCssRules(), "font-weight");
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
            String fontWeightString = CssRuleExtractor.extractValue(buttonLabel.getCssRules(), "font-style");
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
            String fontWeightString = CssRuleExtractor.extractValue(buttonLabel.getCssRules(), "text-decoration");
            if (fontWeightString.equals("underline")) {
                this.underlinedButton.setSelected(true);
                return;
            }
        } catch (NullPointerException e) {
        }
        this.underlinedButton.setSelected(false);
    }

    //Fill type: outline, fill, clear, ...
    private void getButtonThemeFill() {
        if (button.getElement().hasAttr("fill")) {
            this.fillType.getSelectionModel().select(StringUtils.capitalize(button.getElement().attr("fill")));
        } else {
            this.fillType.getSelectionModel().select("Solid");
        }
    }

    //Color: primary, secondary, danger, ...
    private void getButtonThemeColor() {
        if (button.getElement().hasAttr("color")) {
            this.fillTheme.getSelectionModel().select(StringUtils.capitalize(button.getElement().attr("color")));
        } else {
            this.fillTheme.getSelectionModel().select("Default");
        }
    }

    private void getButtonIcon() {
        Element iconJsoupElement = this.button.getElement().selectFirst(SupportedComponents.ION_ICON.toString());
        if (iconJsoupElement != null) {
            String iconName = iconJsoupElement.attr("name");
            this.icon.getSelectionModel().select(new IonIcon(iconName));

            if (!iconJsoupElement.hasAttr("slot")) {
                this.defaultSlot.setSelected(true);
                this.leftSlot.setSelected(false);
                this.rightSlot.setSelected(false);
            } else if (iconJsoupElement.attr("slot").equals("start")) {
                this.leftSlot.setSelected(true);
                this.rightSlot.setSelected(false);
                this.defaultSlot.setSelected(false);
            } else if (iconJsoupElement.attr("slot").equals("end")) {
                this.rightSlot.setSelected(true);
                this.leftSlot.setSelected(false);
                this.defaultSlot.setSelected(false);
            } else {
                this.defaultSlot.setSelected(true);
                this.rightSlot.setSelected(false);
                this.leftSlot.setSelected(false);
            }
        } else {
            this.leftSlot.setDisable(true);
            this.rightSlot.setDisable(true);
            this.defaultSlot.setDisable(true);
        }
    }

    @Override
    public void save() {
        if (this.pagesList.getValue() == null) {
            this.button.getElement().removeAttr("[routerlink]");

            if (this.jsonObject.getAsJsonObject("actions").has(this.button.getElement().id())) {
                this.jsonObject.getAsJsonObject("actions").remove(this.button.getElement().id());
            }
            return;
        }

        try {
            Files.write(Paths.get(SettingsViewController.pageFolder + "/conf.json"), new GsonBuilder().create().toJson(jsonObject).getBytes());
            //this.button.getElement().attr("[routerlink]", this.pagesList.getValue().toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PreviewController.saveDocument();
    }
}
