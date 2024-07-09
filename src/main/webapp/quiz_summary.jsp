<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link href="CSS/quiz_summary.css" rel="stylesheet">
    <title>Quiz Summary</title>
</head>
<body>
<nav>
    <ul class="nav-ul">
        <li class="nav-li"><a href="#"><img class="logo" src="image/logo.jpg"></a></li>
        <li class="nav-li"><a href="#"><i class="fas fa-user nav-icon"></i>profile</a></li>
        <li class="nav-li"><a href="#"><i class="fas fa-pencil-alt nav-icon"></i>create quiz</a></li>
    </ul>

    <section class="search">
        <input type="text" placeholder="quizzes, users ..." class="search-field">
        <button type="button" class="search-button"><i class="fas fa-search"></i></button>
    </section>
</nav>

<h1>Quiz Summary</h1>

<h2>${quizName}</h2>
<p>${quizDescription}</p>

<c:if test="${not empty quiz}">
    <c:forEach var="question" items="${quiz}">
        <h3>${question.text}</h3>
        <ul>
            <c:forEach var="answer" items="${question.answers}" varStatus="status">
                <li class="liebi">
                        ${answer}
                    <c:if test="${question.correctAnswers[status.index]}">
                        <strong>(Correct)</strong>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </c:forEach>
</c:if>
</body>
</html>
