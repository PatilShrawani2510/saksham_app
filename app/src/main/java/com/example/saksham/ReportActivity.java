package com.example.saksham;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.FileOutputStream;
import java.io.IOException;

public class ReportActivity extends AppCompatActivity {

    EditText etDate, etChildName, etTherapistName, etMood, etParticipation,
            etEnergy, etOtherTeachings, etDifficulties, etRecommendations,
            etTherapistNotes, etOverallProgress;

    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Link EditTexts
        etDate = findViewById(R.id.etDate);
        etChildName = findViewById(R.id.etChildName);
        etTherapistName = findViewById(R.id.etTherapistName);
        etMood = findViewById(R.id.etMood);
        etParticipation = findViewById(R.id.etParticipation);
        etEnergy = findViewById(R.id.etEnergy);
        etOtherTeachings = findViewById(R.id.etOtherTeachings);
        etDifficulties = findViewById(R.id.etDifficulties);
        etRecommendations = findViewById(R.id.etRecommendations);
        etTherapistNotes = findViewById(R.id.etTherapistNotes);
        etOverallProgress = findViewById(R.id.etOverallProgress);

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReport();
            }
        });
    }

    private void saveReport() {
        String data = "Date: " + etDate.getText().toString() + "\n"
                + "Name: " + etChildName.getText().toString() + "\n"
                + "Therapist: " + etTherapistName.getText().toString() + "\n"
                + "Mood: " + etMood.getText().toString() + "\n"
                + "Participation: " + etParticipation.getText().toString() + "\n"
                + "Energy: " + etEnergy.getText().toString() + "\n"
                + "Other Teachings: " + etOtherTeachings.getText().toString() + "\n"
                + "Difficulties: " + etDifficulties.getText().toString() + "\n"
                + "Recommendations: " + etRecommendations.getText().toString() + "\n"
                + "Therapist Notes: " + etTherapistNotes.getText().toString() + "\n"
                + "Overall Progress: " + etOverallProgress.getText().toString() + "\n\n";

        try {
            FileOutputStream fos = openFileOutput("child_reports.txt", MODE_APPEND);
            fos.write(data.getBytes());
            fos.close();
            Toast.makeText(this, "Report Submitted!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving report!", Toast.LENGTH_SHORT).show();
        }
    }
}
