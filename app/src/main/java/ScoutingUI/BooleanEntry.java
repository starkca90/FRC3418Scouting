package ScoutingUI;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by cstark on 1/14/2017.
 */

public class BooleanEntry extends Entry {

    private BooleanCompoundView bcv;
    private String name;
    private EventType type;
    private boolean value;
    private String image;

    public BooleanEntry(String name, EventType type, String value, String image) {
        super(name, type, value, image);

        this.name = name;
        this.type = type;
        this.value = value.equals("true");
        this.image = image;
    }

    @Override
    public RelativeLayout createLayout(Context context) {
        bcv = new BooleanCompoundView(context);

        bcv.setTitle(name);
        bcv.setValue(value);

        bcv.setId(View.generateViewId());

        bcv.setToggleListener(v -> {
            value = !value;
            bcv.setValue(value);
        });

        return bcv;
    }

    @Override
    public RelativeLayout getLayout() {
        return bcv;
    }

    @Override
    public void setValue(String value) {
        this.value = value.equals("true");
        bcv.setValue(this.value);
    }
}
