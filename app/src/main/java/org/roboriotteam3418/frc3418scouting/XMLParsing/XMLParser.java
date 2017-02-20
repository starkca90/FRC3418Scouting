/*
 * Copyright (c) 2017. RoboRiot and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of RoboRiot Scouting.
 *
 * RoboRiot Scouting is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RoboRiot Scouting is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RoboRiot Scouting.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.roboriotteam3418.frc3418scouting.XMLParsing;

import org.roboriotteam3418.frc3418scouting.DataStructures.Match;
import org.roboriotteam3418.frc3418scouting.DataStructures.ScheduleEntry;
import org.roboriotteam3418.frc3418scouting.Entries.BooleanEntry;
import org.roboriotteam3418.frc3418scouting.Entries.Entry;
import org.roboriotteam3418.frc3418scouting.Entries.IntegerEntry;
import org.roboriotteam3418.frc3418scouting.Entries.MulEntry;
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

import static org.roboriotteam3418.frc3418scouting.Entries.Entry.getEventType;

/**
 * Responsible for getting information from an XML stream that contains the information
 * required for the different entries of teleop and autonomous as well as schedule and recorder
 * <p>
 * Template of file is as follows:
 * <layout>
 * <recorder Name="[RECORDER]" />
 * <p>
 * <schedule>
 * <match Team="[TEAM#]" Alliance="[ALLIANCE]" />
 * <match ... />
 * <.../>
 * </schedule>
 * <p>
 * <auto>
 * <entry Name="autoInt" Type="INT" Value="0" Image=""/>
 * <entry Name="autoBool" Type="BOOL" Value="0" Image=""/>
 * <entry Name="autoMC" Type="MC" Value="0" Options="Option1,Option2" Image=""/>
 * </auto>
 * <p>
 * <tele>
 * <entry Name="teleInt" Type="INT" Value="0" Image=""/>
 * <entry Name="teleBool" Type="BOOL" Value="0" Image=""/>
 * <entry Name="teleMc" Type="MC" Value="0" Options="Option1,Option2" Image=""/>
 * </tele>
 * <p>
 * <post>
 * <entry Name="postInt" Type="INT" Value="0" Image=""/>
 * <entry Name="postBool" Type="BOOL" Value="0" Image=""/>
 * <entry Name="postMc" Type="MC" Value="0" Options="Option1,Option2" Image=""/>
 * </post>
 * </layout>
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class XMLParser {

    // Keys used in XML file
    private static String strLayoutNode = "layout";
    private static String strRecorNode = "recorder";
    private static String strSchedNode = "scheduled";

    private static String strAutoNode = "auto";
    private static String strTeleNode = "tele";
    private static String strPostNode = "post";
    private static String strEntryNode = "entry";
    private static String strSchMatchNode = "match";

    /**
     * Responsible for reading the XML file and getting the schedule node.
     *
     * From the information, create a list with the schedule information that will be
     * used to update the database
     * @param path InputStream of XML document
     * @return ArrayList containing list of schedule information
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static ArrayList updateSched(InputStream path) throws ParserConfigurationException, IOException, SAXException {
        ArrayList entries;

        Element nodes = getNodes(path);

        Match.getMatch().setRecorder(parseRecorder(findNode(nodes, strRecorNode)));

        entries = (ArrayList) parseSchedule(findNode(nodes, strSchedNode));

        return entries;
    }

    /**
     * Responsible for reading the XML file and reading all iformation from it and
     * updating the necessary fields
     *
     * @param path InputStream of XML file
     * @return ArrayList[] containing information about Autonomous, Teleop and Schedule
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static ArrayList[] parseNew(InputStream path) throws ParserConfigurationException, IOException, SAXException {
        ArrayList[] entries = new ArrayList[4];

        Element nodes = getNodes(path);

        Match.getMatch().setRecorder(parseRecorder(findNode(nodes, strRecorNode)));
        entries[0] = (ArrayList) parseNode(findNode(nodes, strAutoNode));
        entries[1] = (ArrayList) parseNode(findNode(nodes, strTeleNode));
        entries[2] = (ArrayList) parseNode(findNode(nodes, strPostNode));
        entries[3] = (ArrayList) parseSchedule(findNode(nodes, strSchedNode));

        return entries;
    }

    /**
     * Finds the desired node from the XML Element
     * @param list XML Element list
     * @param name String of desired node
     * @return XML Element of desired node
     */
    private static Element findNode(Element list, String name) {
        return (Element) list.getElementsByTagName(name).item(0);
    }

    /**
     * Responsible for getting the recorder information from the XML file
     * @param node XLM Element of recorder node
     * @return String of recorder name
     */
    private static String parseRecorder(Element node) {
        String retVal = "Default";

        if (node != null) {
            NamedNodeMap nodeMap = node.getAttributes();
            retVal = nodeMap.getNamedItem("Name").getNodeValue();
        }
        return retVal;
    }

    /**
     * Responsible for parsing the XML's schedule node to create a list of
     * ScheduleEntrys that will be used to pre-populate the database
     * @param node XML Element of schedule node
     * @return List of ScheduleEntry objects that make up the schedule
     */
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

    /**
     * Responsible for parsing each Match entry in the schedule node and putting the data in
     * a ScheduleEntry
     * @param entry XML Element of a match entry
     * @return ScheduleEntry object containing that match's information
     */
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

    /**
     * Responsible for iterating through the Match criteria from the XML file
     * @param node XML Element of the current criteria list being parsed
     * @return List of Entry objects
     */
    private static List<Entry> parseNode(Element node) {
        List<Entry> entries = new ArrayList<>();

        if (node != null) {
            NodeList list = node.getElementsByTagName(strEntryNode);

            // Iterate through each element
            for (int i = 0; i < list.getLength(); i++) {
                entries.add(parseEntry((Element) list.item(i)));
            }
        }

        return entries;
    }

    /**
     * Responsible for parsing an entry and getting the required information and creating an
     * Entry object
     * @param entry XML Element of the current entry
     * @return Entry object populated with information from XML entry
     */
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

    /**
     * Responsible for getting layout XML element from InputStream
     * @param path InputStream of XML file
     * @return XML Element of Layout element
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private static Element getNodes(InputStream path) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(path);

        return (Element) doc.getElementsByTagName(strLayoutNode).item(0);
    }
}
