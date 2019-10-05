package pg.groupproject.aruma.fragments.cyclocomputer;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.route.Route;
import pg.groupproject.aruma.feature.route.RouteService;
import pg.groupproject.aruma.fragments.common.RouteDetailsFragment;

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

	@StringRes
	private static final int[] TAB_TITLES =
			new int[]{
					R.string.cyclocomputer_tab_current,
					R.string.cyclocomputer_tab_last_tour,
					R.string.cyclocomputer_tab_summary
			};
	private final Context mContext;

	public TabsPagerAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
		super(fm, behavior);
		mContext = context;
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
			case 0:
				return new CyclocomputerTabCurrentFragment();
			case 1: {
				final RouteService routeService = new RouteService(mContext);
				RouteDetailsFragment routeDetailsFragment = new RouteDetailsFragment();
				final Route lastFinishedRoute = routeService.getLastFinishedRoute();
				Bundle bundle = new Bundle();
				if (lastFinishedRoute != null) {
					bundle.putInt("routeId", lastFinishedRoute.getId());
				} else {
					bundle.putInt("routeId", -1);
				}
				routeDetailsFragment.setArguments(bundle);
				return routeDetailsFragment;
			}
			case 2:
				return new CyclocomputerTabSummaryFragment();
			default:
				return null;
		}
	}

	// Two methods below forces fragments to update, to always have route up to date
	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {

	}

	@Nullable
	@Override
	public CharSequence getPageTitle(int position) {
		return mContext.getResources().getString(TAB_TITLES[position]);
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 3;
	}
}
