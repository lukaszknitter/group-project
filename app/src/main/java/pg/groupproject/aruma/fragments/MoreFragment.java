package pg.groupproject.aruma.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.fragments.history.HistoryFragment;
import pg.groupproject.aruma.fragments.savedPoints.SavedPointsFragment;


public class MoreFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View inflateView = inflater.inflate(R.layout.fragment_more, null);

        RelativeLayout history = inflateView.findViewById(R.id.more_list_history);
        history.setOnClickListener(view -> loadFragment(new HistoryFragment()));

        RelativeLayout savedPoints = inflateView.findViewById(R.id.more_list_saved_points);
        savedPoints.setOnClickListener(view -> loadFragment(new SavedPointsFragment()));

        return inflateView;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }



}
