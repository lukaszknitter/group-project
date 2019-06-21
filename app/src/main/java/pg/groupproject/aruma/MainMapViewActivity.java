package pg.groupproject.aruma;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainMapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private MyLocationHandler locationHandler;

    private boolean isTrainingRunning = false;
    private ImageButton starTrainingButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map_view);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        starTrainingButton = findViewById(R.id.startTrainingButton);
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

    public void startTraining(View view){
        if(!this.isTrainingRunning){
            starTrainingButton.setImageResource(R.drawable.outline_pause_circle_outline_black);
            locationHandler.startTraining();
            this.isTrainingRunning = true;
        }else{
            starTrainingButton.setImageResource(R.drawable.outline_play_circle_outline_black);
            locationHandler.pauseTraining();
            this.isTrainingRunning = false;
        }
    }

    private void setMapCamera(){
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        LatLng myLocation;
        if(location != null){
            myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }else{
            myLocation = new LatLng(54.005025, 15.989035);
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
    }

    private void initializeLocationListener(){
        locationHandler = new MyLocationHandler(this, mMap);
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        /** W linijce niżej pierwszy parametr mówi, czy korzystamy z GPS, sieci, wifi etc. To bedzie trzeba sparametryzować,
         żeby wybierało najlepszą możliwą udostępnioną przez użytkownika opcję*/
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, locationHandler);
    }
}
