package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
@XmlTransient
public abstract class IonView {
    private String position = "absolute";
    private String top;
    private String left;
    private String width;
    private String height;
    private String style;

    @XmlAttribute(name = "style")
    public String getStyle() {
        return (position == null ? "" : getPosition())
                + (top == null ? "" : getTop())
                + (left == null ? "" : getLeft())
                + (width == null ? "" : getWidth())
                + (height == null ? "" : getHeight());
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @XmlTransient
    public String getTop() {
        return "top: " + top + ";";
    }

    public void setTop(String top) {
        this.top = top;
    }

    @XmlTransient
    public String getLeft() {
        return "left: " + left + ";";
    }

    public void setLeft(String left) {
        this.left = left;
    }

    @XmlTransient
    public String getWidth() {
        return "width: " + width + ";";
    }

    public void setWidth(String width) {
        this.width = width;
    }

    @XmlTransient
    public String getHeight() {
        return "height: " + height + ";";
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @XmlTransient
    public String getPosition() {
        return "position: " + position + ";";
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
