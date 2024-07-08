package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, String[]> parameterMap = request.getParameterMap();

        List<Question> quiz = new ArrayList<>();

        // Iterate through the parameters and create questions and answers
        for (String parameterName : parameterMap.keySet()) {
            if (parameterName.startsWith("questions_")) {
                String questionId = parameterName.split("_")[1];
                String questionText = parameterMap.get(parameterName)[0];
                Question question = new Question(questionText);

                String[] answers = parameterMap.get("answers_" + questionId);
                String[] correctAnswers = parameterMap.get("correctAnswers_" + questionId);

                if (answers != null) {
                    for (int i = 0; i < answers.length; i++) {
                        boolean isCorrect = correctAnswers != null && correctAnswers.length > i && "on".equals(correctAnswers[i]);
                        question.addAnswer(answers[i], isCorrect);
                    }
                }

                quiz.add(question);
            }
        }

        request.setAttribute("quiz", quiz);
        request.getRequestDispatcher("quiz_summary.jsp").forward(request, response);
    }
}

class Question {
    private String text;
    private List<Answer> answers;

    public Question(String text) {
        this.text = text;
        this.answers = new ArrayList<>();
    }

    public void addAnswer(String answerText, boolean isCorrect) {
        answers.add(new Answer(answerText, isCorrect));
    }

    public String getText() {
        return text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}

class Answer {
    private String text;
    private boolean isCorrect;

    public Answer(String text, boolean isCorrect) {
        this.text = text;
        this.isCorrect = isCorrect;
    }

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
