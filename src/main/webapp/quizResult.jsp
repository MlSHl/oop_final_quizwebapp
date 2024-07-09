<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="CSS/quizResult.css" rel="stylesheet">
    <title>Quiz Result</title>
</head>
<body>
<nav>
    <ul class="nav-ul">
        <li class="nav-li"><a href="home-page.jsp"><img class="logo" src="image/logo.jpg"></a></li>
        <li class="nav-li"><a href="UserPage.jsp"><i class="fas fa-user nav-icon"></i>profile</a></li>
        <li class="nav-li"><a href="quiz_creation_page.jsp"><i class="fas fa-pencil-alt nav-icon"></i>create quiz</a></li>
    </ul>

    <section class="search">
        <input type="text" placeholder="quizzes, users ..." class="search-field">
        <button type="button" class="search-button"><i class="fas fa-search"></i></button>
    </section>
</nav>
<%
    int score = Integer.parseInt(request.getParameter("score"));
    int quizId = Integer.parseInt(request.getParameter("quizId"));
%>
<h1>Quiz Result</h1>
<section class = "res">
<p>Your score: <%= score %></p>
<a href="home-page.jsp">Back to Home Page</a>
</section>
</body>
</html>
