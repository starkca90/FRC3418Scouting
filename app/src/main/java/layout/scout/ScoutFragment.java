package layout.scout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.roboriotteam3418.frc3418scouting.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class ScoutFragment extends Fragment {

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

        spAlliance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    public enum alliance {BLUE, RED, ERROR}
}
