package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ion-header")
public class IonHeader {

    private IonToolbar toolbar;

    public IonHeader() {
        toolbar = new IonToolbar();
    }

    public IonHeader(IonToolbar toolbar) {
        this.toolbar = toolbar;
    }

    @XmlElement(name = "ion-toolbar")
    public IonToolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(IonToolbar toolbar) {
        this.toolbar = toolbar;
    }
}
