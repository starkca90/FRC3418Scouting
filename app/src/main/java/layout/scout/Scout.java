package layout.scout;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.roboriotteam3418.frc3418scouting.R;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import ScoutingUI.BooleanEntry;
import ScoutingUI.Entry;
import ScoutingUI.IntegerEntry;
import ScoutingUI.MulEntry;
import XMLParsing.LocalXML;
import XMLParsing.XMLParser;

public class Scout extends AppCompatActivity implements AutonomousFragment.OnFragmentInteractionListener, TeleopFragment.OnFragmentInteractionListener, NotesFragment.OnFragmentInteractionListener{

    FloatingActionButton fab, fabAuto, fabTele, fabNotes;
    LinearLayout fabLayout, fabLayoutAuto, fabLayoutTele, fabLayoutNotes;
    boolean isFABOpen = false;

    ArrayList[] nodes;

    private final String localPath = "/mnt/sdcard/Download/list.xml";

    private final String prefAutoKey = "auto";
    private final String prefTeleKey = "tele";

    private SharedPreferences mPrefs;

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

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String jsonAuto = sharedPrefs.getString(prefAutoKey, null);
        String jsonTele = sharedPrefs.getString(prefTeleKey, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList autoNode = gson.fromJson(jsonAuto, type);
        ArrayList teleNode = gson.fromJson(jsonTele, type);

        nodes = new ArrayList[2];
        nodes[0] = nodeParsing(autoNode);
        nodes[1] = nodeParsing(teleNode);
//        nodes = gson.fromJson(json, type);

        if(nodes == null)
            reloadParameters(localPath);

    }

    private ArrayList nodeParsing(ArrayList node) {
        ArrayList retVal = new ArrayList();

        for(int i = 0; i < node.size(); i++) {
            String[] nodeElements = ((String) node.get(i)).split("\\s*,\\s*");
            String image;
            if(nodeElements.length < 4)
                image = "";
            else
                image = nodeElements[3];

            Entry.EventType eType = Entry.getEventType(nodeElements[1]);

                switch (eType) {
                    case INT:
                        retVal.add(new IntegerEntry(nodeElements[0], eType, nodeElements[2], image));
                        break;
                    case BOOL:
                        retVal.add(new BooleanEntry(nodeElements[0], eType, nodeElements[2], image));
                        break;
                    case MC:
                        StringBuilder b = new StringBuilder();

                        for(int j = 4; j < nodeElements.length; j++) {
                            b.append(nodeElements[j]);
                            b.append(",");
                        }

                        retVal.add(new MulEntry(nodeElements[0], eType, nodeElements[2],b.toString(), image));
                        break;
                    default:
                        break;
            }
        }
            return retVal;
    }

    private void reloadParameters(String path) {
        LocalXML xml = LocalXML.GetXML();

        try {
            nodes = XMLParser.parseNew(xml.getXML(path));

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPrefs.edit();

            editor.putString(prefAutoKey, nodeToJson(getAutoElements()));
            editor.putString(prefTeleKey, nodeToJson(getTeleElements()));

            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    private String nodeToJson(List node) {
        ArrayList nodeStrings = new ArrayList();
        for(int i = 0; i < node.size(); i++) {
            nodeStrings.add(node.get(i).toString());
        }

        Gson gson = new Gson();

        return gson.toJson(nodeStrings);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                reloadParameters(localPath);
                break;

            case R.id.actionSave:

                break;

            case R.id.action_settings:

                break;
        }
        return super.onOptionsItemSelected(item);
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
