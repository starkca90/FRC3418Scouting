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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.roboriotteam3418.frc3418scouting.R;

import java.util.Locale;

/**
 * This class is responsible for controlling the functions of a Integer view.
 * Responds to increment/decrement button being pressed and updating it's values as necessary
 *
 * @author Casey Stark
 * @version 1.0
 * @since 1
 */
public class IntegerCompoundView extends RelativeLayout {

    private TextView tvTitle;
    private TextView tvValue;

    private Button btnDecTen;
    private Button btnDecFive;
    private Button btnDecrement;
    private Button btnIncrement;
    private Button btnIncFive;
    private Button btnIncTen;

    public IntegerCompoundView(Context context) {
        this(context, null);
    }

    public IntegerCompoundView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCompoundView, 0, 0);

        String strTitle = a.getString(R.styleable.CustomCompoundView_strTitle);
        int intValue = a.getInt(R.styleable.CustomCompoundView_intValue, 0);

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.integer_compound, this, true);

        RelativeLayout layout = (RelativeLayout) getChildAt(0);
        LinearLayout valueLayout = (LinearLayout) layout.getChildAt(1);

        tvTitle = (TextView) layout.getChildAt(0);
        tvValue = (TextView) valueLayout.getChildAt(2);

        btnDecTen = (Button) valueLayout.getChildAt(0);
//        btnDecFive = (Button) valueLayout.getChildAt(1);
        btnDecrement = (Button) valueLayout.getChildAt(1);
        btnIncrement = (Button) valueLayout.getChildAt(3);
//        btnIncFive = (Button) valueLayout.getChildAt(5);
        btnIncTen = (Button) valueLayout.getChildAt(4);

        tvTitle.setSelected(true);

        setTitle(strTitle);
        setValue(intValue);
    }

    public void setDecrementListeners(OnClickListener tenListener, OnClickListener fiveListener, OnClickListener oneListener) {
        btnDecTen.setOnClickListener(tenListener);
//        btnDecFive.setOnClickListener(fiveListener);
        btnDecrement.setOnClickListener(oneListener);
    }

    public void setIncrementListeners(OnClickListener tenListener, OnClickListener fiveListener, OnClickListener oneListener) {
        btnIncTen.setOnClickListener(tenListener);
//        btnIncFive.setOnClickListener(fiveListener);
        btnIncrement.setOnClickListener(oneListener);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setValue(int value) {
        tvValue.setText(String.format(Locale.getDefault(), "%d", value));
    }
}
