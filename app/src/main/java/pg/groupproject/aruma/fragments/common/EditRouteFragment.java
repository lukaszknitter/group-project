package pg.groupproject.aruma.fragments.common;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import pg.groupproject.aruma.R;
import pg.groupproject.aruma.feature.route.Route;
import pg.groupproject.aruma.feature.route.RouteService;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditRouteFragment extends Fragment {
	private EditText nameEditText;
	private EditText hoursEditText;
	private EditText minutesEditText;
	private EditText secondsEditText;
	private EditText distanceEditText;
	private RouteService routeService;

	public EditRouteFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		int routeId = getArguments().getInt("routeId");
		final Context context = getContext();
		routeService = new RouteService(context);
		final Route route = routeService.get(routeId);
		final View view = inflater.inflate(R.layout.fragment_edit_route, container, false);
		initializeInputsFromRoute(view, route);
		final Button cancelButton = view.findViewById(R.id.edit_route_cancel_button);
		cancelButton.setOnClickListener(v -> goBackToHistoryList());
		final Button saveButton = view.findViewById(R.id.edit_route_save_button);
		saveButton.setOnClickListener(v -> {
			updateRouteFromInputs(route);
		});
		return view;
	}

	private void updateRouteFromInputs(Route route) {

		final String secondsString = secondsEditText.getText().toString();
		final String minutesString = minutesEditText.getText().toString();
		final String hoursString = hoursEditText.getText().toString();
		final String distanceString = distanceEditText.getText().toString();
		final String nameString = nameEditText.getText().toString();

		if (secondsString.isEmpty()
				|| minutesString.isEmpty()
				|| hoursString.isEmpty()
				|| distanceString.isEmpty()
				|| nameString.isEmpty()) {
			Toast.makeText(getActivity(),
					getContext().getResources().getString(R.string.edit_route_time_error_message),
					Toast.LENGTH_LONG).show();
		} else {
			final long minutes = Long.parseLong(minutesString);
			final long seconds = Long.parseLong(secondsString);
			if (minutes < 60 && seconds < 60) {

				long totalSeconds = Long.parseLong(hoursString) * 3600 +
						minutes * 60 +
						seconds;
				route.setName(nameString);
				route.setDistance(Double.parseDouble(distanceString));
				route.setTotalSeconds(totalSeconds);
				routeService.update(route);
				goBackToHistoryList();
			} else {
				Toast.makeText(getActivity(),
						getContext().getResources().getString(R.string.edit_route_time_error_message),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private void goBackToHistoryList() {
		getActivity().getSupportFragmentManager().popBackStack();
	}

	private void initializeInputsFromRoute(View view, Route route) {
		nameEditText = view.findViewById(R.id.edit_route_name_value);
		nameEditText.setText(route.getName());
		final long totalSeconds = (long) route.getTotalSeconds();
		long hours = totalSeconds / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long seconds = totalSeconds % 60;
		hoursEditText = view.findViewById(R.id.edit_route_time_hours_value);
		hoursEditText.setText(String.format("%d", hours));
		minutesEditText = view.findViewById(R.id.edit_route_time_minutes_value);
		minutesEditText.setText(String.format("%d", minutes));
		secondsEditText = view.findViewById(R.id.edit_route_time_seconds_value);
		secondsEditText.setText(String.format("%d", seconds));
		distanceEditText = view.findViewById(R.id.edit_route_distance_value);
		distanceEditText.setText(String.format("%.2f", route.getDistance()));
	}
}
