package tn.disguisedtoast.drawable.previewModule.models;

import com.sun.javafx.scene.layout.region.Margins;

public class Skin {
    private String imageName;
    private Margins margins;

    public Skin(double top, double right, double bottom, double left, String imageName) {
        this.margins = new Margins(top, right, bottom, left, true);
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Margins getMargins() {
        return margins;
    }

    public void setMargins(Margins margins) {
        this.margins = margins;
    }
}
