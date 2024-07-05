package org.example.quizwebapp.Servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quizwebapp.DAO.UserDAO;
import org.example.quizwebapp.Model.User;

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
            response.sendRedirect("registration-success.jsp");
        } catch (Exception e) {
            throw new ServletException("Error adding user", e);
        }
    }
}
