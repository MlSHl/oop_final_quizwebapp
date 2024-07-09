<%@ page import="org.example.quizwebapp.DAO.SearchDAO" %>
<%@ page import="org.example.quizwebapp.Model.User" %>
<%@ page import="org.example.quizwebapp.Model.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="CSS/search.css" rel="stylesheet">
    <title>Search Results</title>
</head>
<body>
<h1>Search Results</h1>
<%
    String s = request.getParameter("searchQuery");
%>
<p>You searched for: <%= s %></p>

<%
    SearchDAO searchDAO = new SearchDAO();
    List<User> userList = null;
    List<Quiz> quizList = null;
    try {
        userList = searchDAO.getUsers(s);
        quizList = searchDAO.getQuizzesByName(s);
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
%>
<main>
<div class="users">
<h2>Users</h2>
<%
    if (userList != null && !userList.isEmpty()) {
        for(User user : userList) {
            String user_name = user.getUsername();
%>
<div class="user-result">
    <form action="UserPage.jsp" method="get">
        <input type="hidden" name="profileUser" value="<%= user_name %>">
        <button class="grdzeli" type="submit"><%= user_name %></button>
    </form>
</div>
<%
    }
} else {
%>
<p>No users found.</p>
<%
    }
%>
</div>
<div class="quizzes">
<h2>Quizzes</h2>
<%
    if (quizList != null && !quizList.isEmpty()) {
        for(Quiz quiz : quizList) {
            int quiz_id = quiz.getId();
%>
<form action="takeQuiz.jsp" method="get">
    <input type="hidden" name="quizId" value="<%= quiz_id %>">
    <button class="grdzeli" type="submit"><%= quiz.getTitle() %></button>
</form>
<%
    }
} else {
%>
<p>No quizzes found.</p>
<%
    }
%>
</div>
</main>
</body>
</html>
