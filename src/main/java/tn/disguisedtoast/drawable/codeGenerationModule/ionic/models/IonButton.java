package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "ion-button")
public class IonButton extends IonView {

    private String text;

    public IonButton() {
    }

    public IonButton(String text) {
        this.text = text;
    }

    @XmlValue
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
