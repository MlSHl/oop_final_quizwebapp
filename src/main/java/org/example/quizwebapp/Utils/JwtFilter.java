package org.example.quizwebapp.Utils;

import io.jsonwebtoken.*;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter({"/main.jsp", "/create-quiz/*", "/friend-request/*"})
public class JwtFilter implements Filter {

    private static final String SECRET_KEY = "27baef690b2ceb875b2bb0319f2a05daba6b4f9babb345555db68d01b6e51ba81e58d84984d840aea83250319807c5c1e9b202a35ae85a2fb757903565b7be41";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Get JWT token from local storage
        String jwtToken = (String) request.getSession().getAttribute("token");

        // Validate token
        if (jwtToken != null) {
            try {
                // Validate and parse the token
                Claims claims = Jwts.parser()
                        .setSigningKey(SECRET_KEY)
                        .parseClaimsJws(jwtToken)
                        .getBody();

                // Proceed with the request chain if token is valid
                chain.doFilter(req, res);

            } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
                // Token validation failed, handle accordingly (e.g., redirect to login)
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized access: " + e.getMessage());
            }
        } else {
            // No token found, deny access
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access: No valid JWT token provided");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization code
    }

    @Override
    public void destroy() {
        // Cleanup code
    }
}