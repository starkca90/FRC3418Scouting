/*
 * Copyright (c) 2017. RoboRiot and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This file is part of RoboRiot Scouting.
 *
 * RoboRiot Scouting is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RoboRiot Scouting is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RoboRiot Scouting.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.roboriotteam3418.frc3418scouting.Application;

import android.animation.Animator;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.roboriotteam3418.frc3418scouting.DataStructures.Match;
import org.roboriotteam3418.frc3418scouting.DataStructures.ScheduleEntry;
import org.roboriotteam3418.frc3418scouting.Database.MatchesDataSource;
import org.roboriotteam3418.frc3418scouting.Entries.BooleanEntry;
import org.roboriotteam3418.frc3418scouting.Entries.Entry;
import org.roboriotteam3418.frc3418scouting.Entries.IntegerEntry;
import org.roboriotteam3418.frc3418scouting.Entries.MulEntry;
import org.roboriotteam3418.frc3418scouting.Layout.AutonomousFragment;
import org.roboriotteam3418.frc3418scouting.Layout.NotesFragment;
import org.roboriotteam3418.frc3418scouting.Layout.PostMatchFragment;
import org.roboriotteam3418.frc3418scouting.Layout.ScoutFragment;
import org.roboriotteam3418.frc3418scouting.Layout.TeleopFragment;
import org.roboriotteam3418.frc3418scouting.R;
import org.roboriotteam3418.frc3418scouting.XMLParsing.LocalXML;
import org.roboriotteam3418.frc3418scouting.XMLParsing.XMLParser;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Responsible for controlling the main functionality of the app. Controls
 * fragment switching, saving and central point.
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */

public class ScoutActivity extends AppCompatActivity implements NotesFragment.OnFragmentInteractionListener {

    private final String storageDir = "FRC";
    private final String localPath = Environment.getExternalStorageDirectory().getPath() + "/" + storageDir + "/list.xml";

    // Preference keys for saving and retrieving preferences
    private final String prefAutoKey = "auto";
    private final String prefTeleKey = "tele";
    private final String prefPostKey = "post";
    private final String prefRecoKey = "reco";

    // Floating Action Button variables
    private FloatingActionButton fab;
    private LinearLayout fabLayoutAuto;
    private LinearLayout fabLayoutTele;
    private LinearLayout fabLayoutPost;
    private LinearLayout fabLayoutNotes;
    private LinearLayout fabLayoutHome;
    private boolean isFABOpen = false;

    private Fragment currentFragment;

    private TextView tvABTeam;
    private TextView tvABMatch;

    private ArrayList[] nodes;

    private final int startingMatch = 1;

    // Interface to backend database
    private MatchesDataSource mds;

    /**
     * Responsible for initializing all controls of the activity
     * <p>
     * Sets listeners to FAB, Loads data from preferences.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scout);

        mds = MatchesDataSource.getMDS(this);

        // Load home fragment
        changeFragment(new ScoutFragment());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_view);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        tvABTeam = (TextView) findViewById(R.id.tvABTeam);
        tvABMatch = (TextView) findViewById(R.id.tvABMatch);

        findViewById(R.id.btnABNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment instanceof AutonomousFragment)
                    changeFragment(new TeleopFragment());
                else if (currentFragment instanceof TeleopFragment)
                    changeFragment(new PostMatchFragment());
                else if (currentFragment instanceof PostMatchFragment) {
                    mds.loadMatch(Match.getMatch().getMatchNumber() + 1);
                    changeFragment(new ScoutFragment());
                } else if (currentFragment instanceof ScoutFragment)
                    changeFragment(new AutonomousFragment());
            }
        });

        // Collect all Floating Action Button elements
        fabLayoutAuto = (LinearLayout) findViewById(R.id.fabLayoutAuto);
        fabLayoutTele = (LinearLayout) findViewById(R.id.fabLayoutTele);
        fabLayoutPost = (LinearLayout) findViewById(R.id.fabLayoutPost);
        fabLayoutNotes = (LinearLayout) findViewById(R.id.fabLayoutNotes);
        fabLayoutHome = (LinearLayout) findViewById(R.id.fabLayoutHome);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        FloatingActionButton fabAuto = (FloatingActionButton) findViewById(R.id.fabAuto);
        FloatingActionButton fabTele = (FloatingActionButton) findViewById(R.id.fabTele);
        FloatingActionButton fabPost = (FloatingActionButton) findViewById(R.id.fabPost);
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

        fabPost.setOnClickListener(view -> changeFragment(new PostMatchFragment()));

        fabNotes.setOnClickListener(v -> Toast.makeText(this, "Sorry, I don't exist =(", Toast.LENGTH_SHORT).show());

        fabHome.setOnClickListener(v -> changeFragment(new ScoutFragment()));

        loadPreferences();
    }

    @Override
    public void onBackPressed() {
        Snackbar snack = Snackbar.make(findViewById(R.id.layout), "To go back to previous phase, select it from the list", Snackbar.LENGTH_LONG);
        snack.setAction("Go Home", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment(new ScoutFragment());
            }
        });
        snack.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFABOpen) {
                Rect primaryRect = new Rect();
                Rect homeRect = new Rect();
                fab.getGlobalVisibleRect(primaryRect);
                fabLayoutHome.getGlobalVisibleRect(homeRect);
                Rect allRect = new Rect(homeRect.left, homeRect.top, primaryRect.right, primaryRect.bottom);
                if (!allRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    closeFABMenu();
                    return false;
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Responsible for getting application's preferences and parsing information stored there
     */
    private void loadPreferences() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Load recorder name from preferences
        Match.getMatch().setRecorder(sharedPrefs.getString(prefRecoKey, "Default"));

        // Parse nodes from formatted strings
        Gson gson = new Gson();
        String jsonAuto = sharedPrefs.getString(prefAutoKey, null);
        String jsonTele = sharedPrefs.getString(prefTeleKey, null);
        String jsonPost = sharedPrefs.getString(prefPostKey, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<? extends String> autoNode = gson.fromJson(jsonAuto, type);
        ArrayList<? extends String> teleNode = gson.fromJson(jsonTele, type);
        ArrayList<? extends String> postNode = gson.fromJson(jsonPost, type);

        // Were nodes loaded from preferences?
        if (autoNode != null) {
            // Yes, parse the strings and store the information
            nodes = new ArrayList[4];
            nodes[0] = nodeParsing(autoNode);
            nodes[1] = nodeParsing(teleNode);
            nodes[2] = nodeParsing(postNode);

            // Load first match from SQL
            mds.loadMatch(startingMatch);
        } else {
            // No, need to get that information
            reloadParameters(localPath);
        }
    }

    private void storePreferences(String key, String data) {
        // Store new node information is application's preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        // Store nodes in preferences for future launches
        editor.putString(key, data);

        // Actually store preference
        editor.apply();
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

            // Store attained information in application preferences
            storePreferences(prefAutoKey, nodeToJson(getAutoElements()));
            storePreferences(prefTeleKey, nodeToJson(getTeleElements()));
            storePreferences(prefPostKey, nodeToJson(getPostElements()));
            storePreferences(prefRecoKey, Match.getMatch().getRecorder());

            // Rebuild database with new structure
            mds.upgradeTable();

            // Pre-populate database with attained schedule
            parseSchedule(getSchedElements());

            // Load first match as starting point
            mds.loadMatch(startingMatch);

            // Goto ScoutFragment as starting point
            changeFragment(new ScoutFragment());

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Responsible for parsing file at path and getting schedule information and
     * updating database with new data
     * @param path Path to file to be used for reading schedule from
     */
    private void reloadSchedule(String path) {
        LocalXML xml = LocalXML.GetXML();

        try {
            // Store schedule information in node array for future reference
            nodes[3] = XMLParser.updateSched(xml.getXML(path));

            // Pre-populate database with attained schedule
            parseSchedule(getSchedElements());

            // Load first match as starting point
            mds.loadMatch(startingMatch);

            // Goto ScoutFragment as starting point
            changeFragment(new ScoutFragment());

        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * Responsible for iterating through schedule node and pre-populating database with matches
     * and team information for each match
     * @param node List of all schedule information
     */
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

    public ArrayList getPostElements() {
        return nodes[2];
    }

    public ArrayList getSchedElements() {
        return nodes[3];
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

        // Commit the transaction
        transaction.commit();

        currentFragment = newFragment;

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
        fabLayoutPost.setVisibility(View.VISIBLE);
        fabLayoutNotes.setVisibility(View.GONE); //TODO: Notes Fragment
        fabLayoutHome.setVisibility(View.VISIBLE);

        fab.animate().rotationBy(180);
        fabLayoutAuto.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fabLayoutTele.animate().translationY(-getResources().getDimension(R.dimen.standard_100));
        fabLayoutPost.animate().translationY(-getResources().getDimension(R.dimen.standard_145));
        fabLayoutHome.animate().translationY(-getResources().getDimension(R.dimen.standard_190));
        fabLayoutNotes.animate().translationY(-getResources().getDimension(R.dimen.standard_235));
    }

    /**
     * Animate the FAB Menu closing
     */
    private void closeFABMenu(){
        isFABOpen=false;

        fab.animate().rotationBy(-180);
        fabLayoutAuto.animate().translationY(0);
        fabLayoutTele.animate().translationY(0);
        fabLayoutPost.animate().translationY(0);
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
                    fabLayoutPost.setVisibility(View.GONE);
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
                storePreferences(prefRecoKey, Match.getMatch().getRecorder());
                break;

            case R.id.actionSave:
                String fileName = Match.getMatch().getRecorder() + ".csv";
                mds.outputDatabase(storageDir, fileName);
                Toast.makeText(this, "Database Saved", Toast.LENGTH_SHORT).show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void updateAB(String team, String match) {
        tvABTeam.setText(team);
        tvABMatch.setText(match);
    }

    @Override
    public void onNotesFragmentInteraction(Uri uri) {

    }

}
