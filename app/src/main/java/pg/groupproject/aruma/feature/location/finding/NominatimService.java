package pg.groupproject.aruma.feature.location.finding;

import android.location.Address;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NominatimService {

    private ObjectMapper objectMapper = new ObjectMapper();
    private OkHttpClient okHttpClient = new OkHttpClient();

    public List<Address> searchForPOIs(String value) {
        List<Address> addresses = new ArrayList<>();
        try {
            final Response response = executeRequest(value);
            if (response.isSuccessful()) {
                objectMapper = new ObjectMapper();
                NominatimResponse[] nominatimResponses = objectMapper.readValue(response.body().string(), NominatimResponse[].class);
                for (NominatimResponse nominatimResponse : nominatimResponses) {
                    Address address = new Address(Locale.getDefault());
                    address.setLatitude(nominatimResponse.getLat() == null ? null : Double.parseDouble(nominatimResponse.getLat()));
                    address.setLongitude(nominatimResponse.getLon() == null ? null : Double.parseDouble(nominatimResponse.getLon()));
                    address.setFeatureName(nominatimResponse.getDisplayName());
                    NominatimResponseAddress nominatimResponseAddress = nominatimResponse.getAddress();
                    if (nominatimResponseAddress != null) {
                        address.setCountryName(nominatimResponseAddress.getCountry());
                    }
                    addresses.add(address);
                }
            } else {
                Log.w("searching poi", "Response for searching for POI did not complete successfully" + response.toString());
            }
        } catch (Exception e) {
            Log.e("searching poi", "An error occured while searching for POI using Nominatim API", e);
        }
        return addresses;
    }

    private Response executeRequest(String value) throws IOException {
        final Request request = new Request.Builder()
                .url("https://nominatim.openstreetmap.org/search/" + URLEncoder.encode(value, "utf-8") + "?format=json&addressdetails=1&limit=5")
                .build();

        final Call call = okHttpClient.newCall(request);
        return call.execute();
    }
}
