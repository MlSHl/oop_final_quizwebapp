package org.example.quizwebapp.Servlet;

import org.example.quizwebapp.DAO.ConnectionPool;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quizwebapp.DAO.ScoreDAO;
import org.example.quizwebapp.DAO.UserDAO;
import org.example.quizwebapp.Utils.JwtUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/submitQuiz")
public class SubmitQuizServlet extends HttpServlet {

    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ConnectionPool pool = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int quizId = Integer.parseInt(request.getParameter("quizId"));
        String userName = request.getParameter("userName");

        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();

            String questionsQuery = "SELECT q.question_id, q.quiz_id, a.answer_id, a.answer_type FROM questions q JOIN answers a ON q.question_id = a.question_id WHERE q.quiz_id = ?";
            stmt = conn.prepareStatement(questionsQuery);
            stmt.setInt(1, quizId);
            rs = stmt.executeQuery();

            ArrayList<String[]> matrix = new ArrayList<>();

            while (rs.next()) {
                int questionId = rs.getInt("question_id");


                String[] selectedAnswers = request.getParameterValues("question_" + questionId);
                matrix.add(selectedAnswers);



            }


            ScoreDAO scoreDao = new ScoreDAO();

            int totalScore = scoreDao.submitQuiz(quizId, userName, matrix);
            response.sendRedirect("quizResult.jsp?quizId=" + quizId + "&score=" + totalScore);
            addAchievements(userName);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing quiz submission.");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
            if (conn != null) { ConnectionPool.releaseConnection(conn); }
        }
    }

    private boolean markedAll(String[] selectedAnswers) {
        for (String selectedAnswer : selectedAnswers) {
            if(!selectedAnswer.isEmpty()){
                return false;
            }
        }
        return true;
    }

    private void addAchievements(String userName) throws SQLException, ClassNotFoundException {
        int numQuizzes = userDao.getQuizAmount(userName);
        if (numQuizzes == 1) {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = connectionPool.getConnection();

            String insertAchievementSql = "INSERT INTO user_achievements (user_name, achievement_id) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertAchievementSql)) {
                statement.setString(1, userName);
                statement.setInt(2, 1);

                statement.executeUpdate();
            } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

}
