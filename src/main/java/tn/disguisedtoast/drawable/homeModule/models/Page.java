package tn.disguisedtoast.drawable.homeModule.models;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Page {
    private String name;
    private String folderName;
    private Image image;
    private String html;
    private String tabParent; //to use for tabs

    public Page(String name, String folderName) {
        this.name = name;
        this.folderName = folderName;
        try {
            this.image = new Image(new FileInputStream(folderName + "/snapshot.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Page() {
    }

    public String getTabParent() {
        return tabParent;
    }

    public void setTabParent(String tabParent) {
        this.tabParent = tabParent;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Page{" +
                "name='" + name + '\'' +
                ", folderName='" + folderName + '\'' +
                '}';
    }
}
