<%@ page import="org.example.quizwebapp.DAO.QuizDAO" %>
<%@ page import="org.example.quizwebapp.Model.Quiz" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.example.quizwebapp.Model.Achievement" %>
<%@ page import="org.example.quizwebapp.DAO.UserDAO" %>
<%@ page import="org.example.quizwebapp.CustomExceptions.UserNotFoundException" %>
<%@ page import="org.example.quizwebapp.Utils.JwtUtil" %>
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
        <li class="nav-li"><img class="logo" src="image/weroebi_logo.jpg" alt="logo.jpg"><a href="home-page.jsp"></a></li>
        <li class="nav-li"><a href="UserPage.jsp">profile</a></li>
        <li class="nav-li"><a href="quiz_creation_page.jsp">create quiz</a></li>
    </ul>
    <section class="search">
        <form action="SearchServlet" method="get">
            <input type="text" id="searchQuery" name="searchQuery" class="search-field" required>
            <button type="submit" class="search-button">Search</button>
        </form>
    </section>


</nav>
<main>
    <div class="section-1">
        <fieldset id="popular-quizzes">
            <legend><em>Popular Quizzes</em></legend>

            <%
                QuizDAO qdpop = new QuizDAO();
                List<Quiz> quizpoplist = null;
                try {
                    quizpoplist = qdpop.getPopularQuizzes(2);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                for(Quiz q : quizpoplist) {
                    int quizId = q.getId();
                    String quizName = q.getTitle();
            %>
            <form action="takeQuiz.jsp" method="get">
                <input type="hidden" name="quizId" value="<%= quizId %>">
                <button class="grdzeli" type="submit"><%= quizName %></button>
            </form>
            <%
                }
            %>
        </fieldset>

        <fieldset id="recent-quizzes">
            <legend><em>Recently Created Quizzes</em></legend>
            <%
                QuizDAO qd = new QuizDAO();
                List<Quiz> quizrecentlist = null;
                try {
                    quizrecentlist = qd.getRecentQuizzes(2);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                for(Quiz q : quizrecentlist) {
                    int quizId = q.getId();
                    String quizName = q.getTitle();
            %>
            <form action="takeQuiz.jsp" method="get">
                <input type="hidden" name="quizId" value="<%= quizId %>">
                <button class="grdzeli" type="submit"><%= quizName %></button>
            </form>
            <%
                }
            %>
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
                <%
                    String token = (String) request.getSession().getAttribute("token");
                    String userName = JwtUtil.extractUsername(token);
                    UserDAO userDAO = new UserDAO();
                    List<Quiz> userCreatedQuizzes = null;
                    try {
                        userCreatedQuizzes = userDAO.getQuizzes(userName);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    if (userCreatedQuizzes.isEmpty()) {
                %>
                    <p>You haven't created any quizzes yet.</p>
                <%
                } else {
                    for (Quiz quiz : userCreatedQuizzes) {
                        int quizId = quiz.getId();
                        String quizName = quiz.getTitle();
                %>
                    <form action="takeQuiz.jsp" method="get">
                        <input type="hidden" name="quizId" value="<%= quizId %>">
                        <button type="submit" class="grdzeli"><%= quizName %></button>
                    </form>
                <%
                        }
                    }
                %>
            </ul>
        </fieldset>


        <fieldset id="achievements">
            <legend><em>Your Achievements</em></legend>
            <ul>
                <%
                    UserDAO dao = new UserDAO();
                    List<Achievement> achievements = null;
                    try {
                        achievements = dao.getAchievements(userName);
                    } catch (UserNotFoundException | SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                    if (achievements.isEmpty()) {
                %>
                <li class="ach-txt">
                    <p>User doesn't have any achievements.</p>
                </li>
                <%
                } else {
                    for (Achievement achievement : achievements) {
                %>
                <li class="ach-txt">
                    <img class = "ach-img" src="<%= achievement.getImg() %>" alt="<%= achievement.getName() %> Image">
                    <h3 class = "ach-name"><%= achievement.getName() %></h3>
                    <p class = "ach-txt"><%= achievement.getDescription() %></p>
                </li>
                <%
                        }
                    }
                %>
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
