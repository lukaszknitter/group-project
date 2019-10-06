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
import pg.groupproject.aruma.feature.place.Place;
import pg.groupproject.aruma.feature.place.PlaceService;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditPlaceFragment extends Fragment {
	private EditText nameEditText;
	private PlaceService placeService;

	public EditPlaceFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		int placeId = getArguments().getInt("placeId");
		final Context context = getContext();
		placeService = new PlaceService(context);
		final Place place = placeService.get(placeId);
		final View view = inflater.inflate(R.layout.fragment_edit_place, container, false);
		initializeInputsFromPlace(view, place);
		final Button cancelButton = view.findViewById(R.id.edit_place_cancel_button);
		cancelButton.setOnClickListener(v -> goBackToHistoryList());
		final Button saveButton = view.findViewById(R.id.edit_place_save_button);
		saveButton.setOnClickListener(v -> updatePlaceFromInputs(place));
		return view;
	}

	private void updatePlaceFromInputs(Place place) {
		final String nameString = nameEditText.getText().toString();

		if (nameString.isEmpty()) {
			Toast.makeText(getActivity(),
					getContext().getResources().getString(R.string.not_empty_error_message),
					Toast.LENGTH_LONG).show();
		} else {
			place.setName(nameString);
			placeService.update(place);
			goBackToHistoryList();
		}
	}

	private void goBackToHistoryList() {
		getActivity().getSupportFragmentManager().popBackStack();
	}

	private void initializeInputsFromPlace(View view, Place place) {
		nameEditText = view.findViewById(R.id.edit_place_name_value);
		nameEditText.setText(place.getName());
	}
}
