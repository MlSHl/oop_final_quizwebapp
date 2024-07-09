<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link href="CSS/style.css" rel="stylesheet">
    <title>Login</title>
</head>
<body>

<nav>
    <ul class="nav-ul">
        <li class="nav-li"><a href="#"><img class="logo" src="image/logo.jpg"></a></li>
        <li class="nav-li"><a href="#"><i class="fas fa-user nav-icon"></i>Profile</a></li>
    </ul>

</nav>

<main>
    <div class="loginInfo">
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
    </div>
</main>

</body>
</html>
