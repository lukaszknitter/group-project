package pg.groupproject.aruma.feature.location.finding;

import android.content.Context;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import java.util.List;

import static android.os.AsyncTask.Status.PENDING;

public class TextWatcherLocation implements TextWatcher {

    private final AutoCompleteTextView textView;
    private final Context context;
    private Configuration configuration;
    private AddressAdapter adapter;
    private LocationFinding previousLocationFindingTask;

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
        if (previousLocationFindingTask != null && taskNotYetExecuted()) {
            previousLocationFindingTask.cancel(false);
        }
        final LocationFinding locationFinding = new LocationFinding(configuration, this::updateLocations);
        previousLocationFindingTask = locationFinding;
        locationFinding.execute(charSequence.toString());
    }

    private boolean taskNotYetExecuted() {
        return PENDING == previousLocationFindingTask.getStatus();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        adapter.notifyDataSetChanged();
        textView.showDropDown();
    }

    private void updateLocations(List<NominatimLocation> addresses) {
        adapter = new AddressAdapter(context, addresses);
        adapter.setNotifyOnChange(true);
        textView.setAdapter(adapter);
    }
}
