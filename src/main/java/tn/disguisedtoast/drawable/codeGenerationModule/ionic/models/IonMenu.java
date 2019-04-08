package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ion-menu")
@XmlType(propOrder = {"header", "content", "tabs"})
public class IonMenu extends IonContainer {

    private String side = "start";
    private String menuId = "menu";
    private String style = "--ion-background-color: white;";

    @XmlAttribute(name = "side")
    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    @XmlAttribute(name = "menu-id")
    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    @XmlAttribute(name = "style")
    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
