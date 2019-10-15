package pg.groupproject.aruma.fragments.common;

import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.osmdroid.bonuspack.location.GeocoderNominatim;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pg.groupproject.aruma.R;

public class FindRouteFragment extends Fragment {


    public FindRouteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_find_route, container, false);
        final EditText startPoint = view.findViewById(R.id.find_route_start_point_value);
        final EditText endPoint = view.findViewById(R.id.find_route_end_point_value);

        endPoint.setText(new LocationSearching().execute("Orneta").toString());

        return view;
    }

    private class LocationSearching extends AsyncTask<String, Void, List<Address>> {
        private GeocoderNominatim geocoderNominatim = new GeocoderNominatim(getLocale(), getUserAgent());

        @Override
        protected List<Address> doInBackground(String... objects) {
            if (objects.length == 0) {
                return new ArrayList<>();
            }
            return searchForLocation(objects[0]);
        }

        private List<Address> searchForLocation(String location) {
            try {
                return geocoderNominatim.getFromLocationName(location, 5);
            } catch (Exception e) {
                Log.e(FindRouteFragment.class.toString(), "An error occured while searching for a location", e);
                return new ArrayList<>();
            }
        }


        private String getUserAgent() {
            return System.getProperty("http.agent");
        }

        private Locale getLocale() {
            LocaleList locales = getResources().getConfiguration().getLocales();
            return locales.isEmpty() ? null : locales.get(0);
        }
    }
}
