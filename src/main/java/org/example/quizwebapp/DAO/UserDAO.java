package org.example.quizwebapp.DAO;


import org.example.quizwebapp.Model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    private final static String DBNAME = "quiz_db";
    private final static String TABLENAME = "users";


    public boolean addUser(User user) throws SQLException, ClassNotFoundException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = ConnectionPool.getConnection();
        String sql = "INSERT INTO users (user_name, password)VALUES(?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());

        try{
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
        statement.close();
        ConnectionPool.releaseConnection(connection);

        return true;
    }


}
