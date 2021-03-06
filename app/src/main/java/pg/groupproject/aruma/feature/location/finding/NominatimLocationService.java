package pg.groupproject.aruma.feature.location.finding;

import android.util.Log;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

class NominatimLocationService {

    private static final String NOMINATIM_API_ENDPOINT = "https://nominatim.openstreetmap.org/search?q=";
    private static final int ADDRESSES_LIMIT_QUERY = 10;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient okHttpClient = new OkHttpClient();

    List<NominatimLocation> searchForLocations(String value, SimpleLocation lastKnownLocation, boolean extendedSearch) {
        final List<NominatimLocation> addresses = new ArrayList<>();
        try {
            final Response response = executeRequest(value, lastKnownLocation, extendedSearch);
            final ResponseBody responseBody = response.body();
            if (response.isSuccessful() && responseBody != null) {
                final NominatimLocation[] nominatimResponse = objectMapper.readValue(responseBody.string(), NominatimLocation[].class);
                final List<NominatimLocation> mostAccurateLocations = MostAccurateLocationsFilter.getMostAccurateLocations(Arrays.asList(nominatimResponse));
                addresses.addAll(mostAccurateLocations);
            } else {
                Log.w("searching poi", "Response for searching for POI did not complete successfully" + response.toString());
            }
        } catch (Exception e) {
            Log.e("searching poi", "An error occured while searching for POI using Nominatim API", e);
        }
        return addresses;
    }

    private Response executeRequest(@NonNull String value, SimpleLocation lastKnownLocation, boolean extendedSearch) throws IOException {
        // TODO jeżeli mamy lokalizację, to adres z lokalizacji
        if (extendedSearch) {
            lastKnownLocation.setExtendedShift();
        }
        final String countryCode = Locale.getDefault().getCountry();
        final Request request = new Request.Builder()
                .url(NOMINATIM_API_ENDPOINT + URLEncoder.encode(value.trim(), "utf-8") + appendCountryCode(countryCode, extendedSearch)
                        + appendResponseType() + appendAddressDetails() + appendQueryResultsLimit() + appendViewbox(lastKnownLocation))
                .build();

        final Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    private String appendAddressDetails() {
        return "&addressdetails=1";
    }

    private String appendResponseType() {
        return "&format=json";
    }

    private String appendQueryResultsLimit() {
        return "&limit=" + ADDRESSES_LIMIT_QUERY;
    }

    private String appendCountryCode(String countryCode, boolean extendedSearch) {
        if (extendedSearch) {
            return "";
        }
        return "&countrycodes=" + countryCode;
    }

    private String appendViewbox(SimpleLocation lastKnownLocation) {
        if (lastKnownLocation.isEmpty()) {
            return "";
        }
        return "&viewbox=" + lastKnownLocation.getLonMin() + ',' + lastKnownLocation.getLatMin() + ',' + lastKnownLocation.getLonMax() + ',' + lastKnownLocation.getLatMax()
                + "&bounded=1";
    }

}
