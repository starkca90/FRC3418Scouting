package XMLParsing;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by cstark on 12/13/2016.
 */

public class LocalXML {

    private String tag = this.getClass().toString();

    private static LocalXML singleton;

    public static LocalXML GetXML() {
        if (singleton == null) {
            singleton = new LocalXML();
        }

        return singleton;
    }

    public InputStream getXML(String path) {
        try {
            BufferedReader xmlFile = new BufferedReader(new FileReader(path));

            char[] charBuffer = new char[8 * 1024];
            StringBuilder builder = new StringBuilder();
            int numCharsRead;
            while ((numCharsRead = xmlFile.read(charBuffer, 0, charBuffer.length)) != -1) {
                builder.append(charBuffer, 0, numCharsRead);
            }

            InputStream targetStream = new ByteArrayInputStream(
                    builder.toString().getBytes(StandardCharsets.UTF_8));

            xmlFile.close();

            return targetStream;
        } catch (FileNotFoundException e) {
            Log.e(tag, e.getStackTrace().toString());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

