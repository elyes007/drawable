package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "ion-content")
public class IonContent {

    private String id;
    private List<IonButton> buttons;
    private List<IonImg> images;
    private List<IonLabel> labels;
    private List<IonItem> items;
    private List<IonList> ionLists;
    private String classe;

    @XmlAttribute(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "ion-button")
    public List<IonButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<IonButton> buttons) {
        this.buttons = buttons;
    }

    @XmlElement(name = "ion-img")
    public List<IonImg> getImages() {
        return images;
    }

    public void setImages(List<IonImg> images) {
        this.images = images;
    }

    @XmlElement(name = "ion-label")
    public List<IonLabel> getLabels() {
        return labels;
    }

    public void setLabels(List<IonLabel> labels) {
        this.labels = labels;
    }

    @XmlElement(name = "ion-item")
    public List<IonItem> getItems() {
        return items;
    }

    public void setItems(List<IonItem> items) {
        this.items = items;
    }

    @XmlElement(name = "ion-list")
    public List<IonList> getIonLists() {
        return ionLists;
    }

    public void setIonLists(List<IonList> ionLists) {
        this.ionLists = ionLists;
    }

    @XmlAttribute(name = "class")
    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }
}
