package ScoutingUI;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import java.util.Arrays;
import java.util.List;

/**
 * Created by cstark on 1/14/2017.
 */

public class MulEntry extends Entry {

    SpinnerCompoundView scv;
    private String name;
    private EventType type;
    private int value;
    private List<String> options;
    private String image;

    public MulEntry(String name, EventType type, String value, String options, String image) {
        super(name, type, value, options, image);

        this.name = name;
        this.type = type;
        this.value = Integer.parseInt(value);
        this.options = Arrays.asList(options.split("\\s*,\\s*"));
        this.image = image;
    }

    @Override
    public RelativeLayout createLayout(Context context) {
        scv = new SpinnerCompoundView(context);

        scv.setTitle(name);
        scv.setOptions(options, context);
        scv.setValue(value);

        scv.setId(View.generateViewId());

        scv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                value = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return scv;
    }

    @Override
    public RelativeLayout getLayout() {
        return scv;
    }

    @Override
    public String getValue() {
        return options.get(value);
    }

    @Override
    public void setValue(String value) {
        this.value = Integer.valueOf(value);
        scv.setValue(this.value);
    }
}
