package layout.scout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.roboriotteam3418.frc3418scouting.R;
import org.roboriotteam3418.frc3418scouting.ScoutActivity;

import java.util.ArrayList;

import ScoutingUI.Entry;

public class AutonomousFragment extends Fragment {

    public AutonomousFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList list = ((ScoutActivity) getActivity()).getAutoElements();

        View v = inflater.inflate(R.layout.fragment_autonomous, container, false);

        RelativeLayout viewContainer = (RelativeLayout) v.findViewById(R.id.autoContainer);

        for(int i = 0; i < list.size(); i++) {
            Entry entry = (Entry) list.get(i);
            RelativeLayout layout = entry.createLayout(v.getContext());
            if((i % 2) == 0) {
                // even
                if(i == 0) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    viewContainer.addView(layout, params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.BELOW,((Entry)list.get(i - 2)).getLayout().getId());
                    viewContainer.addView(layout, params);
                }
            } else {
                if(i == 1) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    viewContainer.addView(layout, params);
                } else {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                    params.addRule(RelativeLayout.BELOW,((Entry)list.get(i - 2)).getLayout().getId());
                    viewContainer.addView(layout, params);
                }
            }
        }

        // Inflate the layout for this fragment
        return viewContainer;
    }

}
