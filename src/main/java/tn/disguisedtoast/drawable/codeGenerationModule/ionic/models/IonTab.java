package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ion-tab")
public class IonTab extends IonContent {

    private String id;
    private String tab;

    @XmlAttribute(name = "id")
    public String getId() {
        return tab;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(name = "tab")
    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

}
