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

package org.roboriotteam3418.frc3418scouting.Layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.roboriotteam3418.frc3418scouting.DataStructures.Match;
import org.roboriotteam3418.frc3418scouting.Database.MatchesDataSource;
import org.roboriotteam3418.frc3418scouting.R;

import java.util.Locale;

/**
 * This class is responsible for displaying the information about the match including the
 * team and alliance.
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class ScoutFragment extends Fragment {

    // Maps integers to the alliance
    private static final SparseArray<Match.Alliance> intToAllianceMap = new SparseArray<>();
    static {
        for(Match.Alliance type : Match.Alliance.values()) {
            intToAllianceMap.put(type.ordinal(), type);
        }
    }

    Spinner spAlliance;
    EditText etTeam;
    EditText etMatch;

    public ScoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scout, container, false);

        etTeam = (EditText) v.findViewById(R.id.etTeam);
        etTeam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String team = etTeam.getText().toString();
                Match.getMatch().setTeam(team);
                MatchesDataSource mds = MatchesDataSource.getMDS(getContext());
                mds.updateTeam(team, Match.getMatch().getMatchNumber());
            }
        });

        etMatch = (EditText) v.findViewById(R.id.etMatch);

        Button btnMatchDec = (Button) v.findViewById(R.id.btnMatchDecrement);
        Button btnMatchInc = (Button) v.findViewById(R.id.btnMatchIncrement);

        spAlliance = (Spinner) v.findViewById(R.id.spAlliance);

        etTeam.setText(Match.getMatch().getTeam());
        etMatch.setText(String.format(Locale.getDefault(), "%d", Match.getMatch().getMatchNumber()));

        btnMatchDec.setOnClickListener(v12 -> changeMatch(Match.getMatch().getMatchNumber() - 1));

        btnMatchInc.setOnClickListener(v1 -> changeMatch(Match.getMatch().getMatchNumber() + 1));

        spAlliance.setSelection(Match.getMatch().getAlliance().ordinal());

        spAlliance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Match.Alliance newAlliance = intToAllianceMap.get(position);
                Match.getMatch().setAlliance(newAlliance);
                MatchesDataSource mds = MatchesDataSource.getMDS(getContext());
                mds.updateAlliance(newAlliance.name(), Match.getMatch().getMatchNumber());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    /**
     * Responsible for changing the match when the increment and decrement buttons
     * are pressed
     *
     * @param match Match number to be changed to
     */
    private void changeMatch(int match){
        MatchesDataSource.getMDS(getContext()).loadMatch(match);
        spAlliance.setSelection(Match.getMatch().getAlliance().ordinal());
        etMatch.setText(String.format(Locale.getDefault(), "%d", Match.getMatch().getMatchNumber()));
        etTeam.setText(Match.getMatch().getTeam());
    }
}
