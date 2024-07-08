package org.example.quizwebapp.Model;// Question.java
import java.util.List;

public class Question {
    private String text;
    private List<String> answers;

    public Question(String text, List<String> answers) {
        this.text = text;
        this.answers = answers;
    }

    // getters and setters
}
