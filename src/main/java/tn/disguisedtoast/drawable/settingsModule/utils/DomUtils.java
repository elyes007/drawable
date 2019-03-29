package tn.disguisedtoast.drawable.settingsModule.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

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

    public static String getDocumentHtml(Document document) {
        try{
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer trans = tf.newTransformer();
            StringWriter sw = new StringWriter();
            trans.setOutputProperty(OutputKeys.METHOD, "html");
            trans.transform(new DOMSource(document), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
