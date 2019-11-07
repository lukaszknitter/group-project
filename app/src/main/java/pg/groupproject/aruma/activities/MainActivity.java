package pg.groupproject.aruma.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.permission.PermissionChecker;
import pg.groupproject.aruma.feature.place.Place;
import pg.groupproject.aruma.feature.place.PlaceService;
import pg.groupproject.aruma.fragments.MoreFragment;
import pg.groupproject.aruma.fragments.NavigationFragment;
import pg.groupproject.aruma.fragments.common.RouteDetailsFragment;
import pg.groupproject.aruma.fragments.cyclocomputer.CyclocomputerFragment;
import pg.groupproject.aruma.fragments.history.HistoryContent;
import pg.groupproject.aruma.fragments.history.HistoryFragment;
import pg.groupproject.aruma.fragments.savedPoints.SavedPointsContent;
import pg.groupproject.aruma.fragments.savedPoints.SavedPointsFragment;

public class MainActivity extends AppCompatActivity implements
		BottomNavigationView.OnNavigationItemSelectedListener,
		HistoryFragment.OnListFragmentInteractionListener,
		SavedPointsFragment.OnListFragmentInteractionListener {

	private final CyclocomputerFragment cyclocomputerFragment = new CyclocomputerFragment();
	private final NavigationFragment navigationFragment = new NavigationFragment();
	private final MoreFragment moreFragment = new MoreFragment();
	private FragmentManager supportFragmentManager;
	private PermissionChecker permissionChecker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppTheme);
		setContentView(R.layout.activity_main);
		super.onCreate(savedInstanceState);


		supportFragmentManager = getSupportFragmentManager();
		BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener(this);

		permissionChecker = new PermissionChecker(this);
		permissionChecker.checkPermissions();

		loadFragment(cyclocomputerFragment);
	}

	private void loadFragment(Fragment fragment) {
		FragmentTransaction transaction = supportFragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, fragment);
		Fragment currentFragment = supportFragmentManager.getPrimaryNavigationFragment();
		transaction.setPrimaryNavigationFragment(fragment);
		if (currentFragment != null) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		Fragment fragment;

		switch (item.getItemId()) {
			case R.id.action_navigation:
				fragment = navigationFragment;
				break;
			case R.id.action_more:
				fragment = moreFragment;
				break;
			default:
				fragment = cyclocomputerFragment;
				break;
		}
		loadFragment(fragment);
		return true;
	}


	@Override
	public void onListFragmentInteraction(HistoryContent.HistoryViewModel item) {
		Bundle bundle = new Bundle();
		bundle.putInt("routeId", item.getId());
		RouteDetailsFragment routeDetailsFragment = new RouteDetailsFragment();
		routeDetailsFragment.setArguments(bundle);
		loadFragment(routeDetailsFragment);
	}

	@Override
	public void onListFragmentInteraction(SavedPointsContent.SavedPointViewModel item) {
		DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
			switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					PlaceService placeService = new PlaceService(this);
					final Place place = placeService.get(item.getId());
					Toast.makeText(this,
							getResources().getString(R.string.leading_to_text) + place.getName(),
							Toast.LENGTH_LONG).show();

					Bundle bundle = new Bundle();
					bundle.putDouble("latitude", place.getLatitude());
					bundle.putDouble("longitude", place.getLongitude());

					NavigationFragment navigationFragment = new NavigationFragment();
					navigationFragment.setArguments(bundle);

					loadFragment(navigationFragment);
					break;
				case DialogInterface.BUTTON_NEUTRAL:
				case DialogInterface.BUTTON_NEGATIVE:
					break;
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getResources().getString(R.string.dialog_question_lead_to_place))
				.setPositiveButton(getResources().getString(R.string.dialog_answer_yes), dialogClickListener)
				.setNegativeButton(getResources().getString(R.string.dialog_answer_no), dialogClickListener)
				.show();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (!permissionChecker.permissionsGranted()) {
			Toast.makeText(this, "Uprawnienia są wymagane do uruchomienia aplikacji!", Toast.LENGTH_LONG).show();
			finish();
		}
	}
}