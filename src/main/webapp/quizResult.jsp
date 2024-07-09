<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quiz Result</title>
</head>
<body>
<%
    int score = Integer.parseInt(request.getParameter("score"));
    int quizId = Integer.parseInt(request.getParameter("quizId"));
%>
<h1>Quiz Result</h1>
<p>Your score: <%= score %></p>
<a href="quizzes.jsp">Back to Quizzes</a>
</body>
</html>
