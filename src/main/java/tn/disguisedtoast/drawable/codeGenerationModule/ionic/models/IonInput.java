package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ion-input")
public class IonInput extends IonView {

    public IonInput() {
        setPosition(null);
    }


    @XmlAttribute(name = "class")
    @Override
    public String getClasse() {
        return null;
    }
}
