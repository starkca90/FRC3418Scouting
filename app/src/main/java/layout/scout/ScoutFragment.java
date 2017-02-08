package layout.scout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

    Spinner spAlliance;
    EditText etTeam;
    EditText etMatch;

    public ScoutFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_scout, container, false);

        etTeam = (EditText) v.findViewById(R.id.etTeam);
        etTeam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String team = etTeam.getText().toString();
                Match.getMatch().setTeam(team);
                MatchesDataSource mds = MatchesDataSource.getMDS(getContext());
                mds.updateTeam(team, Match.getMatch().getMatchNumber());
            }
        });

        etMatch = (EditText) v.findViewById(R.id.etMatch);

        Button btnMatchDec = (Button) v.findViewById(R.id.btnMatchDecrement);
        Button btnMatchInc = (Button) v.findViewById(R.id.btnMatchIncrement);

        spAlliance = (Spinner) v.findViewById(R.id.spAlliance);

        etTeam.setText(Match.getMatch().getTeam());
        etMatch.setText(Integer.toString(Match.getMatch().getMatchNumber()));

        btnMatchDec.setOnClickListener(v12 -> {
            changeMatch(Match.getMatch().getMatchNumber() - 1);
        });

        btnMatchInc.setOnClickListener(v1 -> {
            changeMatch(Match.getMatch().getMatchNumber() + 1);
        });

        spAlliance.setSelection(Match.getMatch().getAlliance().ordinal());

        spAlliance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Match.Alliance newAlliance = intToAllianceMap.get(position);
                Match.getMatch().setAlliance(newAlliance);
                MatchesDataSource mds = MatchesDataSource.getMDS(getContext());
                mds.updateAlliance(newAlliance.name(), Match.getMatch().getMatchNumber());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private void changeMatch(int match){
        MatchesDataSource.getMDS(getContext()).loadMatch(match);
        spAlliance.setSelection(Match.getMatch().getAlliance().ordinal());
        etMatch.setText(Integer.toString(Match.getMatch().getMatchNumber()));
        etTeam.setText(Match.getMatch().getTeam());
    }
}
