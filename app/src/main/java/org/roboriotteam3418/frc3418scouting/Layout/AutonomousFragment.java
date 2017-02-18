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
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.Application.ScoutActivity;
import org.roboriotteam3418.frc3418scouting.Entries.Entry;
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
public class AutonomousFragment extends Fragment {

    public AutonomousFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList list = ((ScoutActivity) getActivity()).getAutoElements();

        View v = inflater.inflate(R.layout.fragment_autonomous, container, false);

        RelativeLayout viewContainer = (RelativeLayout) v.findViewById(R.id.autoContainer);
/*
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams relativeParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        LinearLayout layoutRow = new LinearLayout(v.getContext());
        layoutRow.setLayoutParams(layoutParams);
        layoutRow.setWeightSum(2);
        layoutRow.setOrientation(LinearLayout.HORIZONTAL);

        layoutRow.addView(((Entry)list.get(0)).createLayout(v.getContext()));
        layoutRow.addView(((Entry)list.get(1)).createLayout(v.getContext()));

        viewContainer.addView(layoutRow, layoutParams);
*/






        viewContainer.getBackground().setAlpha(30);

        // Iterate through list to autonomouse elements
        for(int i = 0; i < list.size(); i++) {
            Entry entry = (Entry) list.get(i);
            // Create the layout for the entry
            RelativeLayout layout = entry.createLayout(v.getContext());
            if ((i % 2) == 0) { // even
                if(i == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    viewContainer.addView(layout, params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.BELOW,((Entry)list.get(i - 2)).getLayout().getId());
                    viewContainer.addView(layout, params);
                }
            } else { // odd
                if(i == 1) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    viewContainer.addView(layout, params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.BELOW,((Entry)list.get(i - 2)).getLayout().getId());
                    viewContainer.addView(layout, params);
                }
            }
        }

        return viewContainer;
    }

}
