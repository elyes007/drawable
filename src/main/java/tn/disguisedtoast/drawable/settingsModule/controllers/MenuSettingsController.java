package tn.disguisedtoast.drawable.settingsModule.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.jsoup.nodes.Element;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuSettingsController implements Initializable, SettingsControllerInterface {

    @FXML
    private Button addButtonButton;

    private GeneratedElement ionContentGElement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addButtonButton.setOnAction(event -> {
            double id = Math.random();
            Element menuButtonJSoupElement = createMenuButtonJSoup(id);
            org.w3c.dom.Element menuButtonDom = createMenuButtonDom(id);

            this.ionContentGElement.getElement().selectFirst("ion-list").appendChild(menuButtonJSoupElement);
            this.ionContentGElement.getDomElement().getElementsByTagName("ion-list").item(0).appendChild(menuButtonDom);
        });
    }

    private org.w3c.dom.Element createMenuButtonDom(double id) {
        org.w3c.dom.Element menuButtonDom = this.ionContentGElement.getDomElement().getOwnerDocument().createElement(SupportedComponents.ION_ITEM.toString().toUpperCase());
        menuButtonDom.setAttribute("style", "");
        menuButtonDom.setAttribute("class", "clickable menu_item");
        menuButtonDom.setAttribute("id", "menu_item" + id);

        org.w3c.dom.Element menuButtonTitleDom = this.ionContentGElement.getDomElement().getOwnerDocument().createElement(SupportedComponents.ION_LABEL.toString().toUpperCase());
        menuButtonTitleDom.setAttribute("style", "");
        menuButtonDom.appendChild(this.ionContentGElement.getDomElement().getOwnerDocument().createTextNode("Button"));

        return menuButtonDom;
    }

    private Element createMenuButtonJSoup(double id) {
        Element menuButtonJSoupElement = new Element(SupportedComponents.ION_ITEM.toString());
        menuButtonJSoupElement.attr("class", "clickable menu_item");
        menuButtonJSoupElement.attr("id", "menu_item" + id);
        menuButtonJSoupElement.attr("style", "");

        Element menuButtonTitle = new Element(SupportedComponents.ION_LABEL.toString());
        menuButtonTitle.attr("style", "");
        menuButtonTitle.text("Button");

        menuButtonJSoupElement.appendChild(menuButtonTitle);
        return menuButtonJSoupElement;
    }

    public void setIonContentElement(GeneratedElement ionContentElement) {
        this.ionContentGElement = ionContentElement;
    }

    @Override
    public void save() {

    }
}
