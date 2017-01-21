package ScoutingUI;

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
 * Created by cstark on 1/10/2017.
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

    public void setOptions(List options, Context context) {


        String[] aOptions = new String[options.size()];
        aOptions = (String[]) options.toArray(aOptions);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, aOptions);

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
        spValue.setSelection(value);
    }

}
