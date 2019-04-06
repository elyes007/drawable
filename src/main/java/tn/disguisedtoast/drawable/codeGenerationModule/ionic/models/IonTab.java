package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "ion-tab")
public class IonTab {

    private String id;
    private String tab;
    private String component = "placeholder";
    private List<IonButton> buttons;
    private List<IonImg> images;
    private List<IonLabel> labels;
    private List<IonItem> items;

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

    @XmlAttribute(name = "component")
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
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
}
