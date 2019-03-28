package tn.disguisedtoast.drawable.models;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.reader.CSSReaderDeclarationList;
import org.w3c.dom.Element;
import tn.disguisedtoast.drawable.settingsModule.models.Setting;

public class GeneratedElement {
    private Element element;
    private CSSDeclarationList cssRules;

    public GeneratedElement(org.w3c.dom.Element element) {
        this.element = element;
        try {
            this.cssRules = CSSReaderDeclarationList.readFromString(element.getAttribute("style"), ECSSVersion.CSS30);
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

    public org.w3c.dom.Element getElement() {
        return element;
    }

    public void setElement(org.w3c.dom.Element element) {
        this.element = element;
    }

    public CSSDeclarationList getCssRules() {
        return this.cssRules;
    }

    public void setCssRules(CSSDeclarationList cssRules) {
        this.cssRules = cssRules;
    }
}
