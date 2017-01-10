package XMLParsing;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ScoutingUI.Entry;

/**
 * Created by cstark on 12/13/2016.
 */

public class XMLParser {

    private static final String ns = null;

    private static XMLParser singleton;

    public static XMLParser getXMLParser() {
        if (singleton == null) {
            singleton = new XMLParser();
        }

        return singleton;
    }

    public static List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(in, null);
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private static List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        int event = parser.getEventType();
        while (event != XmlPullParser.END_DOCUMENT) {
            String name = parser.getName();

            switch (event) {
                case XmlPullParser.START_TAG:
                    break;

                case XmlPullParser.END_TAG:
                    if (name.equals("entry"))
                        entries.add(readEntry(parser));

                    // Starts by looking for the layout tag
                    if (name.equals("layout")) {
                        entries.add(readEntry(parser));
                    }
                    break;
            }

            event = parser.next();
        }

        return entries;
    }

    private static Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {

        Entry retVal = null;

        String title = null;
        Entry.EventType type = Entry.EventType.ERROR;
        String value = null;
        String options = null;
        String image = null;


        title = parser.getAttributeValue(null, "Name");
        type = getEventType(parser.getAttributeValue(null, "Type"));
        value = parser.getAttributeValue(null, "Value");
        image = parser.getAttributeValue(null, "Image");

        if (parser.getAttributeCount() == 4) {
            retVal = new Entry(title, type, value, image);
        } else if (parser.getAttributeCount() == 5) {
            options = parser.getAttributeValue(null, "Options");
            retVal = new Entry(title, type, value, options, image);
        }

        return retVal;
    }

    private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }

        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    private static Entry.EventType getEventType(String type) {
        Entry.EventType retVal = Entry.EventType.ERROR;

        if (type.equals("INT"))
            retVal = Entry.EventType.INT;
        else if (type.equals("BOOL"))
            retVal = Entry.EventType.BOOL;
        else if (type.equals("MC"))
            retVal = Entry.EventType.MC;

        return retVal;
    }
}
