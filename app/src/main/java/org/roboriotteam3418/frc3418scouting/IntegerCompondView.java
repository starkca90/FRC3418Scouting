package org.roboriotteam3418.frc3418scouting;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by cstark on 1/10/2017.
 */

public class IntegerCompondView extends RelativeLayout {

    private View mValue;

    public IntegerCompondView(Context context) {
        super(context);
    }

    public IntegerCompondView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.)
    }

    public IntegerCompondView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IntegerCompondView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
