package pg.groupproject.aruma.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pg.groupproject.aruma.MyLocationListener;
import pg.groupproject.aruma.R;

public class NavigationFragment extends Fragment {

	private MapView map = null;
	private LocationManager locationManager;
	private MyLocationListener myLocationListener;
	private Location currentLocation;
	private RoadManager roadManager;
    private Polyline selectedRoutePolyline;
    private Polyline travelledRoutePolyline;
	private TextView distanceTextView;
	private TextView timeTextView;
	private ScheduledExecutorService scheduleTaskExecutor;
	private GeoPoint destPoint;
	private boolean isNavigationMode = false;
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		Context ctx = getContext();
		Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
		Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		final View inflateView = inflater.inflate(R.layout.fragment_navigation, null);
		roadManager = new OSRMRoadManager(getActivity());
		distanceTextView = inflateView.getRootView().findViewById(R.id.distanceTextView);
		timeTextView = inflateView.getRootView().findViewById(R.id.timeTextView);
		initializeLocationManager();
		initializeMap(inflateView);
		initializeRoutingFeatures(inflateView);
		initializeScheduledTask();
		initializeStartTrainingButton(inflateView);
		final Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey("latitude") && arguments.containsKey("longitude")) {
                double predefinedLatitude = arguments.getDouble("latitude");
                double predefinedLongitude = arguments.getDouble("longitude");
                leadTo(new GeoPoint(predefinedLatitude, predefinedLongitude));
            } else if (argumentsContainStartAndEndPoints(arguments)) {
                final GeoPoint start = new GeoPoint(arguments.getDouble("start-latitude"), arguments.getDouble("start-longitude"));
                final GeoPoint end = new GeoPoint(arguments.getDouble("end-latitude"), arguments.getDouble("end-longitude"));
                leadTo(start, end);
            }
		}
		return inflateView;
	}

    private boolean argumentsContainStartAndEndPoints(Bundle arguments) {
        return arguments.containsKey("start-latitude") && arguments.containsKey("end-latitude") && arguments.containsKey("start-longitude") && arguments.containsKey("end-longitude");
    }


	private void initializeScheduledTask(){

		scheduleTaskExecutor = Executors.newScheduledThreadPool(3);
		//Schedule a task to run every 5 seconds (or however long you want)
		scheduleTaskExecutor.scheduleAtFixedRate(() -> {
			try {
				if (isNavigationMode) {
					Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (location == null)
						location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

					if (location == null)
						location = currentLocation;

					GeoPoint currentPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
					ArrayList<GeoPoint> points = new ArrayList<>();
					points.add(currentPoint);
					points.add(destPoint);

					Road road = roadManager.getRoad(points);
					if (road.mStatus == Road.STATUS_OK) {
						double roadLength = road.mLength;
						if (roadLength >= 100) {
							getActivity().runOnUiThread(() ->{
								distanceTextView.setText((int) roadLength + " km (0%)");});
						} else {
							updateUI(() -> distanceTextView.setText(String.format(Locale.US, "%.2f km (0%%)", roadLength)));
						}

                        map.getOverlays().remove(selectedRoutePolyline);

                        selectedRoutePolyline = RoadManager.buildRoadOverlay(road);
                        selectedRoutePolyline.setWidth(20);
                        map.getOverlays().add(selectedRoutePolyline);

						updateUI(() -> map.invalidate());
					}
				}
			}catch(Exception e)
			{
				System.out.println("Exception when updating route: "+e);
			}
		}, 0, 5, TimeUnit.SECONDS);
	}

	private void updateUI(Runnable run){
		getActivity().runOnUiThread(run);
	}

	private void initializeLocationManager() {
		myLocationListener = new MyLocationListener();
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		//TODO dodać pytania o uprawnienia
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, myLocationListener);
	}

	private void initializeMap(View inflateView) {
		//inflate and create the map
		map = (MapView) ((CoordinatorLayout) inflateView).getChildAt(0);
		map.setTileSource(TileSourceFactory.OpenTopo);

		map.setTilesScaledToDpi(true);
		map.getController().setZoom(15.0);

		//FIXME? removed while loop
		int retry = 0;
		while(retry < 3){
			currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if(currentLocation != null) break;
			retry++;
		}

		if (currentLocation == null)
			currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		map.getController().setCenter(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
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

	@Override
	public void onResume() {
		super.onResume();
		//this will refresh the osmdroid configuration on resuming.
		Context ctx = getContext();
		Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
		map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
	}

	@Override
	public void onPause() {
		super.onPause();
		//this will refresh the osmdroid configuration on resuming.
		Context ctx = getContext();
		Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
		map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
	}

	private void initializeRoutingFeatures(View inflateView) {
        travelledRoutePolyline = new Polyline();
		FloatingActionButton cancelRoutingButton = inflateView.findViewById(R.id.cancelRoutingButton);
		cancelRoutingButton.setOnClickListener(view -> cancelRouting());

		MapEventsReceiver mReceiver = new MapEventsReceiver() {
			@Override
			public boolean singleTapConfirmedHelper(GeoPoint p) {
				return false;
			}

			@Override
			public boolean longPressHelper(GeoPoint p) {
				Dialog dialog = new Dialog(getContext());
				dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setContentView(R.layout.navigate_dialog);

				TextView routePlaceName = dialog.findViewById(R.id.route_place_name);
				routePlaceName.setText("Latitiude: " + p.getLatitude() + "\nLongitude: " + p.getLongitude());

				destPoint = new GeoPoint(p.getLatitude(), p.getLongitude());
				initializeGoToButton(dialog.findViewById(R.id.go_to_arrow), destPoint, dialog);

				dialog.show();

				return false;
			}
		};

		map.getOverlays().add(new MapEventsOverlay(mReceiver));
	}

	private void cancelRouting() {
		Toast.makeText(getActivity(), "Canceled routing", Toast.LENGTH_SHORT).show();

		isNavigationMode = false;
        map.getOverlays().remove(selectedRoutePolyline);
		distanceTextView.setText("0 km (0 %)");
		timeTextView.setText("00:00");
	}

	private void initializeGoToButton(ImageView imageView, GeoPoint destinationPoint, Dialog dialog) {
		imageView.setOnClickListener(view -> {
            if (selectedRoutePolyline != null) {
                map.getOverlays().remove(selectedRoutePolyline);
			}

			leadTo(destinationPoint);

			dialog.hide();
		});
	}

	private void leadTo(GeoPoint destinationPoint) {
        leadTo(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()), destinationPoint);
    }

    private void leadTo(GeoPoint startPoint, GeoPoint destinationPoint) {
        ArrayList<GeoPoint> points = new ArrayList<>();
        points.add(startPoint);
        points.add(destinationPoint);

        new UpdateRoadTask().execute(points);
    }

	private void initializeStartTrainingButton(View inflateView) {
		FloatingActionButton trainingButton = inflateView.findViewById(R.id.trainingButton);

		//TODO handle this button click
		trainingButton.setOnClickListener(view ->
				Toast.makeText(getActivity(), "Start training clicked!", Toast.LENGTH_SHORT).show());
	}

	private String getDurationStringFromSeconds(double seconds) {
		int hours = (int) seconds / 3600;
		int remainder = (int) seconds - hours * 3600;
		int minutes = remainder / 60;
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes);
	}

	@SuppressLint("StaticFieldLeak")
	private class UpdateRoadTask extends AsyncTask<Object, Void, Road> {

		protected Road doInBackground(Object... params) {
			@SuppressWarnings("unchecked")
			ArrayList<GeoPoint> points = (ArrayList<GeoPoint>) params[0];

			return roadManager.getRoad(points);
		}

		@Override
		protected void onPostExecute(Road road) {
			if (road.mStatus != Road.STATUS_OK) {
				String statusMessage = road.mStatus == Road.STATUS_INVALID
						? "road has not been built yet"
						: "technical issue, no answer from the service provider";
				Toast.makeText(getActivity(), "Error while loading the road (" + statusMessage + ")", Toast.LENGTH_SHORT).show();
			} else {
				double roadLength = road.mLength;
				if (roadLength >= 100) {
					distanceTextView.setText((int) roadLength + " km (0%)");
				} else {
					distanceTextView.setText(String.format(Locale.US, "%.2f km (0%%)", roadLength));
				}

				timeTextView.setText(getDurationStringFromSeconds(road.mDuration));
                selectedRoutePolyline = RoadManager.buildRoadOverlay(road);
                selectedRoutePolyline.setWidth(20);

                map.getOverlays().add(selectedRoutePolyline);
				map.invalidate();
				isNavigationMode = true;
			}
		}
	}
}


