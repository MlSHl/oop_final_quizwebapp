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
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String quizName = request.getParameter("quizName");
        String quizDescription = request.getParameter("quizDescription");
        String[] questions = request.getParameterValues("questions");
        Map<String, String[]> answersMap = request.getParameterMap().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("answers["))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, String[]> correctMap = request.getParameterMap().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("correct["))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Question> quiz = new ArrayList<>();

        if (questions != null) {
            for (int i = 0; i < questions.length; i++) {
                String questionText = questions[i];
                Question question = new Question(questionText);

                String answersKeyPrefix = "answers[" + i + "]";
                String correctKeyPrefix = "correct[" + i + "]";

                String[] questionAnswers = answersMap.get(answersKeyPrefix);
                if (questionAnswers != null) {
                    for (int j = 0; j < questionAnswers.length; j++) {
                        String answerText = questionAnswers[j];
                        String correctKey = correctKeyPrefix + "[" + j + "]";
                        boolean isCorrect = correctMap.containsKey(correctKey) && correctMap.get(correctKey).length > 0;
                        question.addAnswer(answerText, isCorrect);
                    }
                }

                quiz.add(question);
            }
        }

        request.setAttribute("quizName", quizName);
        request.setAttribute("quizDescription", quizDescription);
        request.setAttribute("quiz", quiz);
        request.getRequestDispatcher("quiz_summary.jsp").forward(request, response);
    }
}
