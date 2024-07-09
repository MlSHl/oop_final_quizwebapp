<%@ page import="org.example.quizwebapp.Utils.JwtUtil" %>
<%@ page import="org.example.quizwebapp.DAO.UserDAO" %>
<%@ page import="org.example.quizwebapp.Model.Achievement" %>
<%@ page import="java.util.List" %>
<%@ page import="org.example.quizwebapp.CustomExceptions.UserNotFoundException" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="org.example.quizwebapp.Model.User" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link href="CSS/UserPage.css" rel="stylesheet">
    <title>User Registration</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

<main>
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


    <div class="left-column">
        <section class="saxeli">
            <fieldset>
                <legend><em>Main Box</em></legend>
<%
                String token = (String) request.getSession().getAttribute("token");
                String userName = JwtUtil.extractUsername(token);
%>
                <h2><%= userName %></h2>
                <button type="button">Send Request</button>
            </fieldset>

            <fieldset>
                <legend><em>Achievements</em></legend>
                <%
                    UserDAO dao = new UserDAO();
                    List<Achievement> achievements = dao.getAchievements(userName);
                    if (achievements.isEmpty()) {
                %>
                <p>User doesn't have any achievements.</p>
                <%
                } else {
                    for (Achievement achievement : achievements) {
                %>
                <h3><%= achievement.getName() %></h3>
                <p><%= achievement.getDescription() %></p>
                <img src="<%= achievement.getImg() %>" alt="<%= achievement.getName() %> Image">
                <%
                        }
                    }
                %>
            </fieldset>
        </section>


        <section class="friends">
            <fieldset>
                <legend><em>Friends</em></legend>
                <ul>
                    <%
                        List<User> friends = null;
                        try {
                            friends = dao.getFriendList(userName);
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                        if (friends.isEmpty()) {
                    %>
                    <li>User doesn't have friends</li>
                    <%
                    } else {
                        for (User friend : friends) {
                    %>
                    <li>
                        <a href="Profile.jsp?profileUser=<%= friend.getUsername() %>">
                            <%= friend.getUsername() %>
                        </a>
                    </li>
                    <%
                            }
                        }
                    %>
                </ul>
            </fieldset>
        </section>


    </div>
</main>

</body>
</html>
