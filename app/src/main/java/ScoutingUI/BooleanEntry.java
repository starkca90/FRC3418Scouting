package ScoutingUI;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.BooleanCompoundView;

/**
 * Created by cstark on 1/14/2017.
 */

public class BooleanEntry extends Entry {

    String name;
    EventType type;
    boolean value;
    String image;

    BooleanCompoundView bcv;

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

        bcv.setToggleListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = !value;
                bcv.setValue(value);
            }
        });

        return bcv;
    }

    @Override
    public RelativeLayout getLayout() {
        return bcv;
    }
}
