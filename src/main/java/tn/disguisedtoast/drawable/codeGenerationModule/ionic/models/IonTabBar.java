package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ion-tab-bar")
public class IonTabBar {

    private String slot = "bottom";
    private List<IonTabButton> tabButtons = new ArrayList<>();

    @XmlAttribute(name = "slot")
    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    @XmlElement(name = "ion-tab-button")
    public List<IonTabButton> getTabButtons() {
        return tabButtons;
    }

    public void setTabButtons(List<IonTabButton> tabButtons) {
        this.tabButtons = tabButtons;
    }
}
