package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "ion-menu-button")
public class IonMenuButton {

    private String menu = "menu";

    public IonMenuButton() {
    }

    public IonMenuButton(String menu) {
        this.menu = menu;
    }

    @XmlAttribute(name = "menu")
    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    @XmlValue
    public String getContent() {
        return "";
    }
}
