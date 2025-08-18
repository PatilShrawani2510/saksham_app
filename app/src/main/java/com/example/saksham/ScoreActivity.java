package com.example.saksham;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        TextView scoreTextView = findViewById(R.id.score_text_view);
        Button doneButton = findViewById(R.id.done_button);

        // Get the score from the Intent
        int score = getIntent().getIntExtra("SCORE", 0);
        scoreTextView.setText("Score: " + score + " out of 4");

        doneButton.setOnClickListener(v -> {
            // Navigate back to the TestLevelsActivity
            Intent intent = new Intent(ScoreActivity.this, TestLevelsActivity.class);
            startActivity(intent);
            finish(); // Close the score page
        });
    }
}