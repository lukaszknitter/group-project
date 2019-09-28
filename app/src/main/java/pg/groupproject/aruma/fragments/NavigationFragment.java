package pg.groupproject.aruma.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

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
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.w3c.dom.Text;

import java.util.ArrayList;

import lombok.var;
import pg.groupproject.aruma.MyLocationListener;
import pg.groupproject.aruma.R;

public class NavigationFragment extends Fragment {

	private MapView map = null;
	private LocationManager locationManager;
	private MyLocationListener myLocationListener;
	private Location currentLocation;
	private RoadManager roadManager;
	private Polyline currentPolyline;
	private TextView distanceTextView;
	private TextView timeTextView;
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
		initializeRoutingFeatures();

		return inflateView;
	}

	private void initializeLocationManager(){
		myLocationListener = new MyLocationListener();
		locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		//TODO dodać pytani o uprawnienia
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,myLocationListener);
	}

	private void initializeMap(View inflateView){
		//inflate and create the map
		map = (MapView) ((CoordinatorLayout) inflateView).getChildAt(0);
		map.setTileSource(TileSourceFactory.OpenTopo);

		map.setTilesScaledToDpi(true);
		map.getController().setZoom(15);
		//TODO Jak zając się tym, gdy nic sie nie zwroci?
        int retry = 0;
        while(currentLocation == null) {
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(currentLocation == null)
            	currentLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
		map.getController().setCenter(new GeoPoint(currentLocation.getLatitude(),currentLocation.getLongitude()));
		map.setMultiTouchControls(true);

		MyLocationNewOverlay myLocationNewOverlay = new MyLocationNewOverlay(map);
		myLocationNewOverlay.enableFollowLocation();
		myLocationNewOverlay.enableMyLocation();
		map.getOverlays().add(myLocationNewOverlay);
	}

	public void onResume() {
		super.onResume();
		//this will refresh the osmdroid configuration on resuming.
		Context ctx = getContext();
		Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
		map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
	}

	public void onPause() {
		super.onPause();
		//this will refresh the osmdroid configuration on resuming.
		Context ctx = getContext();
		Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
		map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
	}

	private void initializeRoutingFeatures() {
		MapEventsReceiver mReceiver = new MapEventsReceiver() {
			@Override
			public boolean singleTapConfirmedHelper(GeoPoint p) {
				return false;
			}

			@Override
			public boolean longPressHelper(GeoPoint p) {
				Context ctx = getContext();

				Dialog dialog = new Dialog(ctx);
				dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setContentView(R.layout.navigate_dialog);

				TextView routePlaceName = (TextView) dialog.findViewById(R.id.route_place_name) ;
				routePlaceName.setText("Latitiude: "+ p.getLatitude() + "\nLongitude: "+p.getLongitude());

				initializeGoToButton((ImageView)dialog.findViewById(R.id.go_to_arrow), ctx, new GeoPoint(p.getLatitude(), p.getLongitude()), dialog);

				dialog.show();

				return false;
			}
		};

		map.getOverlays().add(new MapEventsOverlay(mReceiver));
	}

	private void initializeGoToButton(ImageView imageView, Context ctx, GeoPoint destinationPoint, Dialog dialog) {
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

			    if(currentPolyline != null){
			        map.getOverlays().remove(currentPolyline);
                }

				ArrayList<GeoPoint> points = new ArrayList<GeoPoint>();
				points.add(new GeoPoint(currentLocation.getLatitude(), currentLocation.getLongitude()));
				points.add(destinationPoint);
				Road road = roadManager.getRoad(points); //TODO dodać retry jak road.mstatus zwroci cos innego niż OK
				double roadLength = (double) Math.round(road.mLength * 100) / 100;
				distanceTextView.setText(roadLength+" km (0 %)");
				timeTextView.setText(road.mDuration+"");
				currentPolyline = RoadManager.buildRoadOverlay(road);
				currentPolyline.setWidth(20);
				map.getOverlays().add(currentPolyline);
				map.invalidate();
				dialog.hide();
			}
		});
	}
}


