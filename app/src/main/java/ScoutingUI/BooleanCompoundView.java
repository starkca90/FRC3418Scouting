package ScoutingUI;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.roboriotteam3418.frc3418scouting.R;

/**
 * Created by cstark on 1/10/2017.
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
