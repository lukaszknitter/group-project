package pg.groupproject.aruma.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import pg.groupproject.aruma.R;


public class CyclocomputerFragment extends Fragment{
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		final View inflateView = inflater.inflate(R.layout.fragment_cyclocomputer, null);

		TabsPagerAdapter tabsPagerAdapter = new TabsPagerAdapter(this.getContext(), getChildFragmentManager());

		ViewPager viewPager = inflateView.findViewById(R.id.view_pager);
		viewPager.setAdapter(tabsPagerAdapter);

		TabLayout tabs = inflateView.findViewById(R.id.tabs);
		tabs.setupWithViewPager(viewPager);

		return inflateView;

	}

}
