package org.example.quizwebapp.Servlet;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quizwebapp.CustomExceptions.UserAlreadyExistsException;
import org.example.quizwebapp.DAO.UserDAO;
import org.example.quizwebapp.Model.User;
import org.example.quizwebapp.Utils.JwtUtil;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {
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
            userDAO.addUser(user);
            String token = JwtUtil.generateToken(username);

            request.getSession().setAttribute("token", token);
            response.setHeader("Authorization", "Bearer " + token);
            request.getSession().setAttribute("username", user.getUsername());
            response.sendRedirect("registration-success.jsp");
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMessage", "Database error: " + e.getMessage());
            response.sendRedirect("registration-failure.jsp");
        } catch (UserAlreadyExistsException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
            response.sendRedirect("registration-failure.jsp");
        } catch (Exception e) {
            request.getSession().setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
            response.sendRedirect("registration-failure.jsp");
        }
    }
}

