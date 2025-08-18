package com.example.saksham;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ParentLockActivity extends AppCompatActivity {

    private static final String TAG = "SakshamParentLock";
    private EditText dailyLimitMinutesInput;
    private EditText warningMinutesInput;
    private Button saveButton;

    private final String PREFS_NAME = "ParentLockPrefs";
    private final String KEY_DAILY_LIMIT = "dailyTimeLimitMillis";
    private final String KEY_WARNING_TIME = "warningTimeMillis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_lock);

        dailyLimitMinutesInput = findViewById(R.id.dailyLimitMinutesInput);
        warningMinutesInput = findViewById(R.id.warningMinutesInput);
        saveButton = findViewById(R.id.saveButton);

        if (dailyLimitMinutesInput == null || warningMinutesInput == null || saveButton == null) {
            Log.e(TAG, "ERROR: One or more UI components not found in activity_parent_lock.xml.");
            Toast.makeText(this, "Configuration error: Missing UI components.", Toast.LENGTH_LONG).show();
            return;
        }

        // Load existing settings to pre-fill the fields
        loadSettings();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }

    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long dailyLimitMillis = prefs.getLong(KEY_DAILY_LIMIT, 60 * 60 * 1000); // Default to 1 hour
        long warningTimeMillis = prefs.getLong(KEY_WARNING_TIME, 15 * 60 * 1000); // Default to 15 minutes

        dailyLimitMinutesInput.setText(String.valueOf(dailyLimitMillis / (60 * 1000)));
        warningMinutesInput.setText(String.valueOf(warningTimeMillis / (60 * 1000)));
    }

    private void saveSettings() {
        try {
            long dailyLimitMinutes = Long.parseLong(dailyLimitMinutesInput.getText().toString());
            long warningMinutes = Long.parseLong(warningMinutesInput.getText().toString());

            // Convert minutes to milliseconds for the timer
            long dailyLimitMillis = dailyLimitMinutes * 60 * 1000;
            long warningTimeMillis = warningMinutes * 60 * 1000;

            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();

            editor.putLong(KEY_DAILY_LIMIT, dailyLimitMillis);
            editor.putLong(KEY_WARNING_TIME, warningTimeMillis);
            editor.apply();

            Log.d(TAG, "Settings saved: Daily Limit = " + dailyLimitMinutes + "min, Warning Time = " + warningMinutes + "min");
            Toast.makeText(this, "Settings saved successfully!", Toast.LENGTH_SHORT).show();

            // Navigate back to the main activity
            Intent intent = new Intent(this, MainActivity3.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "NumberFormatException during saveSettings: " + e.getMessage());
        }
    }
}