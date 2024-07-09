<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link href="CSS/style.css" rel="stylesheet">
    <title>User Registration</title>
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,700italic,400italic,700" rel="stylesheet">
</head>
<body>
<nav>
    <ul>
        <li><a href="login.jsp">Log In</a></li>
        <li><a href="quiz_creation_page.jsp">profile</a> </li>
        <li><a href="quizzes.jsp">Leaderboard</a> </li>
    </ul>
</nav>
<main>
    <header>
        <h2>User Registration</h2>
    </header>
    <form action="register" method="post">

        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>

        <input type="submit" value="Register">
    </form>
</main>
</body>
</html>


