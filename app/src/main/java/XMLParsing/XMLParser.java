package XMLParsing;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cstark on 12/13/2016.
 */

public class XMLParser {

    private static final String ns = null;

    public List parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();

        while(parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) continue;

            String name = parser.getName();

            if (name.equals("integer") || name.equals("bool") || name.equals("mc"))
                entries.add(readEntry(parser));

            // Starts by looking for the layout tag
            if (name.equals("layout")) {
                entries.add(readEntry(parser));
            } else skip(parser);
        }

        return entries;
    }
}
