package org.example.quizwebapp.Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quizwebapp.Servlet.Question;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] questions = request.getParameterValues("questions");
        List<Question> quiz = new ArrayList<>();

        if (questions != null) {
            for (int i = 0; i < questions.length; i++) {
                String questionText = questions[i];
                Question question = new Question(questionText);

                String[] answers = request.getParameterValues("answers[" + i + "]");
                if (answers != null) {
                    for (int j = 0; j < answers.length; j++) {
                        String answerText = answers[j];
                        String correctKey = "correct[" + i + "][" + j + "]";
                        boolean isCorrect = request.getParameter(correctKey) != null;
                        question.addAnswer(answerText, isCorrect);
                    }
                }

                quiz.add(question);
            }
        }

        request.setAttribute("quiz", quiz);
        request.getRequestDispatcher("quiz_summary.jsp").forward(request, response);
    }
}
