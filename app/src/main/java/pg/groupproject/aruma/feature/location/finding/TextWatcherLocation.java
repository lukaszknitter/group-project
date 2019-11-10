package pg.groupproject.aruma.feature.location.finding;

import android.content.Context;
import android.content.res.Configuration;
import android.location.Address;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import java.util.List;
import java.util.Locale;

public class TextWatcherLocation implements TextWatcher {

    private static int i = 0;
    private final AutoCompleteTextView textView;
    private final Context context;
    private Configuration configuration;
    private AddressAdapter adapter;

    public TextWatcherLocation(AutoCompleteTextView textViewToUpdate, Context context, Configuration configuration) {
        this.textView = textViewToUpdate;
        this.context = context;
        this.configuration = configuration;
        this.adapter = new AddressAdapter(context);
        this.adapter.setNotifyOnChange(true);
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

    private Address getAddress() {
        Address a1 = new Address(Locale.ENGLISH);
        a1.setAddressLine(0, "Wyspy" + i);
        a1.setFeatureName("Galeria" + i);
        a1.setCountryName("Polandia" + i++);
        return a1;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        adapter.notifyDataSetChanged();
        textView.showDropDown();
    }

    private void updateLocations(List<Address> addresses) {
        adapter = new AddressAdapter(context, addresses);
        adapter.setNotifyOnChange(true);
        textView.setAdapter(adapter);
//        Address address = getAddress();
//        addresses.add(address);
    }
}
