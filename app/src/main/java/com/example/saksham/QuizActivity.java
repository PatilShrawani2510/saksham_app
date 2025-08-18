package com.example.saksham;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    private ImageView quizImage;
    private ImageView correctFeedback;
    private ImageView incorrectFeedback;
    private Button optionA, optionB, optionC, optionD;
    // Quiz data
    private ArrayList<QuizQuestions> quizQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        // 1. Initialize Views
        quizImage = findViewById(R.id.quiz_image);
        correctFeedback = findViewById(R.id.correct_feedback);
        incorrectFeedback = findViewById(R.id.incorrect_feedback);
        optionA = findViewById(R.id.option_a);
        optionB = findViewById(R.id.option_b);
        optionC = findViewById(R.id.option_c);
        optionD = findViewById(R.id.option_d);
        initializeQuizData();

        // 3. Set up Click Listeners for the buttons
        optionA.setOnClickListener(v -> checkAnswer(0));
        optionB.setOnClickListener(v -> checkAnswer(1));
        optionC.setOnClickListener(v -> checkAnswer(2));
        optionD.setOnClickListener(v -> checkAnswer(3));

        // 4. Display the first question
        displayQuestion();
    }

    // Inside QuizActivity.java

    private void checkAnswer(int selectedAnswerIndex) {
        // Disable all buttons to prevent multiple clicks
        setOptionsEnabled(false);

        // Get the current question to find the correct answer
        QuizQuestions currentQuestion = quizQuestions.get(currentQuestionIndex);
        int correctAnswerIndex = currentQuestion.getCorrectAnswerIndex();

        if (selectedAnswerIndex == correctAnswerIndex) {
            // Correct answer selected
            score++;
            correctFeedback.setColorFilter(getResources().getColor(android.R.color.holo_green_dark));
            incorrectFeedback.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        } else {
            // Incorrect answer selected
            incorrectFeedback.setColorFilter(getResources().getColor(android.R.color.holo_red_dark));
            correctFeedback.setColorFilter(getResources().getColor(android.R.color.darker_gray));
        }

        // Move to the next question after a delay
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goToNextQuestion();
            }

            private void goToNextQuestion() {
                currentQuestionIndex++;
                resetFeedbackCircles(); // Reset the color of the feedback circles

                if (currentQuestionIndex < quizQuestions.size()) {
                    displayQuestion(); // Show the next question
                    setOptionsEnabled(true); // Re-enable the buttons
                } else {
                    // All questions are answered, navigate to the score page
                    Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
                    intent.putExtra("SCORE", score);
                    startActivity(intent);
                    finish(); // Finish this activity so the user can't go back
                }
            }

            private void resetFeedbackCircles() {
                correctFeedback.setColorFilter(getResources().getColor(android.R.color.darker_gray));
                incorrectFeedback.setColorFilter(getResources().getColor(android.R.color.darker_gray));
            }
        }, 1500); // 1.5 second delay
    }

    private void setOptionsEnabled(boolean enabled) {
        optionA.setEnabled(enabled);
        optionB.setEnabled(enabled);
        optionC.setEnabled(enabled);
        optionD.setEnabled(enabled);
    }

    // Inside QuizActivity.java
    private void initializeQuizData(){
        quizQuestions = new ArrayList<>();
        // Add all 6 questions here. For example:
        quizQuestions.add(new QuizQuestions(R.drawable.capitala, new String[]{"b", "d", "a", "c"}, 2));
        quizQuestions.add(new QuizQuestions(R.drawable.real_applesmall, new String[]{"Banana", "Apple", "Orange", "Grape"}, 1));
        quizQuestions.add(new QuizQuestions(R.drawable.seed, new String[]{"seed", "stone" , "black" , "banana"},0));
        quizQuestions.add(new QuizQuestions(R.drawable.appleword, new String[]{"B", "E" , "A" , "C"},2));
    }

    // Inside QuizActivity.java
    private void displayQuestion() {
        if (currentQuestionIndex < quizQuestions.size()) {
            QuizQuestions currentQuestion = quizQuestions.get(currentQuestionIndex);
            quizImage.setImageResource(currentQuestion.getImageResId());

            String[] options = currentQuestion.getOptions();
            optionA.setText(options[0]);
            optionB.setText(options[1]);
            optionC.setText(options[2]);
            optionD.setText(options[3]);
        }
    }
}