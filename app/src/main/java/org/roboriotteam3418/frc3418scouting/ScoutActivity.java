package org.roboriotteam3418.frc3418scouting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import XMLParsing.LocalXML;
import XMLParsing.XMLParser;

public class ScoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        XMLParser parser = XMLParser.getXMLParser();



        LocalXML xml = LocalXML.GetXML();

/*        try {
            List entries = XMLParser.parse(xml.getXML("/mnt/sdcard/Download/list.xml"));

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);

        return true;
    }

}
