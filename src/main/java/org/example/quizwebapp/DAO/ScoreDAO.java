package org.example.quizwebapp.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class ScoreDAO {

    private int quizId;
    private String userName;

    private ArrayList<String[]> ls;
    public int submitQuiz(int quizId, String userName, ArrayList<String[]> ls) throws SQLException, ClassNotFoundException {
        this.quizId = quizId;
        this.userName = userName;
        this.ls = ls;
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection conn = connectionPool.getConnection();

        int totalScore = calculateScore();


        PreparedStatement stmt = null;
        ResultSet rs = null;

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
        return totalScore;
    }

    private int calculateScore() throws SQLException, ClassNotFoundException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection conn = connectionPool.getConnection();

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {


            String questionsQuery = "SELECT q.question_id, q.quiz_id, a.answer_id, a.answer_type FROM questions q JOIN answers a ON q.question_id = a.question_id WHERE q.quiz_id = ?";
            stmt = conn.prepareStatement(questionsQuery);
            stmt.setInt(1, quizId);
            rs = stmt.executeQuery();

            int cur_question_id = 0;

            boolean allCorrect = true;
            int totalScore = 0;
            int answerCount = 0;
            int index=-1;

            while (rs.next()) {
                index++;
                int questionId = rs.getInt("question_id");
                int answerId = rs.getInt("answer_id");
                String answerType = rs.getString("answer_type");

                if (questionId != cur_question_id) {
                    if (allCorrect) totalScore += answerCount;
                    answerCount = 0;
                    allCorrect = true;
                }
                cur_question_id = questionId;


                String[] selectedAnswers = ls.get(index);


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
            if (allCorrect) totalScore += answerCount;


            return totalScore;
        } finally {

        }
    }
}
