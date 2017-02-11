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

package org.roboriotteam3418.frc3418scouting.CompoundViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.roboriotteam3418.frc3418scouting.R;

/**
 * This class is responsible for controlling the functions of a Boolean view.
 * Responds to toggle button being pressed and updating it's values as necessary
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class BooleanCompoundView extends RelativeLayout {

    private TextView tvTitle;
    private ToggleButton tbValue;

    public BooleanCompoundView(Context context) {
        this(context, null);
    }

    public BooleanCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCompoundView, 0, 0);

        String strTitle = a.getString(R.styleable.CustomCompoundView_strTitle);
        boolean bolValue = a.getBoolean(R.styleable.CustomCompoundView_boolValue, false);

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.boolean_compound, this, true);

        RelativeLayout layout = (RelativeLayout) getChildAt(0);

        tvTitle = (TextView) layout.getChildAt(0);
        tbValue = (ToggleButton) layout.getChildAt(1);

        tvTitle.setSelected(true);

        setTitle(strTitle);
        setValue(bolValue);
    }

    public void setToggleListener(OnClickListener listener) {
        tbValue.setOnClickListener(listener);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setValue(boolean value) {
        tbValue.setChecked(value);
    }
}
