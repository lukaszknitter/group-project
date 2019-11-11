package pg.groupproject.aruma.fragments.common;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.function.Supplier;

import lombok.NoArgsConstructor;
import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.location.finding.AddressAdapter;
import pg.groupproject.aruma.feature.location.finding.NominatimLocation;
import pg.groupproject.aruma.feature.location.finding.TextWatcherLocation;

@NoArgsConstructor
public class FindRouteFragment extends Fragment {

    private static final int THRESHOLD_START_SUGGESTING_LOCATIONS = 1;
    public static final int START_LOCATION_INDEX = 0;
    public static final int END_LOCATION_INDEX = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_find_route, container, false);
        final NominatimLocation[] locations = new NominatimLocation[2];
        final Supplier<FragmentActivity> runOnUiThread = this::getActivity;

        final AutoCompleteTextView startPoint = view.findViewById(R.id.find_route_start_point_value);
        final AddressAdapter startAdapter = new AddressAdapter(view.getContext(), runOnUiThread);
        final Runnable clearSelectedStartLocation = () -> locations[START_LOCATION_INDEX] = null;
        startPoint.setThreshold(THRESHOLD_START_SUGGESTING_LOCATIONS);
        startPoint.addTextChangedListener(new TextWatcherLocation(startPoint, startAdapter, getResources().getConfiguration(), clearSelectedStartLocation));
        startPoint.setOnItemClickListener((parent, view1, position, id) -> locations[START_LOCATION_INDEX] = (NominatimLocation) parent.getItemAtPosition(position));

        final AutoCompleteTextView endPoint = view.findViewById(R.id.find_route_end_point_value);
        final AddressAdapter endAdapter = new AddressAdapter(view.getContext(), runOnUiThread);
        final Runnable clearSelectedEndLocation = () -> locations[END_LOCATION_INDEX] = null;
        endPoint.setThreshold(THRESHOLD_START_SUGGESTING_LOCATIONS);
        endPoint.addTextChangedListener(new TextWatcherLocation(endPoint, endAdapter, getResources().getConfiguration(), clearSelectedEndLocation));
        endPoint.setOnItemClickListener((parent, view1, position, id) -> locations[END_LOCATION_INDEX] = (NominatimLocation) parent.getItemAtPosition(position));

        final Button navigate = view.findViewById(R.id.button_find_route_navigate);
        navigate.setOnClickListener(c -> {
            if (locations[START_LOCATION_INDEX] != null && locations[END_LOCATION_INDEX] != null) {
                // TODO przejście do nawigacji
                Log.i("idziemy", "i nawigujemy!");
            } else {
                Toast.makeText(view.getContext(), "Podaj start i end!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
