package XMLParsing;

import java.io.InputStream;

/**
 * Created by cstark on 12/13/2016.
 */

public abstract class XMLGetter {

    abstract public InputStream getXML(String path);

    abstract public XMLGetter GetXML();
}
