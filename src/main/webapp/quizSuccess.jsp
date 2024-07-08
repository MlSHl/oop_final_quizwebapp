<!-- quizSuccess.jsp -->
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Quiz Created</title>
</head>
<body>
<h1>Quiz Created Successfully!</h1>
<p>Title: ${quiz.title}</p>
<c:forEach var="question" items="${quiz.questions}">
  <p>Question: ${question.text}</p>
  <c:forEach var="answer" items="${question.answers}">
    <p>Answer: ${answer}</p>
  </c:forEach>
</c:forEach>
</body>
</html>
