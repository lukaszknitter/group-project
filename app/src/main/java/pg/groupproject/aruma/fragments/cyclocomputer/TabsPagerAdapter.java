package pg.groupproject.aruma.fragments.cyclocomputer;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import pg.groupproject.aruma.R;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES =
            new int[] {
                    R.string.cyclocomputer_tab_current,
                    R.string.cyclocomputer_tab_last_tour,
                    R.string.cyclocomputer_tab_summary
            };
    private final Context mContext;

    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CyclocomputerTabCurrentFragment();
            case 1:
                return new CyclocomputerTabLastTourFragment();
            case 2:
                return new CyclocomputerTabSummaryFragment();
            default:
                return null;
        }
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
