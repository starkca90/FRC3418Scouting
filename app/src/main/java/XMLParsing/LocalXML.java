package XMLParsing;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by cstark on 12/13/2016.
 */

public class LocalXML extends XMLGetter {

    private String tag = this.getClass().toString();

    @Override
    public BufferedReader GetXML(String path) {
        try {
            BufferedReader xmlFile = new BufferedReader(new FileReader(path));

            return xmlFile;
        } catch (FileNotFoundException e) {
            Log.e(tag, e.getStackTrace().toString());
            e.printStackTrace();
        }

        return null;
    }
}

