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
<main>
    <section class = "sec-login">
        <header class = "login-bad-txt">
            <h2>Login Unsuccessful</h2>
        </header>
        <section class = "error">
            <p>
                <strong><%= session.getAttribute("errorMessage") %> </strong>
            </p>
        </section>
        <section class = "go-back">
            <a href="index.jsp">Register</a>
            <a href="login.jsp">Log In</a>
        </section>
    </section>
</main>
</body>
</html>
<% session.removeAttribute("errorMessage"); %>
