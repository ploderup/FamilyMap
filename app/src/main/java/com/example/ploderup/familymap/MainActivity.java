package com.example.ploderup.familymap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
// LIFE CYCLE
    /**
     * ON CREATE
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle) called");
        setContentView(R.layout.activity_main);

        // set member IDs
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mLoginButton = (Button) findViewById(R.id.login_button);

        // set listeners
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO setup listener activity
                Toast.makeText(MainActivity.this, R.string.register_successful_toast,
                        Toast.LENGTH_SHORT).show();
            }
        });
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO setup listener activity
                Toast.makeText(MainActivity.this, R.string.login_successful_toast,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * TAG:
     * The name of the activity, for logging.
     */
    private final String TAG = "MainActivity";
}
