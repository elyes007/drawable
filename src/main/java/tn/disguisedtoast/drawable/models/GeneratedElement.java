package tn.disguisedtoast.drawable.models;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.reader.CSSReaderDeclarationList;
import org.jsoup.nodes.Element;


public class GeneratedElement {
    private Element element;
    private org.w3c.dom.Element domElement;
    private CSSDeclarationList cssRules;

    public GeneratedElement(Element element, org.w3c.dom.Element domElement) {
        this.element = element;
        this.domElement = domElement;
        try {
            this.cssRules = CSSReaderDeclarationList.readFromString(element.attr("style"), ECSSVersion.CSS30);
        } catch (NullPointerException e) {
            this.cssRules = new CSSDeclarationList();
        }
    }

    @Override
    public String toString() {
        return "GeneratedElement{" +
                "element=" + element +
                ", cssRules=" + cssRules +
                '}';
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public CSSDeclarationList getCssRules() {
        return this.cssRules;
    }

    public void setCssRules(CSSDeclarationList cssRules) {
        this.cssRules = cssRules;
    }

    public org.w3c.dom.Element getDomElement() {
        return domElement;
    }

    public void setDomElement(org.w3c.dom.Element domElement) {
        this.domElement = domElement;
    }
}
