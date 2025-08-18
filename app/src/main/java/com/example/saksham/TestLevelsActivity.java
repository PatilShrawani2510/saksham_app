package com.example.saksham;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TestLevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_levels);

        TextView testLevel1 = findViewById(R.id.textViewTestLevel1);
        TextView testLevel2 = findViewById(R.id.textViewTestLevel2);
        TextView testLevel3 = findViewById(R.id.textViewTestLevel3);

        // Set up the click listener for Level 1
        testLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestLevelsActivity.this , QuizActivity.class);
                startActivity(intent);
            }
        });

        // For now, we won't set up listeners for Level 2 and Level 3
        // as we are focusing on Level 1 first.
    }
}