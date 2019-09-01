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
import pg.groupproject.aruma.fragments.CyclocomputerFragment;
import pg.groupproject.aruma.fragments.MoreFragment;
import pg.groupproject.aruma.fragments.NavigationFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

	private final CyclocomputerFragment cyclocomputerFragment = new CyclocomputerFragment();
	private final NavigationFragment navigationFragment = new NavigationFragment();
	private final MoreFragment moreFragment = new MoreFragment();
	private FragmentManager supportFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);



		BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
		bottomNavigationView.setOnNavigationItemSelectedListener(this);

		initializeSupportFragmentManager();


	}

	private void initializeSupportFragmentManager() {
		supportFragmentManager = getSupportFragmentManager();

		supportFragmentManager
				.beginTransaction()
				.add(R.id.fragment_container, cyclocomputerFragment)
				.add(R.id.fragment_container, navigationFragment)
				.add(R.id.fragment_container, moreFragment)
				.detach(navigationFragment)
				.detach(moreFragment)
				.setPrimaryNavigationFragment(cyclocomputerFragment)
				.commitNow();
	}

	private boolean loadFragment(Fragment fragment) {
		// TODO do not destroy fragment but replace it
		// https://stackoverflow.com/questions/51512010/how-not-to-destroy-fragment-with-androidx

		final FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
		final Fragment curFrag = supportFragmentManager.getPrimaryNavigationFragment();

		if (curFrag != null) {
			fragmentTransaction.detach(curFrag);
		}

		if (fragment != null) {
			fragmentTransaction.attach(fragment);
			fragmentTransaction.setPrimaryNavigationFragment(fragment);
			fragmentTransaction.setReorderingAllowed(true);
			fragmentTransaction.commitNowAllowingStateLoss();
			return true;
		}
		return false;
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
		return loadFragment(fragment);
	}
}
