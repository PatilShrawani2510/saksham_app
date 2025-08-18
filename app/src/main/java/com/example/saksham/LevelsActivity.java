package com.example.saksham;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        TextView level1 = findViewById(R.id.textViewLevel1);
        TextView level2 = findViewById(R.id.textViewLevel2);
        TextView level3 = findViewById(R.id.textViewLevel3);

        level1.setOnClickListener(v -> {
            Intent intent = new Intent(LevelsActivity.this , VideoActivity.class);
            intent.putExtra("LEVEL_NUMBER", 1);
            startActivity(intent);
        });

        level2.setOnClickListener(v -> {
            Intent intent = new Intent(LevelsActivity.this , VideoActivity.class);
            intent.putExtra("LEVEL_NUMBER", 2);
            startActivity(intent);
        });

        level3.setOnClickListener(v -> {
            Intent intent = new Intent(LevelsActivity.this , VideoActivity.class);
            intent.putExtra("LEVEL_NUMBER", 3);
            startActivity(intent);
        });
    }
}