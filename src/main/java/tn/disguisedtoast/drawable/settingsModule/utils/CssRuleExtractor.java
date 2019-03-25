package tn.disguisedtoast.drawable.settingsModule.utils;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.reader.CSSReaderDeclarationList;
import org.w3c.dom.Element;

public final class CssRuleExtractor {

    public static String extractValue(CSSDeclarationList cssRules, String propertyName) throws NullPointerException {
        CSSDeclaration dec = cssRules.getDeclarationOfPropertyName(propertyName);
        if(dec != null) {
            return ((CSSExpressionMemberTermSimple)dec.getExpression().getMemberAtIndex(0)).getValue();
        }
        throw new NullPointerException("Property does not exist.");
    }
}
