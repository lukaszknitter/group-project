package pg.groupproject.aruma.fragments;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import org.osmdroid.config.Configuration;
import org.osmdroid.library.BuildConfig;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import pg.groupproject.aruma.R;

public class NavigationFragment extends Fragment {

	private MapView map = null;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Context ctx = getContext();
		Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
		Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

		final View inflateView = inflater.inflate(R.layout.fragment_navigation, null);
		GeoPoint pointGdansk = new GeoPoint(40.741895, -73.989308);

		//inflate and create the map
		map = (MapView) ((CoordinatorLayout) inflateView).getChildAt(0);
		map.setTileSource(TileSourceFactory.MAPNIK);

		map.setTilesScaledToDpi(true);
		map.getController().setZoom(15);
		map.getController().setCenter(pointGdansk);

		map.setMultiTouchControls(true);
		return inflateView;
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
}