package org.roboriotteam3418.frc3418scouting;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import Schedule.ScheduleEntry;
import ScoutingUI.BooleanEntry;
import ScoutingUI.Entry;
import ScoutingUI.IntegerEntry;
import ScoutingUI.MulEntry;
import XMLParsing.LocalXML;
import XMLParsing.XMLParser;
import layout.scout.AutonomousFragment;
import layout.scout.NotesFragment;
import layout.scout.ScoutFragment;
import layout.scout.TeleopFragment;

public class ScoutActivity extends AppCompatActivity implements NotesFragment.OnFragmentInteractionListener {

    private final String storageDir = "FRC";
    private final String localPath = Environment.getExternalStorageDirectory().getPath() + "/" + storageDir + "/list.xml";
    private final String prefAutoKey = "auto";
    private final String prefTeleKey = "tele";
    private final String prefRecoKey = "reco";
    private FloatingActionButton fab;
    private LinearLayout fabLayoutAuto;
    private LinearLayout fabLayoutTele;
    private LinearLayout fabLayoutNotes;
    private LinearLayout fabLayoutHome;
    private boolean isFABOpen = false;
    private ArrayList[] nodes;

    private final int startingMatch = 1;

    private MatchesDataSource mds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scout);

        mds = MatchesDataSource.getMDS(this);

        // Load home fragment
        changeFragment(new ScoutFragment());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Collection all Floating Action Button elements
        fabLayoutAuto = (LinearLayout) findViewById(R.id.fabLayoutAuto);
        fabLayoutTele = (LinearLayout) findViewById(R.id.fabLayoutTele);
        fabLayoutNotes = (LinearLayout) findViewById(R.id.fabLayoutNotes);
        fabLayoutHome = (LinearLayout) findViewById(R.id.fabLayoutHome);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fabAuto = (FloatingActionButton) findViewById(R.id.fabAuto);
        FloatingActionButton fabTele = (FloatingActionButton) findViewById(R.id.fabTele);
        FloatingActionButton fabNotes = (FloatingActionButton) findViewById(R.id.fabNotes);
        FloatingActionButton fabHome = (FloatingActionButton) findViewById(R.id.fabHome);

        // Set click listeners for FABs
        fab.setOnClickListener(view -> {
            if(!isFABOpen)
                showFABMenu();
            else
                closeFABMenu();
        });

        fabAuto.setOnClickListener(view -> changeFragment(new AutonomousFragment()));

        fabTele.setOnClickListener(view -> changeFragment(new TeleopFragment()));

        fabNotes.setOnClickListener(v -> changeFragment(new NotesFragment()));

        fabHome.setOnClickListener(v -> changeFragment(new ScoutFragment()));

        // Try to load nodes from App's preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Match.getMatch().setRecorder(sharedPrefs.getString(prefRecoKey, "Default"));

        Gson gson = new Gson();
        String jsonAuto = sharedPrefs.getString(prefAutoKey, null);
        String jsonTele = sharedPrefs.getString(prefTeleKey, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<? extends String> autoNode = gson.fromJson(jsonAuto, type);
        ArrayList<? extends String> teleNode = gson.fromJson(jsonTele, type);

        // Were nodes loaded from preferences?
        if (autoNode != null) {
            // Yes, parse the strings and store the information
            nodes = new ArrayList[3];
            nodes[0] = nodeParsing(autoNode);
            nodes[1] = nodeParsing(teleNode);

            // Load first match from SQL
            mds.loadMatch(startingMatch);
        } else {
            // No, need to get that information
            reloadParameters(localPath);
        }

    }

    /**
     * Parses strings retrieved from preferences into usable node elements
     * @param node Current list of strings making up the node components
     * @return The list of nodes for the competition
     */
    private ArrayList<Entry> nodeParsing(ArrayList<? extends String> node) {
        ArrayList<Entry> retVal = new ArrayList<>();

        for(int i = 0; i < node.size(); i++) {
            // Node was stored as a comma separated string, break it up
            String[] nodeElements = (node.get(i)).split("\\s*,\\s*");
            String image;

            // Since image is not implemented, workaround until it is removed or implemented
            // TODO: Image
            // When INT and BOOL types are stored, the image value get dropped since it is empty
            // set image as an empty string to not break anything. MC has available strings, so just
            // put one of those as the image =D, that won't break anything... HA!
            if(nodeElements.length < 4)
                image = "";
            else
                image = nodeElements[3];

            // Decode string to Entry type
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

                        // Iterate through the remaining strings to get options
                        for(int j = 4; j < nodeElements.length; j++) {
                            b.append(nodeElements[j]);
                            b.append(",");
                        }

                        retVal.add(new MulEntry(nodeElements[0], eType, nodeElements[2], b.toString(), image));
                        break;
                    default:
                        break;
            }
        }
            return retVal;
    }

    /**
     * Retrieve the XML file and re-build the database and re-create nodes
     * @param path location where XML file is located - Currently only local locations are supported
     */
    private void reloadParameters(String path) {
        LocalXML xml = LocalXML.GetXML();

        try {
            nodes = XMLParser.parseNew(xml.getXML(path));

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPrefs.edit();

            // Store nodes in preferences for future launches
            editor.putString(prefAutoKey, nodeToJson(getAutoElements()));
            editor.putString(prefTeleKey, nodeToJson(getTeleElements()));
            editor.putString(prefRecoKey, Match.getMatch().getRecorder());

            editor.apply();

            mds.upgradeTable();

            parseSchedule(getSchedElements());

            mds.loadMatch(startingMatch);
            changeFragment(new ScoutFragment());

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void reloadSchedule(String path) {
        LocalXML xml = LocalXML.GetXML();

        try {
            nodes[2] = XMLParser.updateSched(xml.getXML(path));

            parseSchedule(getSchedElements());

            mds.loadMatch(startingMatch);
            changeFragment(new ScoutFragment());

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void parseSchedule(List node) {
        for (int i = 0; i < node.size(); i++) {
            ScheduleEntry entry = (ScheduleEntry) node.get(i);
            mds.prefillMatch(i + 1, entry.getTeam(), entry.getAlliance());
        }
    }

    /**
     * Converts a node to a json formatted string
     * @param node Node to be converted to json
     * @return Json formatted string of a node
     */
    private String nodeToJson(List node) {
        ArrayList<String> nodeStrings = new ArrayList<>();
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

    public ArrayList getSchedElements() {
        return nodes[2];
    }

    /**
     * Change the current fragment to the desired fragment
     * @param newFragment Instance of desired fragment
     */
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

    /**
     * Animate the FAB Menu opening
     */
    private void showFABMenu() {
        isFABOpen = true;
        fabLayoutAuto.setVisibility(View.VISIBLE);
        fabLayoutTele.setVisibility(View.VISIBLE);
        fabLayoutNotes.setVisibility(View.VISIBLE);
        fabLayoutHome.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayoutAuto.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayoutTele.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayoutHome.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
        fabLayoutNotes.animate().translationY(-getResources().getDimension(R.dimen.standard_190));
    }

    /**
     * Animate the FAB Menu closing
     */
    private void closeFABMenu(){
        isFABOpen=false;

        fab.animate().rotationBy(-180);
        fabLayoutAuto.animate().translationY(0);
        fabLayoutTele.animate().translationY(0);
        fabLayoutHome.animate().translationY(0);
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
                    fabLayoutHome.setVisibility(View.GONE);
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
                reloadSchedule(localPath);

                // Store recorder information from file
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPrefs.edit();

                editor.putString(prefRecoKey, Match.getMatch().getRecorder());

                editor.apply();
                break;

            case R.id.actionSave:
                String fileName = Match.getMatch().getRecorder() + ".csv";
                mds.outputDatabase(storageDir, fileName);
                Toast.makeText(this, "Database Saved", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNotesFragmentInteraction(Uri uri) {

    }
}
