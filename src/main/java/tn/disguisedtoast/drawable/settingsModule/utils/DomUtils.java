package tn.disguisedtoast.drawable.settingsModule.utils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public final class DomUtils {

    public static Node getChildNode(String nodeName, Node node) {
        for(int i=0; i < node.getChildNodes().getLength(); i++) {
            Node toReturnNode = node.getChildNodes().item(i);
            if(toReturnNode.getNodeName().equals(nodeName)) {
                return toReturnNode;
            }
        }
        return null;
    }

    public static Node getDeepChildNode(String nodeName, Node node) {
        for(int i=0; i < node.getChildNodes().getLength(); i++) {
            Node toReturnNode = node.getChildNodes().item(i);
            if(toReturnNode.hasChildNodes()){
                Node childChildNode = getDeepChildNode(nodeName, toReturnNode);
                if(childChildNode != null){
                    return childChildNode;
                }
            }
            if(toReturnNode.getNodeName().equals(nodeName)) {
                return toReturnNode;
            }
        }
        return null;
    }

    public static void removeAllChilds(Node node) {
        for (Node n; (n = node.getFirstChild()) != null;) {
            if(n.hasChildNodes()) //edit to remove children of children
            {
                removeAllChilds(n);
                node.removeChild(n);
            }
            else
                node.removeChild(n);
        }
    }

}
