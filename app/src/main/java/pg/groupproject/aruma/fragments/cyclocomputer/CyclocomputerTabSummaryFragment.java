package pg.groupproject.aruma.fragments.cyclocomputer;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.List;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.route.Route;
import pg.groupproject.aruma.feature.route.RouteService;
import pg.groupproject.aruma.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CyclocomputerTabSummaryFragment extends Fragment {

	public CyclocomputerTabSummaryFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_cyclocomputer_tab_summary, container, false);
		final Context context = getContext();

		final RouteService routeService = new RouteService(context);
		final List<Route> routes = routeService.getAll();

		final int totalSeconds = routes.stream()
				.map(Route::getTotalSeconds)
				.mapToInt(Double::intValue)
				.sum();

		final int totalDistance = routes.stream()
				.map(Route::getDistance)
				.mapToInt(Double::intValue)
				.sum();

		final TextView timeTextView = view.findViewById(R.id.cyclocomputer_summary_time_value);
		timeTextView.setText(Utils.formatTimeFromSeconds(totalSeconds));

		final TextView distanceTextView = view.findViewById(R.id.cyclocomputer_summary_distance_value);
		distanceTextView.setText(Utils.formatDistanceFromMeters(totalDistance));

		final TextView averageSpeedTextView = view.findViewById(R.id.cyclocomputer_summary_avgSpeed_value);
		averageSpeedTextView.setText(Utils.formatAverageSpeedFromMetersAndSeconds(totalDistance, totalSeconds));

		return view;
	}

}
