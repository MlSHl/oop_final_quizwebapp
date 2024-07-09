package org.example.quizwebapp.DAO;

import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.Model.Quiz;
import org.example.quizwebapp.Model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchDAO {
    private final UserDAO userDao = new UserDAO();
    private final QuizDAO quizDao = new QuizDAO();
    public List<User> getUsers(String searchTerm) throws SQLException, ClassNotFoundException {
        ArrayList<User> usersList = new ArrayList<>();
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT * FROM users WHERE user_name LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String username = resultSet.getString("user_name");

                User user = userDao.getUserByUsername(username);
                usersList.add(user);
            }
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            connection.close();
        }

        return usersList;
    }

    public List<Quiz> getQuizzesByName(String searchTerm) throws SQLException, ClassNotFoundException {
        ArrayList<Quiz> quizList = new ArrayList<>();
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT * FROM quizzes WHERE quiz_name LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("quiz_name");

                List<Quiz> quizMatchName = quizDao.getQuizByQuizName(name);
                quizList.addAll(quizMatchName);
            }
        } finally {
            connection.close();
        }

        return quizList;
    }

    public List<Quiz> getQuizzesByDesc(String searchTerm) throws SQLException, ClassNotFoundException {
        ArrayList<Quiz> quizList = new ArrayList<>();
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT * FROM quizzes WHERE quiz_desc LIKE ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + searchTerm + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String desc = resultSet.getString("quiz_desc");

                List<Quiz> quizMatchDesc = quizDao.getQuizByQuizDesc(desc);
                quizList.addAll(quizMatchDesc);
            }
        } finally {
            connection.close();
        }
        return quizList;
    }
}
