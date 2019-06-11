package e.ravo.aruma;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mapButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapButton = findViewById(R.id.mapButton);
    }

    public void startMapActivity(View view) {
        Intent mapActivityIntent = new Intent(this, MainMapViewActivity.class);
        startActivity(mapActivityIntent);
    }

}
