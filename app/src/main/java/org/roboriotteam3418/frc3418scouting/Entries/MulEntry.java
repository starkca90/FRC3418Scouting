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

package org.roboriotteam3418.frc3418scouting.Entries;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.CompoundViews.SpinnerCompoundView;
import org.roboriotteam3418.frc3418scouting.DataStructures.Match;
import org.roboriotteam3418.frc3418scouting.Database.MatchesDataSource;

import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for managing a Spinner Entry object. Contains logic for it's
 * column in the database, it's UI component and any other logic required to make sure it works
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class MulEntry extends Entry {

    private SpinnerCompoundView scv;
    private String name;
    private EventType type;
    private int value;
    private List<String> options;
    private String image;

    public MulEntry(String name, EventType type, String value, String options, String image) {
        super(name, type, value, options, image);

        this.name = name;
        this.type = type;
        this.value = Integer.parseInt(value);
        this.options = Arrays.asList(options.split("\\s*,\\s*"));
        this.image = image;
    }

    @Override
    public RelativeLayout createLayout(Context context) {
        scv = new SpinnerCompoundView(context);

        scv.setTitle(name);
        scv.setOptions(options, context);
        scv.setValue(value);

        scv.setId(View.generateViewId());

        scv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value = position;

                updateSQL(context);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return scv;
    }

    @Override
    public RelativeLayout getLayout() {
        return scv;
    }

    @Override
    public void setValue(String value) {
        this.value = Integer.valueOf(value);

        if(scv != null)
            scv.setValue(this.value);
    }

    private void updateSQL(Context context) {
        MatchesDataSource mds = MatchesDataSource.getMDS(context);
        mds.updateMatchEntry(name, Integer.toString(value), Match.getMatch().getMatchNumber());
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
