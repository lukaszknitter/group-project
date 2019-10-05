package pg.groupproject.aruma.fragments.common;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.robinhood.spark.SparkView;

import java.util.List;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.location.Location;
import pg.groupproject.aruma.feature.location.LocationService;
import pg.groupproject.aruma.feature.route.Route;
import pg.groupproject.aruma.feature.route.RouteService;
import pg.groupproject.aruma.fragments.history.HistoryDetailsAltitudeChartAdapter;
import pg.groupproject.aruma.fragments.history.HistoryDetailsSpeedChartAdapter;
import pg.groupproject.aruma.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDetailsFragment extends Fragment implements OnMapReadyCallback {
	private Route route;
	private GoogleMap map;
	private List<Location> locations;

	public RouteDetailsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		initializeRouteAndLocations();
		//TODO if route will be null after this line route details should not be displayed

		final View inflateView = inflater.inflate(R.layout.fragment_route_details, container, false);

		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.route_details_map_map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(this);
		}

		if (route != null) {
			setupCharts(inflateView);
			setupRouteDetails(inflateView);
		}
		// Inflate the layout for this fragment
		return inflateView;
	}

	private void setupCharts(View inflateView) {
		SparkView speedChartView = inflateView.findViewById(R.id.route_details_speed_chart);
		final double[] locationSpeeds = locations.stream()
				.map(Location::getSpeed)
				.mapToDouble(value -> value)
				.toArray();
		speedChartView.setAdapter(new HistoryDetailsSpeedChartAdapter(locationSpeeds));

		SparkView altitudeChartView = inflateView.findViewById(R.id.route_details_altitude_chart);
		final double[] locationAltitudes = locations.stream()
				.map(Location::getAltitude)
				.mapToDouble(value -> value)
				.toArray();
		altitudeChartView.setAdapter(new HistoryDetailsAltitudeChartAdapter(locationAltitudes));
	}

	private void setupRouteDetails(View inflateView) {
		TextView timeTextView = inflateView.findViewById(R.id.route_details_time_value);
		final long totalSeconds = (long) route.getTotalSeconds();
		timeTextView.setText(Utils.formatTimeFromSeconds(totalSeconds));
		TextView distanceTextView = inflateView.findViewById(R.id.route_details_distance_value);
		final long totalDistance = (long) route.getDistance();
		distanceTextView.setText(Utils.formatDistanceFromMeters(totalDistance));
		TextView averageSpeedTextView = inflateView.findViewById(R.id.route_details_avgSpeed_value);
		averageSpeedTextView.setText(Utils.formatAverageSpeedFromMetersAndSeconds(totalDistance, totalSeconds));
	}

	private void initializeRouteAndLocations() {
		int routeId = getArguments().getInt("routeId");
		final Context context = getContext();
		final RouteService routeService = new RouteService(context);
		route = routeService.get(routeId);
		final LocationService locationService = new LocationService(context);
		locations = locationService.getAllByRouteId(routeId);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
	}

}
