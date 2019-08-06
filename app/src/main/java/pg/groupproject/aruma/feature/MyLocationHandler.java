package pg.groupproject.aruma.feature;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.fragments.CyclocomputerFragment;

public class MyLocationHandler implements LocationListener {

	final private Handler handler;
	final private TextView distanceTextView;
	final private TextView speedTextView;

	private CyclocomputerFragment mapCtx;
	private GoogleMap mMap;
	private ArrayList<Location> locations;
	private TrainingStatus status;
	private boolean newLocationAdded;
	private float totalDistance;
	private Location lastLocation = null;

	public MyLocationHandler(CyclocomputerFragment ctx, GoogleMap mMap) {
		this.mapCtx = ctx;
		this.mMap = mMap;
		this.locations = new ArrayList<>();
		this.handler = new Handler();
		this.newLocationAdded = false;
		this.distanceTextView = ctx.getView().findViewById(R.id.distanceTextView);
		this.speedTextView = ctx.getView().findViewById(R.id.speedTextView);
		this.totalDistance = 0;

		initializeHandler();
	}

	@Override
	public void onLocationChanged(Location location) {
		if (lastLocation != null) {
			updateSpeed(location);
		}
		this.lastLocation = location;
		if (status == TrainingStatus.STARTED) {
			if (locations.size() == 0 || locations.get(locations.size() - 1).getLongitude() != location.getLongitude()) {
				locations.add(location);
				this.newLocationAdded = true;
				redrawLine();
			}
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	public void startTraining() {
		status = TrainingStatus.STARTED;
	}

	public void pauseTraining() {
		status = TrainingStatus.PAUSED;
	}

	private void initializeHandler() {
		final int delayTime = 1000;
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				updateDistance();
				handler.postDelayed(this, delayTime);
			}
		}, delayTime);
	}

	private void redrawLine() {
		PolylineOptions po = new PolylineOptions().addAll(
				locations.stream()
						.map(location -> new LatLng(location.getLatitude(), location.getLongitude()))
						.collect(Collectors.toList()))
				.width(10)
				.color(Color.BLUE);
		Polyline line = mMap.addPolyline(po);
	}

	private void updateDistance() {
		if (locations.size() > 2 && this.newLocationAdded) {
			Location last = locations.get(locations.size() - 1);
			Location pen = locations.get(locations.size() - 2);
			float[] result = new float[1];
			Location.distanceBetween(last.getLatitude(), last.getLongitude(),
					pen.getLatitude(), pen.getLongitude(), result);
			totalDistance += result[0];
			final String distance = new DecimalFormat("#.##").format(totalDistance / 1000);
			this.distanceTextView.setText(String.format("%s km", distance));
		}
		this.newLocationAdded = false;
	}

	private void updateSpeed(Location currentLocation) {
		double elapsedTime = currentLocation.getTime() - lastLocation.getTime();
		double calculatedSpeed = lastLocation.distanceTo(currentLocation) / elapsedTime * 1000;
		double speedMPS = currentLocation.hasSpeed() ? currentLocation.getSpeed() : calculatedSpeed;
		double speedKMPH = 3.6 * speedMPS;
		Log.v("Speed", String.format("MPS: %f KMPH: %f calculated %f", speedMPS, speedKMPH, calculatedSpeed));
		final String speedStr = new DecimalFormat("#.##").format(speedKMPH);
		this.speedTextView.setText(String.format("%s km/h", speedStr));
	}
}
