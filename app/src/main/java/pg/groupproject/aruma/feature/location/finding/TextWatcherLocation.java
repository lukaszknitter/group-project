package pg.groupproject.aruma.feature.location.finding;

import android.content.res.Configuration;
import android.location.Address;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.stream.Collectors;

public class TextWatcherLocation implements TextWatcher {

    private final AutoCompleteTextView textViewToUpdate;
    private final Configuration configuration;
    private ArrayAdapter<String> adapter;

    public TextWatcherLocation(@NonNull Fragment fragment, @NonNull AutoCompleteTextView textViewToUpdate, Configuration configuration) {
        this.textViewToUpdate = textViewToUpdate;
        this.configuration = configuration;
        this.adapter = new ArrayAdapter<>(fragment.getActivity(), android.R.layout.select_dialog_item);
        textViewToUpdate.setAdapter(adapter);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        final LocationFinding locationFinding = new LocationFinding(configuration, this::updateLocations);
        locationFinding.execute(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {
        Integer i = 132;
        byte t = i.byteValue();
    }

    private void updateLocations(List<Address> locations) {
        final List<String> locationNames = locations.stream()
                .map(a -> a.getAddressLine(0))
                .collect(Collectors.toList());
        adapter.clear();
        adapter.addAll(locationNames);
        adapter.notifyDataSetChanged();
    }
}
