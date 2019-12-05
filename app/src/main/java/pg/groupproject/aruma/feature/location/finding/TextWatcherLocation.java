package pg.groupproject.aruma.feature.location.finding;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.Switch;

import static android.os.AsyncTask.Status.PENDING;
import static android.os.AsyncTask.Status.RUNNING;

public class TextWatcherLocation implements TextWatcher {

    private static final int THRESHOLD_START_SUGGESTING_LOCATIONS = 3;

    private final AddressAdapter adapter;
    private final Runnable actionClearSelectedLocation;
    private final SimpleLocation lastKnownLocation;
    private final Switch extendedSearch;
    private LocationFinding previousLocationFindingTask;

    public TextWatcherLocation(AutoCompleteTextView textViewToUpdate, AddressAdapter addressAdapter, Runnable actionClearSelectedLocation, SimpleLocation lastKnownLocation, Switch extendedSearch) {
        this.adapter = addressAdapter;
        this.actionClearSelectedLocation = actionClearSelectedLocation;
        this.lastKnownLocation = lastKnownLocation;
        this.extendedSearch = extendedSearch;
        textViewToUpdate.setAdapter(adapter);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (charSequence.length() >= THRESHOLD_START_SUGGESTING_LOCATIONS) {
            if (previousLocationFindingTask != null && taskNotYetExecuted()) {
                previousLocationFindingTask.cancel(false);
            }
            final LocationFinding locationFinding = new LocationFinding(lastKnownLocation, extendedSearch::isChecked, adapter::update);
            previousLocationFindingTask = locationFinding;
            locationFinding.execute(charSequence.toString());
            actionClearSelectedLocation.run();
        }
    }

    private boolean taskNotYetExecuted() {
        return PENDING == previousLocationFindingTask.getStatus() || RUNNING == previousLocationFindingTask.getStatus();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
