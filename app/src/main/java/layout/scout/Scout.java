package layout.scout;

import android.animation.Animator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import org.roboriotteam3418.frc3418scouting.R;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import XMLParsing.LocalXML;
import XMLParsing.XMLParser;

public class Scout extends AppCompatActivity implements AutonomousFragment.OnFragmentInteractionListener, TeleopFragment.OnFragmentInteractionListener, NotesFragment.OnFragmentInteractionListener{

    FloatingActionButton fab, fabAuto, fabTele, fabNotes;
    LinearLayout fabLayout, fabLayoutAuto, fabLayoutTele, fabLayoutNotes;
    boolean isFABOpen = false;

    ArrayList[] nodes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scout);

        changeFragment(new ScoutFragment());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabLayout = (LinearLayout) findViewById(R.id.fabLayout);
        fabLayoutAuto = (LinearLayout) findViewById(R.id.fabLayoutAuto);
        fabLayoutTele = (LinearLayout) findViewById(R.id.fabLayoutTele);
        fabLayoutNotes = (LinearLayout) findViewById(R.id.fabLayoutNotes);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fabAuto = (FloatingActionButton) findViewById(R.id.fabAuto);
        fabTele = (FloatingActionButton) findViewById(R.id.fabTele);
        fabNotes = (FloatingActionButton) findViewById(R.id.fabNotes);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFABOpen)
                    showFABMenu();
                else
                    closeFABMenu();
            }
        });

        fabAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new AutonomousFragment());


            }
        });

        fabTele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new TeleopFragment());

            }
        });

        fabNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new NotesFragment());

            }
        });

        XMLParser parser = XMLParser.getXMLParser();



        LocalXML xml = LocalXML.GetXML();

       try {
            nodes = XMLParser.parseNew(xml.getXML("/mnt/sdcard/Download/list.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
           e.printStackTrace();
       } catch (SAXException e) {
           e.printStackTrace();
       }

    }

    public ArrayList getAutoElements() {
        return nodes[0];
    }

    public ArrayList getTeleElements() {
        return nodes[1];
    }

    private void changeFragment(Fragment newFragment) {
        // Create new fragment and transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment
        // and add the transaction to the back stack if needed
        transaction.replace(R.id.layout, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        if(isFABOpen)
            closeFABMenu();
    }

    private void showFABMenu() {
        isFABOpen = true;
        fabLayoutAuto.setVisibility(View.VISIBLE);
        fabLayoutTele.setVisibility(View.VISIBLE);
        fabLayoutNotes.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayoutAuto.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayoutTele.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayoutNotes.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
    }

    private void closeFABMenu(){
        isFABOpen=false;

        fab.animate().rotationBy(-180);
        fabLayoutAuto.animate().translationY(0);
        fabLayoutTele.animate().translationY(0);
        fabLayoutNotes.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                if(!isFABOpen){
                    fabLayoutAuto.setVisibility(View.GONE);
                    fabLayoutTele.setVisibility(View.GONE);
                    fabLayoutNotes.setVisibility(View.GONE);
                }

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scout, menu);

        return true;
    }


    @Override
    public void onAutoFragmentInteraction(Uri uri) {

    }

    @Override
    public void onTeleopFragmentInteraction(Uri uri) {

    }

    @Override
    public void onNotesFragmentInteraction(Uri uri) {

    }
}
