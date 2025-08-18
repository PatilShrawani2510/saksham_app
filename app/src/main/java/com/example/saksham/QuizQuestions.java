package com.example.saksham;

public class QuizQuestions {
    private int imageResId;
    private String[] options;
    private int correctAnswerIndex;

    public QuizQuestions(int imageResId, String[] options, int correctAnswerIndex) {
        this.imageResId = imageResId;
        this.options = options;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }
}
