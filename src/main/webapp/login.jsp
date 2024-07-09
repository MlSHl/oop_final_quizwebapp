<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link href="CSS/style.css" rel="stylesheet">
    <title>Login</title>
</head>
<body>
<nav>
    <ul>
        <li><a href="#">Quizes</a></li>
        <li><a href="#">Profile</a> </li>
        <li><a href="#">Leaderboard</a> </li>
    </ul>
</nav>
<main>
    <header>
        <h2>User Login</h2>
    </header>
    <form action="login" method="post">

        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>

        <input type="submit" value="Login">
    </form>
</main>
</body>
</html>
