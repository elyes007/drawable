package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlTransient
@XmlType(propOrder = {"header", "content", "tabs"})
public class IonContainer {

    protected IonHeader header = new IonHeader();
    protected IonContent content;
    protected IonTabs tabs;

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
