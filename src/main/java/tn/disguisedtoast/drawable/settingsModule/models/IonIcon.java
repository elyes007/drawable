package tn.disguisedtoast.drawable.settingsModule.models;

import javafx.scene.image.Image;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IonIcon {
    private Image image;
    private String name;

    public static List<IonIcon> ionIcons = new ArrayList<>(Arrays.asList(
            new IonIcon("None"),
            new IonIcon("add"),
            new IonIcon("add-circle"),
            new IonIcon("close"),
            new IonIcon("close-circle")
    ));

    public IonIcon(String name) {
        this.name = name;
        try {
            this.image = new Image(getClass().getResource("/drawable/ion_icons/"+name+".png").toURI().toString());
        } catch (Exception e) { }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IonIcon ionIcon = (IonIcon) o;
        return Objects.equals(name, ionIcon.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
