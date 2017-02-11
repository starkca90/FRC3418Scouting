package XMLParsing;

import org.roboriotteam3418.frc3418scouting.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import Schedule.ScheduleEntry;
import ScoutingUI.BooleanEntry;
import ScoutingUI.Entry;
import ScoutingUI.IntegerEntry;
import ScoutingUI.MulEntry;

import static ScoutingUI.Entry.getEventType;

public class XMLParser {

    private static String strLayoutNode = "layout";
    private static String strRecorNode = "recorder";
    private static String strSchedNode = "scheduled";

    private static String strAutoNode = "auto";
    private static String strTeleNode = "tele";
    private static String strEntryNode = "entry";
    private static String strSchMatchNode = "match";

    public static ArrayList updateSched(InputStream path) throws ParserConfigurationException, IOException, SAXException {
        ArrayList entries;

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(path);

        Element nodes = (Element) doc.getElementsByTagName(strLayoutNode).item(0);

        Match.getMatch().setRecorder(parseRecorder(findNode(nodes, strRecorNode)));

        entries = (ArrayList) parseSchedule(findNode(nodes, strSchedNode));

        return entries;
    }

    public static ArrayList[] parseNew(InputStream path) throws ParserConfigurationException, IOException, SAXException {
        ArrayList[] entries = new ArrayList[3];

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(path);

        Element nodes = (Element) doc.getElementsByTagName(strLayoutNode).item(0);

        Match.getMatch().setRecorder(parseRecorder(findNode(nodes, strRecorNode)));

        entries[0] = (ArrayList) parseNode(findNode(nodes, strAutoNode));
        entries[1] = (ArrayList) parseNode(findNode(nodes, strTeleNode));
        entries[2] = (ArrayList) parseSchedule(findNode(nodes, strSchedNode));

        return entries;
    }

    private static Element findNode(Element list, String name) {
        return (Element) list.getElementsByTagName(name).item(0);
    }

    private static String parseRecorder(Element node) {
        String retVal = "Default";

        if (node != null) {
            NamedNodeMap nodeMap = node.getAttributes();
            retVal = nodeMap.getNamedItem("Name").getNodeValue();
        }
        return retVal;
    }

    private static List<ScheduleEntry> parseSchedule(Element node) {

        List<ScheduleEntry> entries = new ArrayList<>();

        if (node != null) {

            NodeList list = node.getElementsByTagName(strSchMatchNode);

            for (int i = 0; i < list.getLength(); i++) {
                entries.add(parseSchMatch((Element) list.item(i)));
            }
        }

        return entries;
    }

    private static ScheduleEntry parseSchMatch(Element entry) {
        ScheduleEntry retVal;

        String team;
        String alliance;

        NamedNodeMap nodeMap = entry.getAttributes();

        team = nodeMap.getNamedItem("Team").getNodeValue();
        alliance = nodeMap.getNamedItem("Alliance").getNodeValue();

        retVal = new ScheduleEntry(team, alliance);

        return retVal;
    }

    private static List<Entry> parseNode(Element node) {
        List<Entry> entries = new ArrayList<>();

        if (node != null) {
            NodeList list = node.getElementsByTagName(strEntryNode);

            for (int i = 0; i < list.getLength(); i++) {
                entries.add(parseEntry((Element) list.item(i)));
            }
        }

        return entries;
    }

    private static Entry parseEntry(Element entry) {
        Entry retVal = null;

        String title;
        Entry.EventType type;
        String value;
        String options;
        String image;

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
