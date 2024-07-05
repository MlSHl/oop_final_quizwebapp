package org.example.quizwebapp.DAO;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {

    private static ConnectionPool instance;
    private static List<Connection> connectionPool;
    private final static int INITIAL_POOL_SIZE = 8;
    private final static String USERNAME = "root";
    private final static String PASSWORD = "12345678";
    private final static String DB_NAME = "quiz_db";

    /**
     * Makes a connection pool of size INITIAL_POOL_SIZE
     * Uses the properties of the database provided above
     * in USERNAME and PASSWORD, as well as DBNAME
     */
    public ConnectionPool() throws ClassNotFoundException, SQLException {
        connectionPool = new ArrayList<>();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DB_NAME);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setInitialSize(INITIAL_POOL_SIZE);
        Class.forName("com.mysql.cj.jdbc.Driver");
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            connectionPool.add(dataSource.getConnection());
        }
    }

    public static synchronized ConnectionPool getInstance() throws ClassNotFoundException, SQLException {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    /**
     * Removes the last connection and returns it.
     */
    public static Connection getConnection(){
        Connection connection = connectionPool.get(connectionPool.size() - 1);
        connectionPool.remove(connectionPool.size() - 1);
        return connection;
    }

    /**
     * Adds new connection
     * @param connection - a connection to be added
     */
    public static void releaseConnection(Connection connection) {
        connectionPool.add(connection);
    }
}
