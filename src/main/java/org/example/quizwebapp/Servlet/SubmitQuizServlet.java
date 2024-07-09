package org.example.quizwebapp.servlet;

import org.example.quizwebapp.DAO.ConnectionPool;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/submitQuiz")
public class SubmitQuizServlet extends HttpServlet {

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

            // Fetch correct answers
            String correctAnswersQuery = "SELECT answer_id FROM answers WHERE question_id IN (SELECT question_id FROM questions WHERE quiz_id = ?) AND answer_type = 'C'";
            stmt = conn.prepareStatement(correctAnswersQuery);
            stmt.setInt(1, quizId);
            rs = stmt.executeQuery();

            int totalCorrectAnswers = 0;

            while (rs.next()) {
                int answerId = rs.getInt("answer_id");
                String param = "answer_" + answerId;
                if (request.getParameter(param) != null) {
                    totalCorrectAnswers++;
                }
            }

            // Update user quiz scores
            String userQuizQuery = "SELECT best_score FROM user_quiz_scores WHERE user_name = ? AND quiz_id = ?";
            stmt = conn.prepareStatement(userQuizQuery);
            stmt.setString(1, userName);
            stmt.setInt(2, quizId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int bestScore = rs.getInt("best_score");
                if (totalCorrectAnswers > bestScore) {
                    String updateScoreQuery = "UPDATE user_quiz_scores SET best_score = ?, last_score = ? WHERE user_name = ? AND quiz_id = ?";
                    stmt = conn.prepareStatement(updateScoreQuery);
                    stmt.setInt(1, totalCorrectAnswers);
                    stmt.setInt(2, totalCorrectAnswers);
                    stmt.setString(3, userName);
                    stmt.setInt(4, quizId);
                    stmt.executeUpdate();
                } else {
                    String updateScoreQuery = "UPDATE user_quiz_scores SET last_score = ? WHERE user_name = ? AND quiz_id = ?";
                    stmt = conn.prepareStatement(updateScoreQuery);
                    stmt.setInt(1, totalCorrectAnswers);
                    stmt.setString(2, userName);
                    stmt.setInt(3, quizId);
                    stmt.executeUpdate();
                }
            } else {
                String insertScoreQuery = "INSERT INTO user_quiz_scores (user_name, quiz_id, best_score, last_score) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(insertScoreQuery);
                stmt.setString(1, userName);
                stmt.setInt(2, quizId);
                stmt.setInt(3, totalCorrectAnswers);
                stmt.setInt(4, totalCorrectAnswers);
                stmt.executeUpdate();
            }

            response.sendRedirect("quizResult.jsp?quizId=" + quizId + "&score=" + totalCorrectAnswers);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing quiz submission.");
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
            if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
            if (conn != null) { ConnectionPool.releaseConnection(conn); }
        }
    }
}
