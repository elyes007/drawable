package tn.disguisedtoast.drawable.codeGenerationModule.ionic.models;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "div")
public class Div extends IonContainer {

    private String classe;
    private String main;

    public Div() {
    }

    public Div(String classe, String main) {
        this.classe = classe;
        this.main = main;
    }

    @XmlAttribute(name = "class")
    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @XmlAttribute(name = "main")
    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
