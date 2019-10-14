package pg.groupproject.aruma.fragments.common;


import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.robinhood.spark.SparkView;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.List;

import lombok.var;
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
public class RouteDetailsFragment extends Fragment {
	private Route route;
	private List<Location> locations;
	private MapView map = null;

	public RouteDetailsFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		initializeRouteAndLocations();
		//TODO if route will be null after this line route details should not be displayed

		final View inflateView = inflater.inflate(R.layout.fragment_route_details, container, false);
		initializeMap(inflateView);
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

	private void initializeMap(View inflateView) {
		//inflate and create the map
		map = inflateView.findViewById(R.id.route_details_map_map);
		map.setTileSource(TileSourceFactory.OpenTopo);

		map.setTilesScaledToDpi(true);
		map.getController().setZoom(15.0);

		map.setMultiTouchControls(true);


		ScaleBarOverlay mScaleBarOverlay = new ScaleBarOverlay(map);
		mScaleBarOverlay.setScaleBarOffset(getResources().getDisplayMetrics().widthPixels / 2, 10);
		mScaleBarOverlay.setCentred(true);
		map.getOverlays().add(mScaleBarOverlay);

		MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(map);
		myLocationNewOverlay.enableFollowLocation();
		myLocationNewOverlay.enableMyLocation();
		map.getOverlays().add(myLocationNewOverlay);
	}
}
