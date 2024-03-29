package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "ion-label")
public class IonLabel extends IonView {

    private String label;
    private String positionAttr = "floating";
    private boolean ellipsis = false;

    public IonLabel() {
        setPosition(null);
    }

    public IonLabel(String label) {
        this.label = label;
        setPosition(null);
    }

    public IonLabel(String label, String positionAttr) {
        this.label = label;
        this.positionAttr = positionAttr;
        if (positionAttr != null) {
            setPosition(null);
        }
    }

    public IonLabel(String label, String positionAttr, String position) {
        this.label = label;
        this.positionAttr = positionAttr;
        setPosition(position);
    }

    @XmlTransient
    public boolean isEllipsis() {
        return ellipsis;
    }

    public void setEllipsis(boolean ellipsis) {
        this.ellipsis = ellipsis;
    }

    @XmlValue
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @XmlAttribute(name = "position")
    public String getPositionAttr() {
        return positionAttr;
    }

    public void setPositionAttr(String positionAttr) {
        this.positionAttr = positionAttr;
    }

    @XmlAttribute(name = "class")
    @Override
    public String getClasse() {
        return positionAttr != null ? null : "clickable";
    }

    @XmlAttribute(name = "style")
    @Override
    public String getStyle() {
        return super.getStyle() + (ellipsis ? "text-overflow: ellipsis; white-space: nowrap; overflow: hidden;" : "");
    }
}
