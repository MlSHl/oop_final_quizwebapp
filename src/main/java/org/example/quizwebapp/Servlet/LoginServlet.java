package org.example.quizwebapp.Servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quizwebapp.CustomExceptions.UserAlreadyExistsException;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.DAO.UserDAO;
import org.example.quizwebapp.Model.Achievement;
import org.example.quizwebapp.Model.User;
import org.example.quizwebapp.Utils.JwtUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = new User(username, password);

        try {
            User loggedInUser = userDAO.getUser(user);
            if (loggedInUser != null) {
                String token = JwtUtil.generateToken(username);

                // Logging token and user info
                System.out.println("Generated Token: " + token);
                System.out.println("LoggedIn User: " + loggedInUser.getUsername());

                // Set the token and username in the session
                request.getSession().setAttribute("token", token);
                response.setHeader("Authorization", "Bearer " + token);
                request.getSession().setAttribute("username", loggedInUser.getUsername());

                // Fetch achievements
                List<Achievement> achievements = userDAO.getMyAchievements(token);
                System.out.println("Fetched Achievements: " + achievements);

                // Validate achievements before setting them
                if (achievements != null) {
                    request.setAttribute("achievements", achievements);
                    System.out.println(achievements);
                } else {
                    System.out.println("No achievements found.");
                }

                // Forward to login-success.jsp
                request.getRequestDispatcher("login-success.jsp").forward(request, response);

                // Remove unnecessary sendRedirect
                // response.sendRedirect("login-success.jsp"); // This line is removed
            } else {
                throw new UserNotFoundException("User not found for username " + username);
            }
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Database error: " + e.getMessage());
            response.sendRedirect("login-failure.jsp");
        } catch (UserNotFoundException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect("login-failure.jsp");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            response.sendRedirect("login-failure.jsp");
        }
    }

}