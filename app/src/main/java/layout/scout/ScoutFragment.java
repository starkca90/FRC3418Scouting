package layout.scout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        return inflater.inflate(R.layout.fragment_scout, container, false);
    }
}
