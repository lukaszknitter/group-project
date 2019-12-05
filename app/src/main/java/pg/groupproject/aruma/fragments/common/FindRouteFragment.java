package pg.groupproject.aruma.fragments.common;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import java.util.function.Supplier;

import lombok.NoArgsConstructor;
import pg.groupproject.aruma.MyLocationListener;
import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.location.finding.AddressAdapter;
import pg.groupproject.aruma.feature.location.finding.NominatimLocation;
import pg.groupproject.aruma.feature.location.finding.SimpleLocation;
import pg.groupproject.aruma.feature.location.finding.TextWatcherLocation;
import pg.groupproject.aruma.fragments.NavigationFragment;

@NoArgsConstructor
public class FindRouteFragment extends Fragment {

    private static final int START_LOCATION_INDEX = 0;
    private static final int END_LOCATION_INDEX = 1;
    private NominatimLocation[] locations = new NominatimLocation[2];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final SimpleLocation lastKnownLocation = getLastKnownLocationAsSimpleLocation();

        final View view = inflater.inflate(R.layout.fragment_find_route, container, false);
        final Supplier<FragmentActivity> runOnUiThread = this::getActivity;

        final Switch switchExtendedSearch = view.findViewById(R.id.switch_find_route_extend_results);
        final Switch switchAddressFromGps = view.findViewById(R.id.switch_start_point_from_gps);

        final AutoCompleteTextView startPoint = view.findViewById(R.id.find_route_start_point_value);
        final AddressAdapter startAdapter = new AddressAdapter(view.getContext(), runOnUiThread);
        final Runnable clearSelectedStartLocation = () -> locations[START_LOCATION_INDEX] = null;
        startPoint.addTextChangedListener(new TextWatcherLocation(startPoint, startAdapter, clearSelectedStartLocation, lastKnownLocation, switchExtendedSearch));
        startPoint.setOnItemClickListener((parent, view1, position, id) -> locations[START_LOCATION_INDEX] = (NominatimLocation) parent.getItemAtPosition(position));

        final AutoCompleteTextView endPoint = view.findViewById(R.id.find_route_end_point_value);
        final AddressAdapter endAdapter = new AddressAdapter(view.getContext(), runOnUiThread);
        final Runnable clearSelectedEndLocation = () -> locations[END_LOCATION_INDEX] = null;
        endPoint.addTextChangedListener(new TextWatcherLocation(endPoint, endAdapter, clearSelectedEndLocation, lastKnownLocation, switchExtendedSearch));
        endPoint.setOnItemClickListener((parent, view1, position, id) -> locations[END_LOCATION_INDEX] = (NominatimLocation) parent.getItemAtPosition(position));

        switchAddressFromGps.setOnCheckedChangeListener((c, i) -> getOnCheckedChangeListener(c, i, startPoint, view.getContext()));
        final Button navigate = view.findViewById(R.id.button_find_route_navigate);
        navigate.setOnClickListener(c -> {
            if (locationsSelected(locations)) {
                loadNavigationFragment(getNavigationFragment(locations));
            } else {
                Toast.makeText(view.getContext(), R.string.navigate_provide_all_data, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getOnCheckedChangeListener(CompoundButton component, boolean isChecked, AutoCompleteTextView startAdapter, Context context) {
        if (isChecked) {
            Location lastKnownLocation = getLastKnownLocation();
            if (lastKnownLocation != null) {
                NominatimLocation start = new NominatimLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                locations[0] = start;
                startAdapter.setEnabled(false);
                startAdapter.setText("-");
            } else {
                Toast.makeText(context, R.string.cannot_obtain_location, Toast.LENGTH_LONG).show();
                component.setChecked(false);
            }
        } else {
            startAdapter.setEnabled(true);
        }
    }

    private NavigationFragment getNavigationFragment(NominatimLocation[] locations) {
        final Bundle bundle = new Bundle();
        bundle.putDouble("start-latitude", Double.valueOf(locations[0].getLat()));
        bundle.putDouble("start-longitude", Double.valueOf(locations[0].getLon()));
        bundle.putDouble("end-latitude", Double.valueOf(locations[1].getLat()));
        bundle.putDouble("end-longitude", Double.valueOf(locations[1].getLon()));

        NavigationFragment navigationFragment = new NavigationFragment();
        navigationFragment.setArguments(bundle);
        return navigationFragment;
    }

    private void loadNavigationFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        Fragment currentFragment = getActivity().getSupportFragmentManager().getPrimaryNavigationFragment();
        transaction.setPrimaryNavigationFragment(fragment);
        if (currentFragment != null) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private boolean locationsSelected(NominatimLocation[] locations) {
        return locations[START_LOCATION_INDEX] != null && locations[END_LOCATION_INDEX] != null;
    }

    private Location getLastKnownLocation() {
        final LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final MyLocationListener myLocationListener = new MyLocationListener();
        //TODO dodać pytania o uprawnienia
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, myLocationListener);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation == null) {
            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return lastKnownLocation;
    }

    private SimpleLocation getLastKnownLocationAsSimpleLocation() {
        Location lastKnownLocation = getLastKnownLocation();
        if (lastKnownLocation == null) {
            return new SimpleLocation();
        }
        return new SimpleLocation((float) lastKnownLocation.getLongitude(), (float) lastKnownLocation.getLatitude());
    }

}
