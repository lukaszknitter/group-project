package pg.groupproject.aruma.fragments.history;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.robinhood.spark.SparkView;

import pg.groupproject.aruma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryDetailsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap map;

    public HistoryDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View inflateView = inflater.inflate(R.layout.fragment_history_details, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.history_map_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        SparkView historyDetailsSpeedChartView = (SparkView) inflateView.findViewById(R.id.history_speed_chart);
        historyDetailsSpeedChartView.setAdapter(new HistoryDetailsSpeedChartAdapter());

        SparkView historyDetailsAltitudeChartView = (SparkView) inflateView.findViewById(R.id.history_altitude_chart);
        historyDetailsAltitudeChartView.setAdapter(new HistoryDetailsAltitudeChartAdapter());
        // Inflate the layout for this fragment
        return inflateView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
    }

}
