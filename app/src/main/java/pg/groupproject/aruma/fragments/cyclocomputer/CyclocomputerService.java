package pg.groupproject.aruma.fragments.cyclocomputer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

import lombok.var;

public class CyclocomputerService extends Service {

    String TAG = "CyclocomputerService";

    Timer timer;
    String time;
    private static Context ctx;
    private static TextView timerTextView;
    public long _seconds=0;
    Intent updateUiIntent;

    //updatowanie km
    private LocationManager locationManager;
    private MyCyclocomputerListener myLocationListener;
    private double lastLatitude = 0;
    private double lastLongitude = 0;
    private float travelledDistane = 0;
    public CyclocomputerService(){

    }

    @Override
    public void onCreate(){
        super.onCreate();
        initializeLocationManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        timer = new Timer();
        ctx = this;
        Log.i(TAG, "OnStartCommand");

        updateUiIntent = new Intent();
        updateUiIntent.setAction("update.cyclocomputer.ui");
        startTimer();
        startTrackingLocation();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "ondestroy!");
    }

    private void startTimer(){
        var myTimerTask = new TimerTask(){
            @Override
            public void run() {
                int hours = (int) (_seconds / 3600);
                int minutes = (int) ((_seconds % 3600) / 60);
                int secs = (int) (_seconds % 60);
                time = String.format("%d:%02d:%02d", hours, minutes, secs);
                updateUiIntent.putExtra("CurrentTime", time);
                sendBroadcast(updateUiIntent);
                _seconds++;
            }
        };

        timer.schedule(myTimerTask, 0, 1000);
    }

    private void startTrackingLocation(){
        var myLocationTask = new TimerTask(){

            @Override
            public void run() {
                Log.i(TAG, "Updating location");
                var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(location == null)
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                float [] result = new float[1];
                if(lastLongitude == 0 && lastLatitude == 0)
                {
                    lastLatitude = location.getLatitude();
                    lastLongitude = location.getLongitude();
                } else if(isCurrentLocationDifferentThanLastOne(location)) {
                    var currentLatitude = location.getLatitude();
                    var currentLongitude = location.getLongitude();
                    Location.distanceBetween(currentLatitude, currentLongitude, lastLatitude, lastLongitude, result);

                    travelledDistane += result[0];
                    Log.i(TAG, "Travelled: " + travelledDistane);
                    updateUiIntent.putExtra("TravelledDistance", travelledDistane);
                    Log.i(TAG, "Sending broadcast...");
                    lastLatitude = location.getLatitude();
                    lastLongitude= location.getLongitude();
                }else
                    Log.i(TAG, "No update in location.");

                Log.i(TAG, "Current location: longitude = " + location.getLongitude() + ", latitude = " + location.getLatitude());
            }
        };

        timer.schedule(myLocationTask, 0, 5000);
    }

    private boolean isCurrentLocationDifferentThanLastOne(Location location){
        return (location.getLatitude() != lastLatitude || location.getLongitude() != lastLongitude);
    }

    private void initializeLocationManager() {
        myLocationListener = new MyCyclocomputerListener();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //TODO dodać pytania o uprawnienia
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, myLocationListener);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //to w sumie jest totalny wylew
    private class MyCyclocomputerListener implements LocationListener {
        private final String TAG = "MyCyclocomputerListener";
        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged");
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }

}


