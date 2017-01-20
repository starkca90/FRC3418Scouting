package XMLParsing;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import ScoutingUI.BooleanEntry;
import ScoutingUI.Entry;
import ScoutingUI.IntegerEntry;
import ScoutingUI.MulEntry;

import static ScoutingUI.Entry.getEventType;

/**
 * Created by cstark on 12/13/2016.
 */

public class XMLParser {

    private static final String ns = null;

    private static XMLParser singleton;

    private static String strLayoutNode = "layout";
    private static String strAutoNode = "auto";
    private static String strTeleNode = "tele";

    private static String strEntryNode = "entry";

    public static XMLParser getXMLParser() {
        if (singleton == null) {
            singleton = new XMLParser();
        }

        return singleton;
    }

    public static ArrayList[] parseNew(InputStream path) throws ParserConfigurationException, IOException, SAXException {
        ArrayList[] entries = new ArrayList[2];

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(path);

        Element nodes = (Element) doc.getElementsByTagName(strLayoutNode).item(0);

        entries[0] = (ArrayList) parseNode(findNode(nodes, strAutoNode));
        entries[1] = (ArrayList) parseNode(findNode(nodes, strTeleNode));

        return entries;
    }

    private static Element findNode(Element list, String name) {
        return (Element) list.getElementsByTagName(name).item(0);
    }

    private static List parseNode(Element node) {
        List entries = new ArrayList();

        NodeList list = node.getElementsByTagName(strEntryNode);

        for(int i = 0; i < list.getLength(); i++) {
            entries.add(parseEntry((Element) list.item(i)));
        }


        return entries;
    }

    private static Entry parseEntry(Element entry) {
        Entry retVal = null;

        String title = null;
        Entry.EventType type = Entry.EventType.ERROR;
        String value = null;
        String options = null;
        String image = null;

        NamedNodeMap nodeMap = entry.getAttributes();

        title = nodeMap.getNamedItem("Name").getNodeValue();
        type = getEventType(nodeMap.getNamedItem("Type").getNodeValue());
        value = nodeMap.getNamedItem("Value").getNodeValue();
        image = nodeMap.getNamedItem("Image").getNodeValue();

        if(nodeMap.getLength() == 4)
            switch (type) {
                case INT:
                    retVal = new IntegerEntry(title, type, value, image);
                    break;
                case BOOL:
                    retVal = new BooleanEntry(title, type, value, image);
                    break;
                default:
                    break;
            }
        else if(nodeMap.getLength() == 5) {
            options = nodeMap.getNamedItem("Options").getNodeValue();
            switch (type) {
                case MC:
                    retVal = new MulEntry(title, type, value, options, image);
                    break;
                default:
                    break;
            }
        }

        return retVal;
    }


}
