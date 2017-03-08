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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.Application.ScoutActivity;
import org.roboriotteam3418.frc3418scouting.R;

import java.util.ArrayList;

/**
 * Responsible for displaying the Autonomous view of the match. Contains logic for converting
 * the autonomous node to Compound Views that recorder can use to keep track of events
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class PostMatchFragment extends Fragment {

    public PostMatchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList list = ((ScoutActivity) getActivity()).getPostElements();

        View v = inflater.inflate(R.layout.fragment_postmatch, container, false);

        RelativeLayout viewContainer = (RelativeLayout) v.findViewById(R.id.postContainer);
        LinearLayout listParent = new LinearLayout(v.getContext());

        listParent = PhaseLayout.makeLayout(v.getContext(), inflater, list, listParent);

        LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        listParent.setOrientation(LinearLayout.VERTICAL);

        viewContainer.addView(listParent, listParams);

        return viewContainer;
    }

}
