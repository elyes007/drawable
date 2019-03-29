package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ion-item")
@XmlType(propOrder = {"label", "input"})
public class IonItem extends IonView {

    private IonLabel label;
    private IonInput input;

    public IonItem() {
        label = new IonLabel();
        input = new IonInput();
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
}
