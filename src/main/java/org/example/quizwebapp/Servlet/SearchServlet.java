package org.example.quizwebapp.Servlet;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SearchServlet")
public class SearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");

        // Perform your search logic here
        // For demonstration purposes, let's just print the search query to the console
        System.out.println("Search Query: " + searchQuery);

        // Forward the request and response to a JSP page to display the search results
        request.setAttribute("searchQuery", searchQuery);
        request.getRequestDispatcher("/searchResults.jsp").forward(request, response);
    }
}
