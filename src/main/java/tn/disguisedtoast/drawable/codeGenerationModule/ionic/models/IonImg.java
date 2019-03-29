package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "ion-img")
public class IonImg extends IonView {

    private String src;

    @XmlAttribute(name = "src")
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @XmlAttribute(name = "style")
    public String getStyle() {
        return "object-fit: cover; " + super.getStyle();
    }

    @XmlValue
    public String getContent() {
        return "";
    }
}
