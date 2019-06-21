package pg.groupproject.aruma;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MyLocationHandler implements LocationListener {

    final private Handler handler;
    final private TextView distanceTextView;

    private MainMapViewActivity mapCtx;
    private GoogleMap mMap;
    private ArrayList<LatLng> locations;
    private TrainingStatus status;
    private boolean newLocationAdded;
    private float totalDistance;



    public MyLocationHandler(MainMapViewActivity ctx, GoogleMap mMap)
    {
        this.mapCtx = ctx;
        this.mMap = mMap;
        this.locations = new ArrayList<>();
        this.handler = new Handler();
        this.newLocationAdded = false;
        this.distanceTextView = ctx.findViewById(R.id.distanceTextView);
        this.totalDistance = 0;

        initializeHandler();
    }
    @Override
    public void onLocationChanged(Location location) {
        if(status == TrainingStatus.STARTED){
            locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
            this.newLocationAdded = true;
            redrawLine();
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

    public void startTraining(){
        status = TrainingStatus.STARTED;
    }

    public void pauseTraining(){
        status = TrainingStatus.PAUSED;
    }

    private void initializeHandler(){
        final int delayTime = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateDistance();
                handler.postDelayed(this, delayTime);
            }
        }, delayTime);
    }

    private void redrawLine(){
        PolylineOptions po = new PolylineOptions().addAll(locations).width(10).color(Color.BLUE);
        Polyline line = mMap.addPolyline(po);
    }

    private void updateDistance(){
        if(locations.size() > 2 && this.newLocationAdded){
            LatLng last = locations.get(locations.size()-1);
            LatLng pen = locations.get(locations.size()-2);
            float[] result = new float[1];
            Location.distanceBetween(last.latitude, last.longitude,
                                     pen.latitude, pen.longitude,result);
            totalDistance += result[0];
            this.distanceTextView.setText(totalDistance/1000+" km");
            System.out.println(result[0]);
        }
        this.newLocationAdded = false;
    }
}
