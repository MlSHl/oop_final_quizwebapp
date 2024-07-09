package org.example.quizwebapp.DAO;

import org.example.quizwebapp.Model.Quiz;
import org.example.quizwebapp.Model.User;
import org.example.quizwebapp.Model.Question;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizDAO {

    public void createQuiz(User creator, Quiz quiz) throws SQLException, ClassNotFoundException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        String insertQuizSql = "INSERT INTO quiz_db.quizzes (quiz_name, quiz_desc, creator_name) VALUES (?, ?, ?)";
        PreparedStatement quizStatement = connection.prepareStatement(insertQuizSql, Statement.RETURN_GENERATED_KEYS);
        try {
            quizStatement.setString(1, quiz.getTitle());
            quizStatement.setString(2, quiz.getDesc());
            quizStatement.setString(3, creator.getUsername());

            quizStatement.executeUpdate();
            try (ResultSet generatedKeys = quizStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int quizId = generatedKeys.getInt(1);
                    quiz.setId(quizId);
                } else {
                    throw new SQLException("Creating quiz failed, no ID obtained.");
                }
            }
        } finally {
            quizStatement.close();
        }

        String insertQuestionSQL = "INSERT INTO questions (quiz_id, question_text, points) VALUES (?, ?, 1)";
        PreparedStatement questionStatement = connection.prepareStatement(insertQuestionSQL, Statement.RETURN_GENERATED_KEYS);
        try {
            for (Question question : quiz.getQuestions()) {
                questionStatement.setInt(1, quiz.getId());
                questionStatement.setString(2, question.getText());
                questionStatement.executeUpdate();

                try (ResultSet generatedKeys = questionStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int questionId = generatedKeys.getInt(1);
                        question.setQuestionId(questionId);
                    } else {
                        throw new SQLException("Creating question failed, no ID obtained.");
                    }
                }

                String insertAnswerSQL = "INSERT INTO answers (question_id, answer_text, answer_type) VALUES (?, ?, ?)";
                PreparedStatement answerStatement = connection.prepareStatement(insertAnswerSQL);
                try {
                    List<String> answers = question.getAnswers();
                    List<Boolean> correctAnswers = question.getCorrectAnswers();
                    for (int i = 0; i < answers.size(); i++) {
                        answerStatement.setInt(1, question.getQuestionId());
                        answerStatement.setString(2, answers.get(i));
                        answerStatement.setString(3, correctAnswers.get(i) ? "C" : "I");
                        answerStatement.executeUpdate();
                    }
                } finally {
                    answerStatement.close();
                }
            }
        } finally {
            questionStatement.close();
            ConnectionPool.releaseConnection(connection);
        }
    }

    public List<Quiz> getPopularQuizzes(int numQUizzes){
        return null;
    }

    public List<Quiz> getRecentQuizzes(int numQuizzes) throws SQLException, ClassNotFoundException {
        List<Quiz> quizzes = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        int quizzesPulled = 0;

        String quizSql = "SELECT quiz_id, quiz_name, quiz_desc FROM quizzes " +
                         "ORDER BY quiz_id desc";
        String questionSql = "SELECT question_id, question_text FROM questions WHERE quiz_id = ?";
        String answerSql = "SELECT answer_text, answer_type FROM answers WHERE question_id = ?";

        try (PreparedStatement quizStatement = connection.prepareStatement(quizSql)) {
            ResultSet quizResultSet = quizStatement.executeQuery();

            while (quizResultSet.next() && quizzesPulled < numQuizzes) {
                int quizId = quizResultSet.getInt("quiz_id");
                String quizName = quizResultSet.getString("quiz_name");
                String quizDesc = quizResultSet.getString("quiz_desc");

                Quiz quiz = new Quiz(quizName, quizDesc, new ArrayList<>());
                quiz.setId(quizId);

                try (PreparedStatement questionStatement = connection.prepareStatement(questionSql)) {
                    questionStatement.setInt(1, quizId);
                    ResultSet questionResultSet = questionStatement.executeQuery();

                    while (questionResultSet.next()) {
                        int questionId = questionResultSet.getInt("question_id");
                        String questionText = questionResultSet.getString("question_text");

                        Question question = new Question(questionText);
                        question.setQuestionId(questionId);
                        quiz.getQuestions().add(question);

                        try (PreparedStatement answerStatement = connection.prepareStatement(answerSql)) {
                            answerStatement.setInt(1, questionId);
                            ResultSet answerResultSet = answerStatement.executeQuery();

                            while (answerResultSet.next()) {
                                String answerText = answerResultSet.getString("answer_text");
                                String answerType = answerResultSet.getString("answer_type");

                                boolean isCorrect = "C".equals(answerType);
                                question.addAnswer(answerText, isCorrect);
                            }
                        }
                    }
                }
                quizzesPulled++;
                quizzes.add(quiz);
            }
        } finally {
            connection.close();
        }

        return quizzes;
    }

}