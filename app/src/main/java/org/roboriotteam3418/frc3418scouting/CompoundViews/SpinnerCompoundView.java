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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.roboriotteam3418.frc3418scouting.R;

import java.util.Arrays;
import java.util.List;

/**
 * This class is responsible for controlling the functions of a Spinner view.
 * Responds to item selection and updating it's values as necessary
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class SpinnerCompoundView extends RelativeLayout {

    private TextView tvTitle;
    private Spinner spValue;

    public SpinnerCompoundView(Context context) {
        this(context, null);
    }

    public SpinnerCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCompoundView, 0, 0);

        String strTitle = a.getString(R.styleable.CustomCompoundView_strTitle);
        String strOptions = a.getString(R.styleable.CustomCompoundView_strOptions);

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.spinner_compound, this, true);

        RelativeLayout layout = (RelativeLayout) getChildAt(0);

        tvTitle = (TextView) layout.getChildAt(0);
        spValue = (Spinner) layout.getChildAt(1);

        tvTitle.setSelected(true);

        if(strTitle != null)
            setTitle(strTitle);
        if (strOptions != null) {
            List<String> lOptions = Arrays.asList(strOptions.split("\\s*,\\s*"));
            setOptions(lOptions, context);
        }
    }

    /**
     * Responsible for parsing a list of options to be added to the spinner
     *
     * @param options List of items to be added to the spinner as available options
     * @param context Context of parent
     */
    public void setOptions(List options, Context context) {

        String[] aOptions = new String[options.size()];
        aOptions = (String[]) options.toArray(aOptions);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, aOptions);

        spValue.setAdapter(adapter);
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        spValue.setOnItemSelectedListener(listener);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setValue(int value) {
        spValue.setSelection(value);
    }

}
