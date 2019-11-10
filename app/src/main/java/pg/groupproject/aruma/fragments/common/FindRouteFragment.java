package pg.groupproject.aruma.fragments.common;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import lombok.NoArgsConstructor;
import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.location.finding.TextWatcherLocation;

@NoArgsConstructor
public class FindRouteFragment extends Fragment {

    private static final int THRESHOLD_START_SUGGESTING_LOCATIONS = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_find_route, container, false);
        final AutoCompleteTextView startPoint = view.findViewById(R.id.find_route_start_point_value);
        startPoint.setThreshold(THRESHOLD_START_SUGGESTING_LOCATIONS);
        startPoint.addTextChangedListener(new TextWatcherLocation(startPoint, view.getContext()));

        final AutoCompleteTextView endPoint = view.findViewById(R.id.find_route_end_point_value);
        endPoint.setThreshold(THRESHOLD_START_SUGGESTING_LOCATIONS);
        endPoint.addTextChangedListener(new TextWatcherLocation(endPoint, view.getContext()));

        return view;
    }
}
