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
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.CompoundViews.BooleanCompoundView;
import org.roboriotteam3418.frc3418scouting.DataStructures.Match;
import org.roboriotteam3418.frc3418scouting.Database.MatchesDataSource;

/**
 * This class is responsible for managing a Boolean Entry object. Contains logic for it's
 * column in the database, it's UI component and any other logic required to make sure it works
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class BooleanEntry extends Entry {

    private BooleanCompoundView bcv;
    private String name;
    private EventType type;
    private boolean value;
    private String image;

    public BooleanEntry(String name, EventType type, String value, String image) {
        super(name, type, value, image);

        this.name = name;
        this.type = type;
        this.value = value.equals("true");
        this.image = image;
    }

    @Override
    public RelativeLayout createLayout(Context context) {
        bcv = new BooleanCompoundView(context);

        bcv.setTitle(name);
        bcv.setValue(value);

        bcv.setId(View.generateViewId());

        bcv.setToggleListener(v -> {
            value = !value;
            bcv.setValue(value);

            MatchesDataSource mds = MatchesDataSource.getMDS(context);
            mds.updateMatchEntry(name, Integer.toString(value ? 1 : 0), Match.getMatch().getMatchNumber());
        });

        return bcv;
    }

    @Override
    public RelativeLayout getLayout() {
        return bcv;
    }

    @Override
    public void setValue(String value) {
        this.value = value.equals("1");

        if(bcv != null)
            bcv.setValue(this.value);
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
