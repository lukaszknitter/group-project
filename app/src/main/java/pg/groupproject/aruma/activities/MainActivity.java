package pg.groupproject.aruma.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.fragments.HistoryFragment;
import pg.groupproject.aruma.fragments.MoreFragment;
import pg.groupproject.aruma.fragments.NavigationFragment;
import pg.groupproject.aruma.fragments.SavedPointsFragment;
import pg.groupproject.aruma.fragments.cyclocomputer.CyclocomputerFragment;
import pg.groupproject.aruma.fragments.viewModels.HistoryContent;
import pg.groupproject.aruma.fragments.viewModels.SavedPointsContent;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, HistoryFragment.OnListFragmentInteractionListener, SavedPointsFragment.OnListFragmentInteractionListener {

	private final CyclocomputerFragment cyclocomputerFragment = new CyclocomputerFragment();
	private final NavigationFragment navigationFragment = new NavigationFragment();
	private final MoreFragment moreFragment = new MoreFragment();
	private FragmentManager supportFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		supportFragmentManager = getSupportFragmentManager();
		BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener(this);

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
		Fragment fragment = null;

		switch (item.getItemId()) {
			case R.id.action_cyclocomputer:
				fragment = cyclocomputerFragment;
				break;
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

	}

	@Override
	public void onListFragmentInteraction(SavedPointsContent.SavedPointViewModel item) {

	}
}
