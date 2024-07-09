package org.example.quizwebapp.DAO;


import org.example.quizwebapp.CustomExceptions.UserAlreadyExistsException;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.Model.Achievement;
import org.example.quizwebapp.Model.Profile;
import org.example.quizwebapp.Model.Quiz;
import org.example.quizwebapp.Model.User;
import org.example.quizwebapp.Utils.JwtUtil;
import org.example.quizwebapp.Utils.PasswordEncryptor;
import org.example.quizwebapp.Utils.UserValidation;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public void addUser(User user) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UserAlreadyExistsException {
        if (!UserValidation.CheckUnique(user.getUsername())) {
            throw new UserAlreadyExistsException();
        }
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        String sql = "INSERT INTO quiz_db.users (user_name, password)VALUES(?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, PasswordEncryptor.Encrypt(user.getPassword()));

        try{
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            return;
        }
        statement.close();
        ConnectionPool.releaseConnection(connection);

    }

    public User getUser(User user) throws SQLException, ClassNotFoundException, NoSuchAlgorithmException, UserAlreadyExistsException, UserNotFoundException {
        String sql = "SELECT * FROM quiz_db.users WHERE user_name = ? AND password = ?";
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        try{
            statement.setString(1, user.getUsername());
            statement.setString(2, PasswordEncryptor.Encrypt(user.getPassword()));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getString("user_name"),
                            resultSet.getString("password")
                    );
                }else{
                    throw new UserNotFoundException();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.releaseConnection(connection);
        }
        return null;
    }

    public User getUserByUsername(String username) throws SQLException, ClassNotFoundException, UserNotFoundException {
        String sql = "SELECT * FROM quiz_db.users WHERE user_name = ?";
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        try {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new User(
                        resultSet.getString("user_name"),
                        resultSet.getString("password")
                );
            } else {
                throw new UserNotFoundException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            ConnectionPool.releaseConnection(connection);
        }
    }

    public List<Achievement> getMyAchievements(String token) throws UserNotFoundException, SQLException, ClassNotFoundException {
        String username = JwtUtil.extractUsername(token);
        return getAchievements(username);
    }

    public List<Achievement> getAchievements(String username) throws UserNotFoundException, SQLException, ClassNotFoundException {
        User user = getUserByUsername(username);

        String sql = "SELECT ad.achievement_name, ad.achievement_desc, ad.achievement_img " +
                "FROM quiz_db.user_achievements ua " +
                "JOIN quiz_db.achievement_desc ad ON ua.achievement_id = ad.achievement_id " +
                "JOIN quiz_db.users u ON ua.user_name = u.user_name " +
                "WHERE u.user_name = ?";

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, user.getUsername());

        List<Achievement> achievements = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Achievement achievement = new Achievement(
                        resultSet.getString("achievement_name"),
                        resultSet.getString("achievement_desc"),
                        resultSet.getString("achievement_img")
                );
                achievements.add(achievement);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            statement.close();
            ConnectionPool.releaseConnection(connection);
        }
        return achievements;
    }


    public void requestFriendship(User requester, User receiver) throws SQLException, ClassNotFoundException {

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = null;

        try {
            String sql = "INSERT INTO quiz_db.friend_requests (sender_name, reciever_name) VALUES (?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, requester.getUsername());
            statement.setString(2, receiver.getUsername());

            statement.executeUpdate();

        } finally {
            if (statement != null) {
                statement.close();
            }
            ConnectionPool.releaseConnection(connection);
        }
    }

    public void acceptFriendship(User accepter, User requester) { //TODO
        // Implementation
    }

    public void challengeToQuiz(User challenger, User opponent, Quiz quiz) { //TODO
        // Implementation
    }

    public Profile getOwnProfile(String token) throws UserNotFoundException, SQLException, ClassNotFoundException {
        String username = JwtUtil.extractUsername(token);
        return getUserProfile(username, true);
    }
    public Profile getUserProfile(String username) throws SQLException, ClassNotFoundException, UserNotFoundException {
        return getUserProfile(username, false);
    }
    public Profile getUserProfile(String username, boolean ownProfile) throws UserNotFoundException, SQLException, ClassNotFoundException {
        List<Achievement> achievements = getAchievements(username);
        List<Quiz> quizzes = getQuizzes(username);
        int numQuizzes = getTakenQuizAmount(username);
        List<User> friends = getFriendList(username);
        return new Profile(username, achievements, quizzes, numQuizzes, friends);
    }

    public List<Quiz> getQuizzes(String username){ //TODO
        return new ArrayList<Quiz>();
    }

    public int getTakenQuizAmount(String username){ //TODO
        return 0;
    }

    public List<User> getFriendList(String username) throws UserNotFoundException, SQLException, ClassNotFoundException {
        List<User> friendList = new ArrayList<>();
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT u.* " +
                    "FROM quiz_db.friends f " +
                    "JOIN quiz_db.users u ON f.friend_name = u.user_name " +
                    "WHERE f.user_name = (SELECT user_name FROM quiz_db.users WHERE user_name = ?)";

            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User friend = new User(
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );
                friendList.add(friend);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            ConnectionPool.releaseConnection(connection);
        }

        return friendList;
    }

    public void gradeQuiz(){ //TODO
        //needs parameters, needs to get quiz id and answers from the front,
        //check against database answers for the quiz, count score, store it
        //for the user that took the quiz
    }

    public List<Quiz> getUserCompletedQuizzes(int numQuizzes, String token){ //TODO
        return null;
        //gets numQuizzes quizzes already completed by the user logged in, sorted from newest to oldest.
    }


//    public int getUserIdByUsername(String username) throws SQLException, ClassNotFoundException {
//        ConnectionPool connectionPool = ConnectionPool.getInstance();
//        Connection connection = connectionPool.getConnection();
//        PreparedStatement statement = null;
//        int userId = -1;
//
//        try {
//            String sql = "SELECT user_name FROM quiz_db.users WHERE user_name = ?";
//            statement = connection.prepareStatement(sql);
//            statement.setString(1, username);
//
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    userName = resultSet.getString("user_name");
//                }
//            }
//
//        } finally {
//            if (statement != null) {
//                statement.close();
//            }
//            ConnectionPool.releaseConnection(connection);
//        }
//
//        return userId;
//    }

}