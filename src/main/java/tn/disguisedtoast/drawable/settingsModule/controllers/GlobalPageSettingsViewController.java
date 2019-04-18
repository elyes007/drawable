package tn.disguisedtoast.drawable.settingsModule.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpression;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
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
import java.util.ResourceBundle;

public class GlobalPageSettingsViewController implements Initializable, SettingsControllerInterface {
    @FXML public TextField pageNameField;
    @FXML public CheckBox hasToolbarButton;
    @FXML public Label backgroundColorLabelPane;
    private CustomColorPicker customColorPicker;

    private GeneratedElement bodyGeneratedElement;
    private final CSSWriter aWriter = new CSSWriter (new CSSWriterSettings(ECSSVersion.CSS30, false));
    private JsonObject jsonObject;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        aWriter.setContentCharset (StandardCharsets.UTF_8.name ());

        initBackgroundColor();
        initToolbar();
    }

    private void initBackgroundColor(){
        this.customColorPicker = new CustomColorPicker();
        this.customColorPicker.setValue(Color.TRANSPARENT);
        this.customColorPicker.setTooltip(new Tooltip("Select button background color."));
        this.backgroundColorLabelPane.setGraphic(customColorPicker);

        this.customColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                CSSDeclaration declaration = this.bodyGeneratedElement.getCssRules().getDeclarationOfPropertyName("background-color");
                this.bodyGeneratedElement.getCssRules().removeDeclaration(declaration);
            }catch (NullPointerException e) {
                System.out.println(e);
            }
            if(!newValue.equals(Color.TRANSPARENT)) {
                this.bodyGeneratedElement.getCssRules().addDeclaration(new CSSDeclaration("background-color", CSSExpression.createSimple(FxUtils.toRGBCode(newValue)+" !important")));
            }
            String cssRules = aWriter.getCSSAsString (this.bodyGeneratedElement.getCssRules());
            this.bodyGeneratedElement.getElement().attr("style", cssRules);
            this.bodyGeneratedElement.getDomElement().setAttribute("style", cssRules);
        });
    }

    private void initToolbar(){
        this.hasToolbarButton.setOnAction(event -> {
            if(this.hasToolbarButton.isSelected() && this.bodyGeneratedElement.getElement().selectFirst("#toolbar") == null) {
                addJsoupToolbar();
                addDomToolbar();
            }else{
                this.bodyGeneratedElement.getElement().selectFirst("#toolbar").remove();
                Node toolbarNode = this.bodyGeneratedElement.getDomElement().getElementsByTagName(SupportedComponents.ION_TOOLBAR.toString().toUpperCase()).item(0);
                toolbarNode.getParentNode().removeChild(toolbarNode);
            }
            PreviewController.updateClickableElements();
        });
    }

    private void addJsoupToolbar(){
        Element toolbar = new Element(SupportedComponents.ION_TOOLBAR.toString());
        toolbar.attr("color", "primary");
        toolbar.attr("id", "toolbar");
        toolbar.addClass("clickable");

        Element buttons = new Element(SupportedComponents.ION_BUTTONS.toString());
        buttons.attr("slot", "start");

        Element backButton = new Element(SupportedComponents.ION_BACK_BUTTON.toString());
        backButton.attr("default-href", "");
        buttons.appendChild(backButton);

        Element title = new Element(SupportedComponents.ION_TITLE.toString());
        title.text("Hello");

        toolbar.appendChild(buttons);
        toolbar.appendChild(title);
        this.bodyGeneratedElement.getElement().selectFirst("ion-header").appendChild(toolbar);
    }

    private void addDomToolbar(){
        Document document = this.bodyGeneratedElement.getDomElement().getOwnerDocument();
        Node ionHeader = this.bodyGeneratedElement.getDomElement().getElementsByTagName("ION-HEADER").item(0);

        org.w3c.dom.Element toolbar = document.createElement(SupportedComponents.ION_TOOLBAR.toString().toUpperCase());
        toolbar.setAttribute("color", "primary");
        toolbar.setAttribute("id", "toolbar");
        System.out.println(toolbar);
        toolbar.setAttribute("class", "clickable");

        org.w3c.dom.Element buttons = document.createElement(SupportedComponents.ION_BUTTONS.toString().toUpperCase());
        buttons.setAttribute("slot", "start");

        org.w3c.dom.Element backButton = document.createElement(SupportedComponents.ION_BACK_BUTTON.toString().toUpperCase());
        backButton.setAttribute("default-href", "");

        buttons.appendChild(backButton);

        org.w3c.dom.Element title = document.createElement(SupportedComponents.ION_TITLE.toString().toUpperCase());
        title.setTextContent("Hello");

        toolbar.appendChild(buttons);
        toolbar.appendChild(title);

        ionHeader.appendChild(toolbar);
    }

    public void setBodyGeneratedElement(GeneratedElement bodyGeneratedElement){
        this.bodyGeneratedElement = bodyGeneratedElement;

        checkHasToolbar();
        getBackgroundColor();
        getPageName();
    }

    private void getBackgroundColor(){
        try {
            String backgroundString = CssRuleExtractor.extractValue(this.bodyGeneratedElement.getCssRules(), "background-color");
            this.customColorPicker.setValue(Color.valueOf(backgroundString));
        }catch (NullPointerException e){
            this.customColorPicker.setValue(Color.TRANSPARENT);
        }
    }

    private void checkHasToolbar(){
        this.hasToolbarButton.setSelected(this.bodyGeneratedElement.getElement().selectFirst("#toolbar") != null);
    }



    private void getPageName() {
        try{
            jsonObject = new JsonParser().parse(new FileReader(SettingsViewController.pageFolder+"/conf.json")).getAsJsonObject();
            this.pageNameField.setText(jsonObject.get("page").getAsString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void save() {
        try{
            jsonObject.addProperty("page", this.pageNameField.getText());
            Files.write(Paths.get(SettingsViewController.pageFolder + File.separator + "conf.json"), new Gson().toJson(jsonObject).getBytes());
            PreviewController.saveDocument();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
