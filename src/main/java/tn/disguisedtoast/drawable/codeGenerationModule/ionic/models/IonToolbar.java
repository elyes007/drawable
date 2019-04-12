package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ion-toolbar")
@XmlType(propOrder = {"ionButtons", "title"})
public class IonToolbar {

    private String title = "Page";
    private String color = "primary";
    private String id = "toolbar";
    private String classe = "clickable";
    private IonButtons ionButtons = new IonButtons();

    public IonToolbar() {
    }

    public IonToolbar(String title) {
        this.title = title;
    }

    @XmlElement(name = "ion-title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @XmlAttribute(name = "color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlAttribute(name = "class")
    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @XmlElement(name = "ion-buttons")
    public IonButtons getIonButtons() {
        return ionButtons;
    }

    public void setIonButtons(IonButtons ionButtons) {
        this.ionButtons = ionButtons;
    }
}
