package pg.groupproject.aruma.fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.MyLocationHandler;
import pg.groupproject.aruma.feature.location.LocationService;
import pg.groupproject.aruma.feature.route.Route;
import pg.groupproject.aruma.feature.route.RouteService;

public class CyclocomputerFragment extends Fragment implements OnMapReadyCallback {

	private GoogleMap mMap;
	private LocationManager locationManager;
	private MyLocationHandler locationHandler;
	private RouteService routeService;
	private LocationService locationService;

	private long currentRouteId = -1;
	private boolean isTrainingRunning = false;
	private ImageButton startTrainingButton;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View inflateView = inflater.inflate(R.layout.fragment_cyclocomputer, null);
		SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(this);
		}

		initializeButtons(inflateView);
		setRetainInstance(true);
		routeService = new RouteService(getActivity().getApplicationContext());
		locationService = new LocationService(getActivity().getApplicationContext());
		return inflateView;
	}

	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		mMap.setMyLocationEnabled(true);

		initializeLocationListener();
		setMapCamera();
	}

	private void initializeButtons(final View view) {
		startTrainingButton = view.findViewById(R.id.startTrainingButton);
		startTrainingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startTraining(view);
			}
		});
	}

	private void startTraining(View view) {
		if (!this.isTrainingRunning) {
			currentRouteId = routeService.create();
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			locationService.insert(location, currentRouteId);
			startTrainingButton.setImageResource(R.drawable.outline_pause_circle_outline_black);
			locationHandler.startTraining();
			this.isTrainingRunning = true;
		} else {
			List<Route> routes = routeService.getAll();
			Log.i(CyclocomputerFragment.class.getName(),
					"Got One: " + routeService.get(currentRouteId));
			Log.i(CyclocomputerFragment.class.getName(),
					"Got routes: ");
			routes.forEach(route -> Log.i(CyclocomputerFragment.class.getName(), route.toString()));

			List<pg.groupproject.aruma.feature.location.Location> locations = locationService.getAllByRouteId(currentRouteId);
			Log.i(CyclocomputerFragment.class.getName(),
					"Got locations: ");
			locations.forEach(location -> Log.i(CyclocomputerFragment.class.getName(), location.toString()));
			startTrainingButton.setImageResource(R.drawable.outline_play_circle_outline_black);
			locationHandler.pauseTraining();
			this.isTrainingRunning = false;
		}
	}

	private void setMapCamera() {
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		LatLng myLocation;
		if (location != null) {
			myLocation = new LatLng(location.getLatitude(), location.getLongitude());
		} else {
			myLocation = new LatLng(54.005025, 15.989035);
		}
		mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
	}

	private void initializeLocationListener() {
		locationHandler = new MyLocationHandler(this, mMap);
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

		/** W linijce niżej pierwszy parametr mówi, czy korzystamy z GPS, sieci, wifi etc. To bedzie trzeba sparametryzować,
		 żeby wybierało najlepszą możliwą udostępnioną przez użytkownika opcję*/
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationHandler);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, locationHandler);
	}
}
