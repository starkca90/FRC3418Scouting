package ScoutingUI;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.Match;
import org.roboriotteam3418.frc3418scouting.MatchesDataSource;

public class IntegerEntry extends Entry {

    private String name;
    private EventType type;
    private int value;
    private String image;

    private IntegerCompoundView icv;

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
            if (value > 0)
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
