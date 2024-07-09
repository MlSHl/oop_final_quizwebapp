<%@ page import="org.example.quizwebapp.DAO.SearchDAO" %>
<%@ page import="org.example.quizwebapp.Model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.example.quizwebapp.Model.Quiz" %>
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
<p>You searched for: <%= request.getAttribute("searchQuery") %></p>

<%
    SearchDAO searchDAO = new SearchDAO();

%>

<%
    SearchDAO sd = new SearchDAO();
    List<User> userList= null;
    List<Quiz> quizList = null;
    try {
        userList = sd.getUsers(s);
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    for(User u : userList) {
        String user_name = u.getUsername();

%>
<form action="UserPage.jsp" method="get">
    <input type="hidden" name="user_name" value="<%= user_name %>">
    <button class="grdzeli" type="submit"><%= user_name %></button>
</form>
<%
    }
%>


<%
    try {
        quizList = sd.getQuizzesByName(s);
    } catch (SQLException | ClassNotFoundException e) {
        throw new RuntimeException(e);
    }
    for(Quiz u : quizList) {
        int quiz_id= u.getId();

%>
<form action="takeQuiz.jsp" method="get">
    <input type="hidden" name="quizId" value="<%= quiz_id %>">
    <button class="grdzeli" type="submit"><%= u.getTitle() %></button>
</form>
<%
    }
%>

<!-- Display the search results here -->
</body>
</html>
