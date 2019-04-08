package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ion-tab-button")
public class IonTabButton {

    private String tab;
    private IonIcon icon;
    private String label;
    private String badge;

    @XmlAttribute(name = "tab")
    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    @XmlElement(name = "ion-icon")
    public IonIcon getIcon() {
        return icon;
    }

    public void setIcon(IonIcon icon) {
        this.icon = icon;
    }

    @XmlElement(name = "ion-label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlElement(name = "ion-badge")
    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }
}
