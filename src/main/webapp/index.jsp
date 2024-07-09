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
    <ul class="nav-ul">
        <li class="nav-li"><a href="#"><img class="logo" src="image/logo.jpg"></a></li>
        <li class="nav-li"><a href="login.jsp">Log In<i class="fas fa-user nav-icon"></i></a></li>
        <li class="nav-li"><a href="#"><i class="fas fa-pencil-alt nav-icon"></i>Leaderboard</a></li>
    </ul>

</nav>

<main>
    <div class="loginInfo">
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
        </div>
</main>
</body>
</html>


