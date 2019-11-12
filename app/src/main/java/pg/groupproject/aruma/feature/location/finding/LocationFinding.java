package pg.groupproject.aruma.feature.location.finding;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import pg.groupproject.aruma.fragments.common.FindRouteFragment;

public class LocationFinding extends AsyncTask<String, Void, List<NominatimLocation>> {
    private NominatimLocationService nominatimService = new NominatimLocationService();
    private Consumer<List<NominatimLocation>> actionOnPostExecute;
    private SimpleLocation lastKnownLocation;

    LocationFinding(SimpleLocation lastKnownLocation, Consumer<List<NominatimLocation>> actionOnPostExecute) {
        this.lastKnownLocation = lastKnownLocation;
        this.actionOnPostExecute = actionOnPostExecute;
    }

    @Override
    protected List<NominatimLocation> doInBackground(String... objects) {
        if (objects.length == 0) {
            return new ArrayList<>();
        }
        return searchForLocation(objects[0]);
    }

    @Override
    protected void onPostExecute(List<NominatimLocation> addresses) {
        actionOnPostExecute.accept(addresses);
    }

    private List<NominatimLocation> searchForLocation(String locationName) {
        try {
            // TODO jeżeli mamy lokalizację, to ją wykorzystajmy
            return nominatimService.searchForLocations(locationName, lastKnownLocation);
        } catch (Exception e) {
            Log.e(FindRouteFragment.class.toString(), "An error occured while searching for a location", e);
            return new ArrayList<>();
        }
    }
}