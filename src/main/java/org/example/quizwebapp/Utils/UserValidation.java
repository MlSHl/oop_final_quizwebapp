package org.example.quizwebapp.Utils;

import org.example.quizwebapp.DAO.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserValidation {
    public static boolean CheckUnique(String username) throws SQLException, ClassNotFoundException {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection connection = connectionPool.getConnection();

        String sql = "SELECT COUNT(*) FROM users WHERE user_name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);

        ResultSet resultSet = statement.executeQuery();
        boolean isUnique = true;
        if (resultSet.next()) {
            int count = resultSet.getInt(1);
            if (count > 0) {
                isUnique = false;
            }
        }

        resultSet.close();
        statement.close();
        ConnectionPool.releaseConnection(connection);

        return isUnique;
    }
}