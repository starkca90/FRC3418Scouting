package org.roboriotteam3418.frc3418scouting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import XMLParsing.LocalXML;
import XMLParsing.XMLParser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XMLParser parser = XMLParser.getXMLParser();
        LocalXML xml = LocalXML.GetXML(this);

        try {
            parser.parse(xml.getXML("/mnt/sdcard/Download/list.xml"));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
