package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ion-item")
@XmlType(propOrder = {"icon", "label", "input"})
public class IonItem extends IonView {

    private IonLabel label;
    private IonInput input;
    private IonIcon icon;

    public IonItem() {
    }

    public IonItem(IonLabel label, IonInput input) {
        this.label = label;
        this.input = input;
    }

    public IonItem(IonLabel label, IonIcon icon) {
        this.label = label;
        this.icon = icon;
    }

    @XmlElement(name = "ion-label")
    public IonLabel getLabel() {
        return label;
    }

    public void setLabel(IonLabel label) {
        this.label = label;
    }

    @XmlElement(name = "ion-input")
    public IonInput getInput() {
        return input;
    }

    public void setInput(IonInput input) {
        this.input = input;
    }

    @XmlElement(name = "ion-icon")
    public IonIcon getIcon() {
        return icon;
    }

    public void setIcon(IonIcon icon) {
        this.icon = icon;
    }
}
