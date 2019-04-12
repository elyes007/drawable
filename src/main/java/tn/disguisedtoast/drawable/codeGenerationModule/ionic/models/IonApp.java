package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ion-app")
@XmlType(propOrder = {"ionMenu", "div", "header", "content", "tabs"})
public class IonApp extends IonContainer {

    private IonMenu ionMenu;
    private Div div;

    public IonApp() {
    }

    public IonApp(IonContent ionContent) {
        super();
        this.content = ionContent;
    }

    public IonApp(IonTabs ionTabs) {
        super();
        this.tabs = ionTabs;
    }

    @XmlElement(name = "ion-menu")
    public IonMenu getIonMenu() {
        return ionMenu;
    }

    public void setIonMenu(IonMenu ionMenu) {
        this.ionMenu = ionMenu;
    }

    @XmlElement(name = "div")
    public Div getDiv() {
        return div;
    }

    public void setDiv(Div div) {
        this.div = div;
    }
}
