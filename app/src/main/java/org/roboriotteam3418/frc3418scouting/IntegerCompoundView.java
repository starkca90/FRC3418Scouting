package org.roboriotteam3418.frc3418scouting;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by cstark on 1/10/2017.
 */

public class IntegerCompoundView extends RelativeLayout {

    private TextView tvTitle;
    private TextView tvValue;

    private Button btnDecrement;
    private Button btnIncrement;

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

        tvTitle = (TextView) layout.getChildAt(0);
        tvValue = (TextView) layout.getChildAt(2);

        btnDecrement = (Button) layout.getChildAt(1);
        btnIncrement = (Button) layout.getChildAt(3);

        tvTitle.setSelected(true);

        setTitle(strTitle);
        setValue(intValue);
    }

    public void setDecrementListener(OnClickListener listener) {
        btnDecrement.setOnClickListener(listener);
    }

    public void setIncrementListener(OnClickListener listener) {
        btnIncrement.setOnClickListener(listener);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setValue(int value) {
        tvValue.setText(Integer.toString(value));
    }
}
