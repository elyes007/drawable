package tn.disguisedtoast.drawable.codeGenerationModule.android.models.views;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EditText")
public class EditText extends View {

    private String hint;

    @XmlAttribute(name = "android:hint")
    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }
}
