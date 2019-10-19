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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_find_route, container, false);
        final AutoCompleteTextView startPoint = view.findViewById(R.id.find_route_start_point_value);
        final AutoCompleteTextView endPoint = view.findViewById(R.id.find_route_end_point_value);

        new TextWatcherLocation(this, startPoint, getResources().getConfiguration());
//        startPoint.addTextChangedListener(new TextWatcherLocation(this, startPoint, R.layout.fragment_find_route, getResources().getConfiguration()));
        endPoint.addTextChangedListener(new TextWatcherLocation(this, endPoint, getResources().getConfiguration()));

        startPoint.setThreshold(3);
        startPoint.showDropDown();
        endPoint.setThreshold(3);


        return view;
    }


}
