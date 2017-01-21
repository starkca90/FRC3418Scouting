package ScoutingUI;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by cstark on 1/14/2017.
 */

public class IntegerEntry extends Entry {

    String name;
    EventType type;
    int value;
    String image;

    IntegerCompoundView icv;

    public IntegerEntry(String name, EventType type, String value, String image) {
        super(name, type, value, image);

        this.name = name;
        this.type = type;
        this.value = Integer.parseInt(value);
        this.image = image;
    }

    @Override
    public RelativeLayout getLayout() {
        return icv;
    }

    @Override
    public String getValue() {
        return Integer.toString(value);
    }

    @Override
    public void setValue(String value) {
        this.value = Integer.parseInt(value);
        icv.setValue(this.value);
    }

    @Override
    public RelativeLayout createLayout(Context context) {
        icv = new IntegerCompoundView(context);

        icv.setTitle(name);
        icv.setValue(value);

        icv.setId(View.generateViewId());

        icv.setIncrementListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value++;
                icv.setValue(value);
            }
        });

        icv.setDecrementListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value--;
                icv.setValue(value);
            }
        });

        return icv;
    }
}
