package pg.groupproject.aruma.fragments.cyclocomputer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import pg.groupproject.aruma.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class CyclocomputerTabCurrentFragment extends Fragment {


    public CyclocomputerTabCurrentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cyclocomputer_tab_current, container, false);
    }

}
