package pg.groupproject.aruma.feature.location.finding;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;

import static android.os.AsyncTask.Status.PENDING;
import static android.os.AsyncTask.Status.RUNNING;

public class TextWatcherLocation implements TextWatcher {

    private static final int THRESHOLD_START_SUGGESTING_LOCATIONS = 3;

    private AddressAdapter adapter;
    private LocationFinding previousLocationFindingTask;
    private Runnable actionClearSelectedLocation;
    private SimpleLocation lastKnownLocation;

    public TextWatcherLocation(AutoCompleteTextView textViewToUpdate, AddressAdapter addressAdapter, Runnable actionClearSelectedLocation, SimpleLocation lastKnownLocation) {
        this.adapter = addressAdapter;
        this.actionClearSelectedLocation = actionClearSelectedLocation;
        this.lastKnownLocation = lastKnownLocation;
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
            final LocationFinding locationFinding = new LocationFinding(lastKnownLocation, l -> adapter.update(l));
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
