package ScoutingUI;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.Match;
import org.roboriotteam3418.frc3418scouting.MatchesDataSource;

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
    public void setValue(String value) {
        this.value = Integer.parseInt(value);

        if(icv != null)
            icv.setValue(this.value);
    }

    @Override
    public RelativeLayout createLayout(Context context) {
        icv = new IntegerCompoundView(context);

        icv.setTitle(name);
        icv.setValue(value);

        icv.setId(View.generateViewId());

        icv.setIncrementListener(v -> {
            value++;
            icv.setValue(value);

            updateSQL(context);
        });

        icv.setDecrementListener(v -> {
            value--;
            icv.setValue(value);

            updateSQL(context);
        });

        return icv;
    }

    private void updateSQL(Context context) {
        MatchesDataSource mds = MatchesDataSource.getMDS(context);
        mds.updateMatchEntry(name, Integer.toString(value), Match.getMatch().getMatchNumber());
    }
}
