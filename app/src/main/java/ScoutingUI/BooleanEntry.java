package ScoutingUI;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.Match;
import org.roboriotteam3418.frc3418scouting.MatchesDataSource;

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

            MatchesDataSource mds = MatchesDataSource.getMDS(context);
            mds.updateMatchEntry(name, Integer.toString(value ? 1 : 0), Match.getMatch().getMatchNumber());
        });

        return bcv;
    }

    @Override
    public RelativeLayout getLayout() {
        return bcv;
    }

    @Override
    public void setValue(String value) {
        this.value = value.equals("1");

        if(bcv != null)
            bcv.setValue(this.value);
    }
}
