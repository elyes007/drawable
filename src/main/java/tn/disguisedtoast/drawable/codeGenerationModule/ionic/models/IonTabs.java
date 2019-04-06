package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "ion-tabs")
@XmlType(propOrder = {"tabs", "tabBar"})
public class IonTabs {

    private String style = "position: relative;";
    private List<IonTab> tabs = new ArrayList<>();
    private IonTabBar tabBar;

    @XmlAttribute(name = "style")
    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @XmlElement(name = "ion-tab")
    public List<IonTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<IonTab> tabs) {
        this.tabs = tabs;
    }

    @XmlElement(name = "ion-tab-bar")
    public IonTabBar getTabBar() {
        return tabBar;
    }

    public void setTabBar(IonTabBar tabBar) {
        this.tabBar = tabBar;
    }
}
