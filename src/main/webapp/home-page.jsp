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
        <li class="nav-li"><a href="home-page.jsp"><img class="logo" src="weroebi_logo.jpg"><a href="home-page.jsp"></a></li>
        <li class="nav-li"><a href="UserPage.jsp">profile</a></li>
        <li class="nav-li"><a href="#">create quiz</a></li>
    </ul>
    <section class="search">
        <label>
            <input type="text" placeholder="quizzes, users ..." class="search-field">
        </label>
        <button type="button" class="search-button">search</button>
    </section>
</nav>
<main>
    <div class="section-1">
        <fieldset id="popular-quizzes">
            <legend><em>Popular Quizzes</em></legend>
            <ul>
                <li><a class="grdzeli" href="#">qvizis saxeli linkad</a></li>
                <li><a class="grdzeli" href="#">Quiz 2</a></li>
                <li><a class="grdzeli" href="#">Quiz 3</a></li>
            </ul>
        </fieldset>

        <fieldset id="recent-quizzes">
            <legend><em>Recently Created Quizzes</em></legend>
            <ul>
                <li><a class="grdzeli" href="#">Quiz A</a></li>
                <li><a class="grdzeli" href="#">Quiz B</a></li>
                <li><a class="grdzeli" href="#">Quiz C</a></li>
            </ul>
        </fieldset>

        <fieldset id="user-activities">
            <legend><em>Your Recent Activities</em></legend>
            <ul>
                <li><a class="grdzeli" href="#">Quiz X</a></li>
                <li><a class="grdzeli" href="#">Quiz Y</a></li>
            </ul>
        </fieldset>

        <fieldset id="user-created-quizzes">
            <legend><em>Your Created Quizzes</em></legend>
            <ul>
                <li><a class="grdzeli" href="#">Quiz Alpha</a></li>
                <li><a class="grdzeli" href="#">Quiz Beta</a></li>
            </ul>
        </fieldset>

        <fieldset id="achievements">
            <legend><em>Your Achievements</em></legend>
            <ul>
                <li class="ach-txt">
                    <p>Achievement 1</p>
                </li>
                <li class="ach-txt">
                    <p>Achievement 2</p>
                </li>
            </ul>
        </fieldset>

        <fieldset id="friends-activities">
            <legend><em>Friends' Recent Activities</em></legend>
            <ul>
                <li class="teqstiani">
                    <a class="grdzeli" href="#">Friend1</a>
                    <p>took</p>
                    <a class="grdzeli" href="#">Quiz 3</a>
                </li>
                <li class="teqstiani">
                    <a class="grdzeli" href="#">Friend2</a>
                    <p>earned</p>
                    <a class="grdzeli" href="#">Achievement X</a>
                </li>
            </ul>
        </fieldset>
    </div>
</main>
</body>
</html>
