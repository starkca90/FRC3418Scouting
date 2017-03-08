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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.Entries.Entry;

import java.util.ArrayList;

/**
 * Created by cstark on 2/21/2017.
 */

public class PhaseLayout {

    public static LinearLayout makeLayout(Context context, LayoutInflater inflater, ArrayList list, LinearLayout parent) {

        LinearLayout subLayout = new LinearLayout(context);
        LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams compoundLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        subLayout.setLayoutParams(subLayoutParams);
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
        subLayout.setWeightSum(2);

        boolean subAdded = false;

        // Iterate through list of elements
        for (int i = 0; i < list.size(); i++) {
            // Create the layout for the entry
            RelativeLayout compoundView = ((Entry) list.get(i)).createLayout(context);

            if (((i % 2) == 0) && (i != 0)) { // Even not first
                subLayout = new LinearLayout(context);
                subLayout.setOrientation(LinearLayout.HORIZONTAL);
                subLayout.setWeightSum(2);

                subAdded = false;

                subLayout.addView(compoundView, compoundLayoutParams);
            } else if (i == 0) { // Is first
                subAdded = false;

                subLayout.addView(compoundView, compoundLayoutParams);
            } else { // Odd
                subLayout.addView(compoundView, compoundLayoutParams);

                parent.addView(subLayout, subLayoutParams);

                subAdded = true;
            }
        }

        if (!subAdded)
            parent.addView(subLayout, subLayoutParams);

        return parent;
    }

}
