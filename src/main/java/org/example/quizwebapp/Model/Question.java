package org.example.quizwebapp.Servlet;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private String text;
    private List<String> answers;
    private List<Boolean> correctAnswers;

    public Question(String text) {
        this.text = text;
        this.answers = new ArrayList<>();
        this.correctAnswers = new ArrayList<>();
    }

    public void addAnswer(String answer, boolean isCorrect) {
        answers.add(answer);
        correctAnswers.add(isCorrect);
    }

    public String getText() {
        return text;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public List<Boolean> getCorrectAnswers() {
        return correctAnswers;
    }
}
