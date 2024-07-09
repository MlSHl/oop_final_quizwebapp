package org.example.quizwebapp.DAO;


import org.example.quizwebapp.CustomExceptions.RequestAlreadyExists;
import org.example.quizwebapp.CustomExceptions.RequestDoesntExist;
import org.example.quizwebapp.CustomExceptions.UserAlreadyExistsException;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.Model.*;
import org.example.quizwebapp.Utils.JwtUtil;
import org.example.quizwebapp.Utils.PasswordEncryptor;
import org.example.quizwebapp.Utils.UserValidation;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    public void requestFriendship(User requester, User receiver) throws SQLException, ClassNotFoundException, RequestAlreadyExists {
        if(checkIfFriendRequestExists(receiver, requester)){
            throw new RequestAlreadyExists("Friend Request Already Exists");
        }
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

    public boolean checkIfFriendRequestExists(User acceptor, User requester) throws SQLException, ClassNotFoundException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        String checkRequestSQL = "SELECT COUNT(*) FROM friend_requests WHERE sender_name = ? AND reciever_name = ?";

        try (PreparedStatement checkRequestStatement = connection.prepareStatement(checkRequestSQL)) {
            checkRequestStatement.setString(1, requester.getUsername());
            checkRequestStatement.setString(2, acceptor.getUsername());
            try (ResultSet resultSet = checkRequestStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } finally {
            ConnectionPool.releaseConnection(connection);
        }
        return false;
    }


    public void acceptFriendship(User accepter, User requester) throws RequestDoesntExist, SQLException, ClassNotFoundException {
        if(!checkIfFriendRequestExists(accepter, requester)){
            throw new RequestDoesntExist("Can not accept a friend request that doesn't exist");
        }

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        try {
            connection.setAutoCommit(false);

            String deleteRequestSQL = "DELETE FROM friend_requests WHERE sender_name = ? AND reciever_name = ?";
            try (PreparedStatement deleteRequestStatement = connection.prepareStatement(deleteRequestSQL)) {
                deleteRequestStatement.setString(1, requester.getUsername());
                deleteRequestStatement.setString(2, accepter.getUsername());
                deleteRequestStatement.executeUpdate();
            }

            String insertFriendSQL = "INSERT INTO friends (user_name, friend_name) VALUES (?, ?)";
            try (PreparedStatement insertFriendStatement = connection.prepareStatement(insertFriendSQL)) {
                insertFriendStatement.setString(1, accepter.getUsername());
                insertFriendStatement.setString(2, requester.getUsername());
                insertFriendStatement.executeUpdate();

                insertFriendStatement.setString(1, requester.getUsername());
                insertFriendStatement.setString(2, accepter.getUsername());
                insertFriendStatement.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
            ConnectionPool.releaseConnection(connection);
        }
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
        int numPoints = getPointAmount(username);
        List<User> friends = getFriendList(username);
        Profile profile = new Profile(username, achievements, quizzes, numPoints, friends);
        if(ownProfile) profile.setOwnProfile(true);
        return profile;
    }

    public List<Quiz> getQuizzes(String username) throws SQLException, ClassNotFoundException {
        List<Quiz> quizzes = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String quizSql = "SELECT quiz_id, quiz_name, quiz_desc FROM quizzes " +
                "WHERE creator_name = ? ORDER BY quiz_id DESC";
        String questionSql = "SELECT question_id, question_text FROM questions WHERE quiz_id = ?";
        String answerSql = "SELECT answer_text, answer_type FROM answers WHERE question_id = ?";

        try (PreparedStatement quizStatement = connection.prepareStatement(quizSql)) {
            quizStatement.setString(1, username);
            ResultSet quizResultSet = quizStatement.executeQuery();

            while (quizResultSet.next()) {
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

                        // Populate answers for the current question
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

                quizzes.add(quiz);
            }
        } finally {

            ConnectionPool.releaseConnection(connection);
            // ConnectionPool.releaseConnection(connection);
        }

        return quizzes;
    }

    public int getPointAmount(String username) throws SQLException, ClassNotFoundException {
        int totalPoints = 0;

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT SUM(best_score) AS total_points " +
                "FROM user_quiz_scores " +
                "WHERE user_name = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                totalPoints = resultSet.getInt("total_points");
            }
        } finally {
            ConnectionPool.releaseConnection(connection);
        }

        return totalPoints;
    }

    public List<User> getFriendList(String username) throws SQLException, ClassNotFoundException {
        List<User> friendList = new ArrayList<>();
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            String sql = "SELECT u.user_name, u.password " +
                    "FROM quiz_db.friends f " +
                    "JOIN quiz_db.users u ON f.friend_name = u.user_name " +
                    "WHERE f.user_name = ?";

            statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String friendUsername = resultSet.getString("user_name");
                String friendPassword = resultSet.getString("password");

                User friend = new User(friendUsername, friendPassword);
                friendList.add(friend);
            }
        } finally {
            // Close resources in finally block to ensure they are always released
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

    public int getTakenQuizAmount(String userName) throws SQLException, ClassNotFoundException {
                ConnectionPool connectionPool = ConnectionPool.getInstance();
                Connection connection = connectionPool.getConnection();

                String sql = "SELECT COUNT(quiz_id) AS quiz_count FROM user_quiz_scores WHERE user_name = ?";

                int quizCount = 0;

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, userName);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        quizCount = resultSet.getInt("quiz_count");
                    }
                } finally {
                    ConnectionPool.releaseConnection(connection);
                }

                return quizCount;
    }

    public int getCreatedQuizAmount(String username) throws SQLException, ClassNotFoundException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT COUNT(quiz_id) AS quiz_count FROM quizzes WHERE creator_name = ?";

        int quizCount = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                quizCount = resultSet.getInt("quiz_count");
            }
        } finally {
            ConnectionPool.releaseConnection(connection);
        }

        return quizCount;
    }

    public List<Quiz> getUserCompletedQuizzes(int numQuizzes, String username) throws SQLException, ClassNotFoundException {
        List<Quiz> quizzes = new ArrayList<>();

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        int quizzesPulled = 0;

        String quizSql = "SELECT quiz_id, quiz_name, quiz_desc FROM quizzes " +
                "WHERE quiz_id IN (SELECT DISTINCT quiz_id FROM user_quiz_scores WHERE user_name = ?)" +
                "ORDER BY quiz_id DESC";
        String questionSql = "SELECT question_id, question_text FROM questions WHERE quiz_id = ?";
        String answerSql = "SELECT answer_text, answer_type FROM answers WHERE question_id = ?";

        try (PreparedStatement quizStatement = connection.prepareStatement(quizSql)) {
            quizStatement.setString(1, username);
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

                        // Populate answers for the current question
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

            ConnectionPool.releaseConnection(connection);
            // ConnectionPool.releaseConnection(connection);
        }

        return quizzes;
    }

    public List<Object> getRecentFriendActivities(String username) throws SQLException, ClassNotFoundException {
        List<Object> recentActivities = new ArrayList<>();

        List<String> friendUsernames = retrieveFriendUsernames(username);

        Quiz recentTakenQuiz = retrieveMostRecentQuizTakenByFriend(friendUsernames);
        if (recentTakenQuiz != null) {
            recentActivities.add(recentTakenQuiz);
        }else{
            recentActivities.add(-1);
        }

        Quiz recentCreatedQuiz = retrieveMostRecentQuizCreatedByFriend(friendUsernames);
        if (recentCreatedQuiz != null) {
            recentActivities.add(recentCreatedQuiz);
        }else{
            recentActivities.add(-1);
        }

        Achievement recentAchievement = retrieveMostRecentAchievementByFriend(friendUsernames);
        if (recentAchievement != null) {
            recentActivities.add(recentAchievement);
        }else{
            recentActivities.add(-1);
        }

        return recentActivities;
    }

    private List<String> retrieveFriendUsernames(String username) throws SQLException, ClassNotFoundException {
        List<String> friendUsernames = new ArrayList<>();
        List<User> friends = getFriendList(username);
        for(User friend : friends){
            friendUsernames.add(friend.getUsername());
        }
        return friendUsernames;
    }

    private Quiz retrieveMostRecentQuizTakenByFriend(List<String> friendUsernames) throws SQLException, ClassNotFoundException {
        Quiz recentQuizTaken = null;

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT q.quiz_id, q.quiz_name, q.quiz_desc " +
                "FROM user_quiz_scores uqs " +
                "JOIN quizzes q ON uqs.quiz_id = q.quiz_id " +
                "WHERE uqs.username IN (" + createInClause(friendUsernames.size()) + ") " +
                "ORDER BY uqs.quiz_completed_at DESC " +
                "LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < friendUsernames.size(); i++) {
                statement.setString(i + 1, friendUsernames.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int quizId = resultSet.getInt("quiz_id");
                String quizName = resultSet.getString("quiz_name");
                String quizDesc = resultSet.getString("quiz_desc");

                recentQuizTaken = new Quiz(quizName, quizDesc, new ArrayList<>());
                recentQuizTaken.setId(quizId);

            }
        } finally {

            ConnectionPool.releaseConnection(connection);
            // ConnectionPool.releaseConnection(connection);
        }

        return recentQuizTaken;
    }

    private Quiz retrieveMostRecentQuizCreatedByFriend(List<String> friendUsernames) throws SQLException, ClassNotFoundException {
        Quiz recentQuizCreated = null;

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT q.quiz_id, q.quiz_name, q.quiz_desc " +
                "FROM quizzes q " +
                "WHERE q.creator_name IN (" + createInClause(friendUsernames.size()) + ") " +
                "ORDER BY q.quiz_id DESC " +
                "LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < friendUsernames.size(); i++) {
                statement.setString(i + 1, friendUsernames.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int quizId = resultSet.getInt("quiz_id");
                String quizName = resultSet.getString("quiz_name");
                String quizDesc = resultSet.getString("quiz_desc");

                recentQuizCreated = new Quiz(quizName, quizDesc, new ArrayList<>());
                recentQuizCreated.setId(quizId);
            }
        } finally {

            ConnectionPool.releaseConnection(connection);
            // ConnectionPool.releaseConnection(connection);
        }

        return recentQuizCreated;
    }


    public Achievement retrieveMostRecentAchievementByFriend(List<String> friendUsernames)
            throws SQLException, ClassNotFoundException {
        Achievement recentAchievement = null;

        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT ad.achievement_name, ad.achievement_desc, ad.achievement_img " +
                "FROM user_achievement ua " +
                "JOIN achievement_desc ad ON ua.achievement_id = ad.achievement_id " +
                "WHERE ua.username IN (" + createInClause(friendUsernames.size()) + ") " +
                "ORDER BY ua.id DESC " +
                "LIMIT 1";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < friendUsernames.size(); i++) {
                statement.setString(i + 1, friendUsernames.get(i));
            }

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String achievementName = resultSet.getString("achievement_name");
                String achievementDesc = resultSet.getString("achievement_desc");
                String achievementImg = resultSet.getString("achievement_img");

                recentAchievement = new Achievement(achievementName, achievementDesc, achievementImg);
            }
        } finally {
            ConnectionPool.releaseConnection(connection);
        }

        return recentAchievement;
    }

    private String createInClause(int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("?");
            if (i < size - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
}