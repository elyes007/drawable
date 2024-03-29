package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "ion-icon")
public class IonIcon {

    public static List<String> names = new ArrayList<>(Arrays.asList("calendar", "map", "contacts"));

    private String name;
    private String slot;

    public IonIcon() {
    }

    public IonIcon(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "slot")
    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    @XmlValue
    public String getContent() {
        return "";
    }
}
