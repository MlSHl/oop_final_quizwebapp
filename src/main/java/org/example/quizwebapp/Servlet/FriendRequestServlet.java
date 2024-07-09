package org.example.quizwebapp.Servlet;

import org.example.quizwebapp.CustomExceptions.RequestAlreadyExists;
import org.example.quizwebapp.CustomExceptions.UserNotFoundException;
import org.example.quizwebapp.DAO.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.quizwebapp.Utils.JwtUtil;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/FriendRequestServlet")
public class FriendRequestServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = (String) request.getSession().getAttribute("token");
        String currentUser = JwtUtil.extractUsername(token);
        String friendUsername = request.getParameter("friendUsername");

        UserDAO userDAO = new UserDAO();
        try {
            userDAO.requestFriendship(userDAO.getUserByUsername(currentUser), userDAO.getUserByUsername(friendUsername));
            response.sendRedirect("UserPage.jsp?profileUser=" + friendUsername);
        } catch (SQLException e) {
            throw new ServletException("Unable to process friend request", e);
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (RequestAlreadyExists e) {
            throw new RuntimeException(e);
        }
    }
}