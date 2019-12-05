package pg.groupproject.aruma.fragments.cyclocomputer;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Locale;

import lombok.var;
import pg.groupproject.aruma.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CyclocomputerTabCurrentFragment extends Fragment {

    private Intent cyclocomputerServiceIntent;
    private CyclocomputerService cyclocomputerService;
    private TextView timerTextView;
    private TextView distanceTextView;
    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s1 = intent.getStringExtra("CurrentTime");
            float travelledDistance = intent.getFloatExtra("TravelledDistance", 0.0f);
            Log.i("CyclocomputerTabCurrentFragment!", "Updating time in ui...");
            timerTextView.setText(s1);
            distanceTextView.setText(String.format(Locale.US, "%.2f km", (travelledDistance / 1000)));
        }
    };
    public CyclocomputerTabCurrentFragment() {
        // Required empty public constructor
    }

    private Context ctx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //restoreVariablesFromBundle(savedInstanceState);
        final var inflateView = inflater.inflate(R.layout.fragment_cyclocomputer_tab_current, container, false);
        timerTextView = inflateView.getRootView().findViewById(R.id.cyclocomputer_current_time_value);
        distanceTextView = inflateView.getRootView().findViewById(R.id.cyclocomputer_current_distance_value);
        ctx = getActivity();
        // Inflate the layout for this fragment

        cyclocomputerService = new CyclocomputerService();
        cyclocomputerServiceIntent = new Intent(ctx, cyclocomputerService.getClass());

        if (!isMyServiceRunning(cyclocomputerService.getClass())) {
            ctx.startService(cyclocomputerServiceIntent);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("update.cyclocomputer.ui");
        ctx.registerReceiver(broadcastReceiver, intentFilter);

        return inflateView;
    }

    //jakaś metoda, którą uzywali wszyscy i wszedzie
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }
}


