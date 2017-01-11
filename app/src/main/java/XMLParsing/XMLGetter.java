package XMLParsing;

import java.io.InputStream;

/**
 * Created by cstark on 12/13/2016.
 */

public abstract class XMLGetter {

    public abstract InputStream getXML(String path);

    public abstract XMLGetter GetXML();
}
