<%@ page import="java.sql.*" %>
<%@ page import="org.example.quizwebapp.DAO.ConnectionPool" %>
<%@ page import="org.example.quizwebapp.Utils.JwtUtil" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <link href="CSS/takeQuiz.css" rel="stylesheet">
    <title>Take Quiz</title>
</head>
<body>
<nav>
    <ul class="nav-ul">
        <li class="nav-li"><a href="#"><img class="logo" src="image/logo.jpg"></a></li>
        <li class="nav-li"><a href="#"><i class="fas fa-user nav-icon"></i>profile</a></li>
        <li class="nav-li"><a href="#"><i class="fas fa-pencil-alt nav-icon"></i>create quiz</a></li>
    </ul>

    <section class="search">
        <input type="text" placeholder="quizzes, users ..." class="search-field">
        <button type="button" class="search-button"><i class="fas fa-search"></i></button>
    </section>
</nav>
<section class = "q-body">
    <%
        ConnectionPool pool = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String token = (String) request.getSession().getAttribute("token");
        String userName = JwtUtil.extractUsername(token);

        try {
            pool = ConnectionPool.getInstance();
            conn = pool.getConnection();

            // Fetch quiz details
            String quizQuery = "SELECT quiz_name, quiz_desc, creator_name FROM quizzes WHERE quiz_id = ?";
            stmt = conn.prepareStatement(quizQuery);
            stmt.setInt(1, quizId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String quizName = rs.getString("quiz_name");
                String quizDesc = rs.getString("quiz_desc");
                String creatorName = rs.getString("creator_name");

    %>
    <h1><%= quizName %></h1>
    <section class = "q-desc">
        <p><%= quizDesc %></p>
        <p><strong>Created by:</strong> <%= creatorName %></p>
    </section>
    <section class = "q-questions">
        <form action="submitQuiz" method="post">
            <input type="hidden" name="quizId" value="<%= quizId %>">
            <input type="hidden" name="userName" value="<%= userName %>">
            <%
                // Fetch questions and answers
                String questionQuery = "SELECT q.question_id, q.question_text, a.answer_id, a.answer_text, a.answer_type " +
                        "FROM questions q " +
                        "JOIN answers a ON q.question_id = a.question_id " +
                        "WHERE q.quiz_id = ? " +
                        "ORDER BY q.question_id, a.answer_id";
                stmt = conn.prepareStatement(questionQuery);
                stmt.setInt(1, quizId);
                rs = stmt.executeQuery();

                int currentQuestionId = -1;
                while (rs.next()) {
                    int questionId = rs.getInt("question_id");
                    String questionText = rs.getString("question_text");
                    int answerId = rs.getInt("answer_id");
                    String answerText = rs.getString("answer_text");

                    if (questionId != currentQuestionId) {
                        if (currentQuestionId != -1) {
            %>
            </ul>
            </div>
            <%
                }
                currentQuestionId = questionId;
            %>
            <div class="question">
                <p><strong><%= questionText %></strong></p>
                <ul>
                    <%
                        }
                    %>
                    <li>
                        <input type="checkbox" name="question_<%= questionId %>" value="<%= answerId %>" id="answer_<%= answerId %>">
                        <label for="answer_<%= answerId %>"><%= answerText %></label>
                    </li>
                    <%
                        }
                        if (currentQuestionId != -1) {
                    %>
                </ul>
            </div>
            <%
                }
            %>
            <button class = "q-button" type="submit">Submit Quiz</button>
        </form>
        <%
        } else {
        %>
        <p>Quiz not found.</p>
        <%
            }
        } catch (Exception e) {
            e.printStackTrace();
        %>
        <p>Error retrieving quiz details. Please try again later.</p>
        <%
            } finally {
                if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
                if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
                if (conn != null) {
                    try {
                        ConnectionPool.releaseConnection(conn);
                    } catch (Exception ignore) {}
                }
            }
        %>
    </section>
</section>
</body>
</html>
