package pg.groupproject.aruma.fragments.cyclocomputer;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;

import lombok.var;
import pg.groupproject.aruma.R;

public class CyclocomputerService extends Service {

    String TAG = "CyclocomputerService";
    String time;
    private static Context ctx;
    private static TextView timerTextView;
    public long _seconds=0;

    public CyclocomputerService(){

    }
    Intent intent1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        ctx = this;
        Log.i(TAG, "OnStartCommand");

        intent1 = new Intent();
        intent1.setAction("update.cyclocomputer.ui");
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "ondestroy!");
    }

    private void startTimer(){
        var myTimerTask = new TimerTask(){
            @Override
            public void run() {
                int hours = (int) (_seconds / 3600);
                int minutes = (int) ((_seconds % 3600) / 60);
                int secs = (int) (_seconds % 60);
                time = String.format("%d:%02d:%02d", hours, minutes, secs);
                intent1.putExtra("CurrentTime", time);
                sendBroadcast(intent1);
                _seconds++;
            }
        };

        var timer = new Timer();
        timer.schedule(myTimerTask, 0, 1000);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
