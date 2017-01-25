package layout.scout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.roboriotteam3418.frc3418scouting.Match;
import org.roboriotteam3418.frc3418scouting.MatchesDataSource;
import org.roboriotteam3418.frc3418scouting.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScoutFragment extends Fragment {

    private static final Map<Integer, Match.Alliance> intToAllianceMap = new HashMap<Integer, Match.Alliance>();
    static {
        for(Match.Alliance type : Match.Alliance.values()) {
            intToAllianceMap.put(type.ordinal(), type);
        }
    }

    public ScoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scout, container, false);

        EditText etTeam = (EditText) v.findViewById(R.id.etTeam);
        EditText etMatch = (EditText) v.findViewById(R.id.etMatch);

        Button btnMatchDec = (Button) v.findViewById(R.id.btnMatchDecrement);
        Button btnMatchInc = (Button) v.findViewById(R.id.btnMatchIncrement);

        Spinner spAlliance = (Spinner) v.findViewById(R.id.spAlliance);

        etTeam.setText(Match.getMatch().getTeam());
        etMatch.setText(Integer.toString(Match.getMatch().getMatchNumber()));

        btnMatchDec.setOnClickListener(v12 -> {
            MatchesDataSource.getMDS(getContext()).loadMatch(Match.getMatch().getMatchNumber() - 1);
            etMatch.setText(Integer.toString(Match.getMatch().getMatchNumber()));
        });

        btnMatchInc.setOnClickListener(v1 -> {
            MatchesDataSource.getMDS(getContext()).loadMatch(Match.getMatch().getMatchNumber() + 1);
            etMatch.setText(Integer.toString(Match.getMatch().getMatchNumber()));
        });

        spAlliance.setSelection(Match.getMatch().getAlliance().ordinal());

        spAlliance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Match.getMatch().setAlliance(intToAllianceMap.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }
}
