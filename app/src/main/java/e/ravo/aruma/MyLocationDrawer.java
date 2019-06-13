package e.ravo.aruma;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MyLocationDrawer implements LocationListener {
    private Context mapCtx;
    private GoogleMap mMap;
    private ArrayList<LatLng> locations;
    public MyLocationDrawer(Context ctx, GoogleMap mMap)
    {
        this.mapCtx = ctx;
        this.mMap = mMap;
        this.locations = new ArrayList<>();
    }
    @Override
    public void onLocationChanged(Location location) {
        locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
        redrawLine();
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

    private void redrawLine(){
        System.out.println("Redrawing line for points: ");
        for(int i = 0; i< locations.size(); i++){
            LatLng latLng= locations.get(i);
            System.out.println(latLng.latitude+" , "+latLng.longitude);
        }
        PolylineOptions po = new PolylineOptions().addAll(locations).width(10).color(Color.BLUE);
        Polyline line = mMap.addPolyline(po);
    }
}
