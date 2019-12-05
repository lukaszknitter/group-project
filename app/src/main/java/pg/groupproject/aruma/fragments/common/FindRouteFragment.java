package pg.groupproject.aruma.fragments.common;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
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
import pg.groupproject.aruma.MyLocationListener;
import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.location.finding.AddressAdapter;
import pg.groupproject.aruma.feature.location.finding.NominatimLocation;
import pg.groupproject.aruma.feature.location.finding.SimpleLocation;
import pg.groupproject.aruma.feature.location.finding.TextWatcherLocation;

@NoArgsConstructor
public class FindRouteFragment extends Fragment {

    private static final int START_LOCATION_INDEX = 0;
    private static final int END_LOCATION_INDEX = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final SimpleLocation lastKnownLocation = getLastKnownLocation();

        final View view = inflater.inflate(R.layout.fragment_find_route, container, false);
        final NominatimLocation[] locations = new NominatimLocation[2];
        final Supplier<FragmentActivity> runOnUiThread = this::getActivity;

        final AutoCompleteTextView startPoint = view.findViewById(R.id.find_route_start_point_value);
        final AddressAdapter startAdapter = new AddressAdapter(view.getContext(), runOnUiThread);
        final Runnable clearSelectedStartLocation = () -> locations[START_LOCATION_INDEX] = null;
        startPoint.addTextChangedListener(new TextWatcherLocation(startPoint, startAdapter, clearSelectedStartLocation, lastKnownLocation));
        startPoint.setOnItemClickListener((parent, view1, position, id) -> locations[START_LOCATION_INDEX] = (NominatimLocation) parent.getItemAtPosition(position));

        final AutoCompleteTextView endPoint = view.findViewById(R.id.find_route_end_point_value);
        final AddressAdapter endAdapter = new AddressAdapter(view.getContext(), runOnUiThread);
        final Runnable clearSelectedEndLocation = () -> locations[END_LOCATION_INDEX] = null;
        endPoint.addTextChangedListener(new TextWatcherLocation(endPoint, endAdapter, clearSelectedEndLocation, lastKnownLocation));
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

    private SimpleLocation getLastKnownLocation() {
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final MyLocationListener myLocationListener = new MyLocationListener();
        //TODO dodać pytania o uprawnienia
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, myLocationListener);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (lastKnownLocation == null) {
            return new SimpleLocation();
        }
        return new SimpleLocation((float) lastKnownLocation.getLongitude(), (float) lastKnownLocation.getLatitude());
    }

}
