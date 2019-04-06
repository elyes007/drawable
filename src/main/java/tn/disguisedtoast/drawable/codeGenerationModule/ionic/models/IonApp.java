package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ion-app")
@XmlType(propOrder = {"header", "content", "tabs"})
public class IonApp {

    private IonHeader header;
    private IonContent content;
    private IonTabs tabs;

    public IonApp() {
        header = new IonHeader();
    }

    public IonApp(IonContent ionContent) {
        header = new IonHeader();
        this.content = ionContent;
    }

    @XmlElement(name = "ion-header")
    public IonHeader getHeader() {
        return header;
    }

    public void setHeader(IonHeader header) {
        this.header = header;
    }

    @XmlElement(name = "ion-content")
    public IonContent getContent() {
        return content;
    }

    public void setContent(IonContent content) {
        this.content = content;
    }

    @XmlElement(name = "ion-tabs")
    public IonTabs getTabs() {
        return tabs;
    }

    public void setTabs(IonTabs tabs) {
        this.tabs = tabs;
    }
}
