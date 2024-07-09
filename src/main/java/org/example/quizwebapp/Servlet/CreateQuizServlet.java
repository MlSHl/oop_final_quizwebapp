package org.example.quizwebapp.Servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.DAO.ConnectionPool;
import org.example.quizwebapp.DAO.QuizDAO;
import org.example.quizwebapp.DAO.UserDAO;
import org.example.quizwebapp.Model.Quiz;
import org.example.quizwebapp.Model.User;
import org.example.quizwebapp.Model.Question;
import org.example.quizwebapp.Utils.JwtUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet("/CreateQuizServlet")
public class CreateQuizServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final UserDAO userDao = new UserDAO();
    private static final QuizDAO quizDao = new QuizDAO();

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

        List<Question> quizQuestionsList = new ArrayList<>();

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
                quizQuestionsList.add(question);
            }
            Quiz quiz = new Quiz(quizName, quizDescription, quizQuestionsList);
            String token = (String) request.getSession().getAttribute("token");
            String username = JwtUtil.extractUsername(token);
            User user = null;
            try {
                user = userDao.getUserByUsername(username);
            } catch (SQLException | UserNotFoundException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            try {
                quizDao.createQuiz(user, quiz);
                addAchievements(username);
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


        request.setAttribute("quizName", quizName);
        request.setAttribute("quizDescription", quizDescription);
        request.setAttribute("quiz", quizQuestionsList);
        request.getRequestDispatcher("quiz_summary.jsp").forward(request, response);
    }

    private void addAchievements(String username) throws SQLException, ClassNotFoundException {
        int numQuizzes = userDao.getCreatedQuizAmount(username);
        if (numQuizzes == 1 || numQuizzes == 5 || numQuizzes == 10) {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = connectionPool.getConnection();

            String insertAchievementSql = "INSERT INTO user_achievements (user_name, achievement_id) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertAchievementSql)) {
                statement.setString(1, username);
                if(numQuizzes == 1){
                    statement.setInt(2, 1);
                }else if(numQuizzes == 5){
                    statement.setInt(2, 2);
                }else{
                    statement.setInt(2, 3);
                }

                statement.executeUpdate();
            } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
 }
}