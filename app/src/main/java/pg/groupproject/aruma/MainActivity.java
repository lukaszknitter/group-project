package pg.groupproject.aruma;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView infoTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infoTextView = findViewById(R.id.mapInfoTextView);
    }

    public void startMapActivity(View view) {
        Intent mapActivityIntent = new Intent(this, MainMapViewActivity.class);
        startActivity(mapActivityIntent);
    }

    public void startMapActivity(MenuItem item) {
        Intent mapActivityIntent = new Intent(this, MainMapViewActivity.class);
        startActivity(mapActivityIntent);
    }
}
