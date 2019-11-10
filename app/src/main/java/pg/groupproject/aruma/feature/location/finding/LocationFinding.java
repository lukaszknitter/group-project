package pg.groupproject.aruma.feature.location.finding;

import android.content.res.Configuration;
import android.location.Address;
import android.os.AsyncTask;
import android.os.LocaleList;
import android.util.Log;

import org.osmdroid.bonuspack.location.GeocoderNominatim;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import pg.groupproject.aruma.fragments.common.FindRouteFragment;

public class LocationFinding extends AsyncTask<String, Void, List<Address>> {
    public static final int MAX_SEARCH_RESULTS = 20;
    private GeocoderNominatim geocoderNominatim;
    //    OverpassAPIProvider overpassAPIProvider;
    private Configuration configuration;
    private Consumer<List<Address>> actionOnPostExecute;

    public LocationFinding(Configuration configuration, Consumer<List<Address>> actionOnPostExecute) {
        this.configuration = configuration;
        this.actionOnPostExecute = actionOnPostExecute;
//        overpassAPIProvider = new OverpassAPIProvider();
        geocoderNominatim = new GeocoderNominatim(getLocale(), getUserAgent());
    }

    @Override
    protected List<Address> doInBackground(String... objects) {
        if (objects.length == 0) {
            return new ArrayList<>();
        }
        return searchForLocation(objects[0]);
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        actionOnPostExecute.accept(addresses);
    }

    private List<Address> searchForLocation(String location) {
        try {
            // TODO jeżeli mamy lokalizację, to ją wykorzystajmy
            return geocoderNominatim.getFromLocationName(location, MAX_SEARCH_RESULTS);
        } catch (Exception e) {
            Log.e(FindRouteFragment.class.toString(), "An error occured while searching for a location", e);
            return new ArrayList<>();
        }
    }

    private String getUserAgent() {
        return System.getProperty("http.agent");
    }

    private Locale getLocale() {
        LocaleList locales = configuration.getLocales();
        return locales.isEmpty() ? null : locales.get(0);
    }
}