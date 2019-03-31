package tn.disguisedtoast.drawable.settingsModule.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class DomUtils {

    public static Element getChildNode(String nodeName, Node node) {
        for(int i=0; i < node.getChildNodes().getLength(); i++) {
            Node element = node.getChildNodes().item(i);
            if(element.getNodeName().equals(nodeName.toUpperCase())) {
                return (Element) element;
            }
        }
        return null;
    }

}
