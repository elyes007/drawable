package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ion-buttons")
public class IonButtons {

    private String slot = "start";
    private IonMenuButton menuButton;

    @XmlAttribute(name = "slot")
    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    @XmlElement(name = "ion-menu-button")
    public IonMenuButton getMenuButton() {
        return menuButton;
    }

    public void setMenuButton(IonMenuButton menuButton) {
        this.menuButton = menuButton;
    }
}
