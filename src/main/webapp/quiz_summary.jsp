<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quiz Summary</title>
</head>
<body>
<h1>Quiz Summary</h1>

<c:if test="${not empty quiz}">
    <c:forEach var="question" items="${quiz}">
        <h2>${question.text}</h2>
        <ul>
            <c:forEach var="answer" items="${question.answers}" varStatus="status">
                <li>
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
