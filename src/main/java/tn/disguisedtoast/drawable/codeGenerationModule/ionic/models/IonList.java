package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ion-list")
public class IonList {

    private List<IonItem> items = new ArrayList<>();

    @XmlElement(name = "ion-item")
    public List<IonItem> getItems() {
        return items;
    }

    public void setItems(List<IonItem> items) {
        this.items = items;
    }
}
