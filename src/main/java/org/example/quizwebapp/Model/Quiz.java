package org.example.quizwebapp.Model;// Quiz.java
import org.example.quizwebapp.Servlet.Question;

import java.util.List;

public class Quiz {
    private String title;
    private List<Question> questions;

    public Quiz(String title, List<Question> questions) {
        this.title = title;
        this.questions = questions;
    }

    // getters and setters
}
