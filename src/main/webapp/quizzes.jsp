<%@ page import="java.sql.*" %>
<%@ page import="org.example.quizwebapp.DAO.ConnectionPool" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Available Quizzes</title>
</head>
<body>
<%
    ConnectionPool pool = null;
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        pool = ConnectionPool.getInstance();
        conn = pool.getConnection();

        String quizQuery = "SELECT quiz_id, quiz_name FROM quizzes";
        stmt = conn.prepareStatement(quizQuery);
        rs = stmt.executeQuery();

        while (rs.next()) {
            int quizId = rs.getInt("quiz_id");
            String quizName = rs.getString("quiz_name");
%>
        <form action="takeQuiz.jsp" method="get">
            <input type="hidden" name="quizId" value="<%= quizId %>">
            <button type="submit"><%= quizName %></button>
        </form>
<%
    }
} catch (Exception e) {
    e.printStackTrace();
%>
<p>Error retrieving quizzes. Please try again later.</p>
<%
    } finally {
        if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
        if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
        if (conn != null) {
            try {
                ConnectionPool.releaseConnection(conn);
            } catch (Exception ignore) {}
        }
    }
%>
</body>
</html>
