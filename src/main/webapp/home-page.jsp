<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link href="CSS/home-page.css" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,700italic,400italic,700" rel="stylesheet">
    <title>weroebi</title>
</head>
<body>
<nav>
    <ul class="nav-ul">
        <li class="nav-li"><a href="#">weroebi</a> </li> <!-- weroebis magvirad logo pirdapir sheidzleba -->
        <li class="nav-li"><a href="#">profile</a></li>
        <li class="nav-li"><a href="#">create quiz</a></li>
    </ul>
    <section class = "search">
        <input type = "text" placeholder="quizzes, users ..." class = "search-field">
        <button type = "button" class = "search-button">search</button>
    </section>
</nav>
<main>
    <div class="section-1">
        <fieldset id="popular-quizzes">
            <legend><em>Popular Quizzes</em></legend>
            <ul>
                <li class = "aa"><a href="#">qvizis saxeli linkad</a></li>
                <li><a href="#">Quiz 2</a></li>
                <li><a href="#">Quiz 3</a></li>
            </ul>
        </fieldset>

        <fieldset id="recent-quizzes">
            <legend>Recently Created Quizzes</legend>
            <ul>
                <li><a href="#">Quiz A</a></li>
                <li><a href="#">Quiz B</a></li>
                <li><a href="#">Quiz C</a></li>
            </ul>
        </fieldset>

        <fieldset id="user-activities">
            <legend>Your Recent Activities</legend>
            <ul>
                <li>Took <a href="#">Quiz X</a></li>
                <li>Took <a href="#">Quiz Y</a></li>
            </ul>
        </fieldset>

        <fieldset id="user-created-quizzes">
            <legend>Your Created Quizzes</legend>
            <ul>
                <li><a href="#">Quiz Alpha</a></li>
                <li><a href="#">Quiz Beta</a></li>
            </ul>
        </fieldset>

        <fieldset id="achievements">
            <legend>Your Achievements</legend>
            <ul>
                <li>Achievement 1</li>
                <li>Achievement 2</li>
            </ul>
        </fieldset>

        <fieldset id="friends-activities">
            <legend>Friends' Recent Activities</legend>
            <ul>
                <li class = "teqstiani">
                    <a href="#">Friend1</a>
                    <p>took </p>
                    <a href="#">Quiz 3</a>
                </li>
                <li class = "teqstianebi">
                    <a href="#">Friend2</a>
                    <p>earned</p>
                    <a href="#">Achievement X</a>
                </li>
            </ul>
        </fieldset>
    </div>
</main>
</body>
</html>

