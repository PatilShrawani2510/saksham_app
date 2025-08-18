package com.example.saksham;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {
    FrameLayout block1 , block2 , block3, block4, block5, block6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // buttons
        block1 = findViewById(R.id.block1);
        block2 = findViewById(R.id.block2); // for board = drawing.
        block3 = findViewById(R.id.block3);
        block4 = findViewById(R.id.block4);
        block5 = findViewById(R.id.block5);
        block6 = findViewById(R.id.block6);

        // making buttons clickable
        block1.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, MainActivity3.class);
            startActivity(intent);
            Toast.makeText(this, "a", Toast.LENGTH_SHORT).show();
        });
        //Navigation for board block which is of drawing .
        block2.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            startActivity(intent);
            Toast.makeText(this, "b", Toast.LENGTH_SHORT).show();
        });
        block3.setOnClickListener(v->{
             Intent intent = new Intent(MainActivity.this , ParentLockActivity.class);
             Toast.makeText(this, "c", Toast.LENGTH_SHORT).show();
             startActivity(intent);

        });
        block4.setOnClickListener(v -> {
            Toast.makeText(this, "Reporting", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
        });
        block5.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LevelsActivity.class);
            Toast.makeText(this, "d", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
        block6.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this , TestLevelsActivity.class);
            Toast.makeText(this,"e", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }
}

// Intent : It is used to move from one screen to another or send a message inside the app or between apps.