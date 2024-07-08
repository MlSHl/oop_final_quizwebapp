package org.example.quizwebapp.DAO;

import org.example.quizwebapp.Model.Quiz;
import org.example.quizwebapp.Model.User;
import org.example.quizwebapp.Servlet.Question;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class QuizDAO {

    public void createQuiz(User creator, Quiz quiz) throws SQLException, ClassNotFoundException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        String sql = "Insert into quizzes ( quiz_name, quiz_desc, creator_name) VALUES (?,?,?) ";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, quiz.getTitle());
        statement.setString(2, quiz.getDesc());
        statement.setString(3, creator.getUsername());

        statement.executeUpdate();

        // TODO: Use Result Set to retrieve data and then do setId()
        // TODO: Make sure you create/use necessary amount of statements

        String sql_id = "select quiz_id from quizzes order by quiz_id desc limit 1";
        PreparedStatement statementQuizId = connection.prepareStatement(sql_id);
        quiz.setId(statement.executeQuery(sql_id).getInt(1));

        for(Question question : quiz.getQuestions()) {
            String sql_insert_new_question =
                    "insert into questions (quiz_id, question_text) VALUES (?,?)";
            statement.setInt(1, quiz.getId());
            statement.setString(2, question.getText());
            statement.executeUpdate(sql_insert_new_question);
            List<String> answers = question.getAnswers();
            List<Boolean> correctAnswers = question.getCorrectAnswers();
            for(int i = 0; i < answers.size(); i++){
                String sql_answer = "insert into answers (question_id, answer_text, answer_type) VALUES (?,?,?)";
            }
        }


    }

    public List<Quiz> getPopularQuizzes(int numQUizzes){

        return null;

    }

    public List<Quiz> getRecentQuizzes(int numQUizzes){
        return null;
        //return most recent numQUizzes quizzes
    }

}