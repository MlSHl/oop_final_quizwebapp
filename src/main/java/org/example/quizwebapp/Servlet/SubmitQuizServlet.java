package org.example.quizwebapp.Servlet;

import org.example.quizwebapp.DAO.ConnectionPool;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quizwebapp.DAO.QuizDAO;
import org.example.quizwebapp.DAO.UserDAO;
import org.example.quizwebapp.Utils.JwtUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/submitQuiz")
public class SubmitQuizServlet extends HttpServlet {

    private final UserDAO userDao = new UserDAO();
    private final QuizDAO quizDao = new QuizDAO();

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

            int cur_question_id = 0;

            boolean allCorrect = true;
            int totalScore = 0;
            int answerCount = 0;
            while (rs.next()) {
                int questionId = rs.getInt("question_id");
                int answerId = rs.getInt("answer_id");
                String answerType = rs.getString("answer_type");

                if(questionId != cur_question_id) {
                    if(allCorrect) totalScore += answerCount;
                    answerCount=0;
                    allCorrect = true;
                }
                cur_question_id = questionId;


                String[] selectedAnswers = request.getParameterValues("question_" + questionId);


                if (selectedAnswers != null) {
                    for (int i = 0; i < selectedAnswers.length; i++) {
                        if (Integer.parseInt(selectedAnswers[i]) == answerId && answerType.equals("I")) {
                            allCorrect = false;
                            break;
                        } else if (Integer.parseInt(selectedAnswers[i]) == answerId && answerType.equals("C")) {
                            answerCount++;
                        }
                    }
                }

            }
            if(allCorrect) totalScore += answerCount;

            String userQuizQuery = "SELECT best_score FROM user_quiz_scores WHERE user_name = ? AND quiz_id = ?";
            stmt = conn.prepareStatement(userQuizQuery);
            stmt.setString(1, userName);
            stmt.setInt(2, quizId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int bestScore = rs.getInt("best_score");
                if (totalScore > bestScore) {
                    String updateScoreQuery = "UPDATE user_quiz_scores SET best_score = ?, last_score = ? WHERE user_name = ? AND quiz_id = ?";
                    stmt = conn.prepareStatement(updateScoreQuery);
                    stmt.setInt(1, totalScore);
                    stmt.setInt(2, totalScore);
                    stmt.setString(3, userName);
                    stmt.setInt(4, quizId);
                    stmt.executeUpdate();
                } else {
                    String updateScoreQuery = "UPDATE user_quiz_scores SET last_score = ? WHERE user_name = ? AND quiz_id = ?";
                    stmt = conn.prepareStatement(updateScoreQuery);
                    stmt.setInt(1, totalScore);
                    stmt.setString(2, userName);
                    stmt.setInt(3, quizId);
                    stmt.executeUpdate();
                }
            } else {
                String insertScoreQuery = "INSERT INTO user_quiz_scores (user_name, quiz_id, best_score, last_score) VALUES (?, ?, ?, ?)";
                stmt = conn.prepareStatement(insertScoreQuery);
                stmt.setString(1, userName);
                stmt.setInt(2, quizId);
                stmt.setInt(3, totalScore);
                stmt.setInt(4, totalScore);
                stmt.executeUpdate();
            }

            String sql_get_times_taken = "SELECT times_taken from quizzes WHERE quiz_id = ?";
            PreparedStatement s = conn.prepareStatement(sql_get_times_taken);
            s.setInt(1, quizId);
            ResultSet r = s.executeQuery();
            r.next();
            int times_taken = r.getInt("times_taken");


            String sql_update_times_taken =
                    "update quizzes set times_taken = ? where quiz_id = ?";

            PreparedStatement pstm = conn.prepareStatement(sql_update_times_taken);
            times_taken += 1;
            pstm.setInt(1, times_taken);
            pstm.setInt(2, quizId);
            pstm.executeUpdate();
            response.sendRedirect("quizResult.jsp?quizId=" + quizId + "&score=" + totalScore);
            addAchievements(userName, quizId, totalScore);
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

    private void addAchievements(String userName, int quizId, int totalScore) throws SQLException, ClassNotFoundException {
        int numQuizzes = userDao.getTakenQuizAmount(userName);
        if (numQuizzes == 10) {
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = connectionPool.getConnection();

            String insertAchievementSql = "INSERT INTO user_achievements (user_name, achievement_id) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertAchievementSql)) {
                statement.setString(1, userName);
                statement.setInt(2, 4);

                statement.executeUpdate();
            } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }

        int maxPoints = quizDao.getQuizMaxPoints(quizId);
        if(maxPoints == totalScore){
            ConnectionPool connectionPool = ConnectionPool.getInstance();
            Connection connection = connectionPool.getConnection();

            String insertAchievementSql = "INSERT INTO user_achievements (user_name, achievement_id) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertAchievementSql)) {
                statement.setString(1, userName);
                statement.setInt(2, 5);

                statement.executeUpdate();
            } finally {
                ConnectionPool.releaseConnection(connection);
            }
        }
    }

}
