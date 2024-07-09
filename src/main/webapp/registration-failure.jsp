<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="CSS/style.css" rel="stylesheet">
    <title>Registration Unsuccessful</title>
</head>
<body>
<nav>
    <ul>
        <li><a href="#">Quizzes</a></li>
        <li><a href="#">Profile</a></li>
        <li><a href="#">Leaderboard</a></li>
    </ul>
</nav>
<main>
    <header>
        <h2>Registration Unsuccessful</h2>
    </header>
    <section>
        <p style="font-size: large; color: red;">
            <%= session.getAttribute("errorMessage") %>
        </p>
    </section>
    <section>
        <a href="index.jsp">Go back to registration</a>
        <a href="login.jsp">Log In</a>
    </section>
</main>
</body>
</html>
<% session.removeAttribute("errorMessage"); %>

