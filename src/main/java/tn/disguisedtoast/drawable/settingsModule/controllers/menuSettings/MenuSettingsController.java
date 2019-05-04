package tn.disguisedtoast.drawable.settingsModule.controllers.menuSettings;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.apache.commons.lang3.RandomStringUtils;
import org.jsoup.nodes.Element;
import tn.disguisedtoast.drawable.models.GeneratedElement;
import tn.disguisedtoast.drawable.models.SupportedComponents;
import tn.disguisedtoast.drawable.previewModule.controllers.PreviewController;
import tn.disguisedtoast.drawable.settingsModule.interfaces.SettingsControllerInterface;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuSettingsController implements Initializable, SettingsControllerInterface {

    @FXML
    private Button addButtonButton;

    private GeneratedElement ionContentGElement;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.addButtonButton.setOnAction(event -> {
            String id = RandomStringUtils.randomAlphanumeric(5);
            Element menuButtonJSoupElement = createMenuButtonJSoup(id);
            org.w3c.dom.Element menuButtonDom = createMenuButtonDom(id);

            this.ionContentGElement.getElement().selectFirst("ion-list").appendChild(menuButtonJSoupElement);
            this.ionContentGElement.getDomElement().getElementsByTagName("ion-list").item(0).appendChild(menuButtonDom);
            PreviewController.updateClickableElements();
            PreviewController.saveDocument();
        });
    }

    private org.w3c.dom.Element createMenuButtonDom(String id) {
        org.w3c.dom.Element menuToggle = this.ionContentGElement.getDomElement().getOwnerDocument().createElement("ion-menu-toggle");
        menuToggle.setAttribute("auto-hide", "false");

        org.w3c.dom.Element menuButtonDom = this.ionContentGElement.getDomElement().getOwnerDocument().createElement(SupportedComponents.ION_ITEM.toString().toUpperCase());
        menuButtonDom.setAttribute("style", "");
        menuButtonDom.setAttribute("class", "clickable menu_item");
        menuButtonDom.setAttribute("id", "menu_item_" + id);

        org.w3c.dom.Element menuButtonTitleDom = this.ionContentGElement.getDomElement().getOwnerDocument().createElement(SupportedComponents.ION_LABEL.toString().toUpperCase());
        menuButtonTitleDom.setAttribute("style", "");
        menuButtonTitleDom.appendChild(this.ionContentGElement.getDomElement().getOwnerDocument().createTextNode("Button"));

        menuButtonDom.appendChild(menuButtonTitleDom);

        menuToggle.appendChild(menuButtonDom);

        return menuToggle;
    }

    private Element createMenuButtonJSoup(String id) {
        Element menuToggle = new Element("ion-menu-toggle");
        menuToggle.attr("auto-hide", "false");

        Element menuButtonJSoupElement = new Element(SupportedComponents.ION_ITEM.toString());
        menuButtonJSoupElement.attr("class", "clickable menu_item");
        menuButtonJSoupElement.attr("id", "menu_item_" + id);
        menuButtonJSoupElement.attr("style", "");

        Element menuButtonTitle = new Element(SupportedComponents.ION_LABEL.toString());
        menuButtonTitle.attr("style", "");
        menuButtonTitle.text("Button");

        menuButtonJSoupElement.appendChild(menuButtonTitle);

        menuToggle.appendChild(menuButtonJSoupElement);
        return menuToggle;
    }

    public void setIonContentElement(GeneratedElement ionContentElement) {
        this.ionContentGElement = ionContentElement;
    }

    @Override
    public void save() {
        PreviewController.saveDocument();
    }
}
