package tn.disguisedtoast.drawable.settingsModule.utils;

import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpressionMemberTermSimple;
import com.helger.css.reader.CSSReaderDeclarationList;
import org.jsoup.nodes.Element;

public final class CssRuleExtractor {

    public static String extractValue(CSSDeclarationList cssRules, String propertyName) throws NullPointerException {
        CSSDeclaration dec = cssRules.getDeclarationOfPropertyName(propertyName);
        if(dec != null) {
            return ((CSSExpressionMemberTermSimple)dec.getExpression().getMemberAtIndex(0)).getValue();
        }
        throw new NullPointerException("Property does not exist.");
    }

    public static CSSDeclarationList getCssRules(Element element) {
        CSSDeclarationList cssRules;
        try {
            cssRules = CSSReaderDeclarationList.readFromString(element.attr("style"), ECSSVersion.CSS30);
        } catch (NullPointerException e) {
            cssRules = new CSSDeclarationList();
        }
        return cssRules;
    }
}
