package com.recipe_maker.backend.components;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.recipe_maker.backend.authentication.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter for JWT authentication. Intercepts incoming HTTP requests, 
 * extracts the JWT token from the Authorization header,
 * validates it, and sets the authentication in the security context 
 * if the token is valid.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * The JWT service for handling JWT operations.
     */
    private final JwtService jwtService;

    /**
     * The UserDetailsService for loading user details.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Constructor for JwtAuthenticationFilter.
     * 
     * @param jwtService The JWT service to be used for token validation and extraction.
     * @param userDetailsService The UserDetailsService to load user details based on the username extracted from the token.
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming HTTP requests to perform JWT authentication. It checks for 
     * the presence of a Bearer token in the Authorization header, validates the token, 
     * and if valid, sets the authentication in the security context.
     * 
     * @param request The incoming HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain to pass the request and response to the next filter in the chain.
     * @throws ServletException If an error occurs during filtering.
     * @throws IOException If an I/O error occurs during filtering.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Extract the Authorization header from the incoming request
        String authHeader = request.getHeader("Authorization");
        
        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            /*
             * If the Authorization header is missing or does not start with "Bearer ",
             * we simply pass the request and response to the next filter in the chain 
             * without setting any authentication.
             */
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token from the Authorization header by removing the "Bearer " prefix
        String token = authHeader.substring(7);

        /*
         * Check if the JWT token is valid. If the token is valid, we extract the username 
         * from the token, load the user details using the UserDetailsService, and create 
         * an authentication token. We then set this authentication in the security context.
         */
        if (jwtService.isTokenValid(token)) {
            // Extract the username from the valid JWT token
            String username = jwtService.extractUsername(token);

            // Load the user details based on the extracted username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            /*
             * Create an authentication token using the user details and set it in the 
             * security context.
             */
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        // Pass the request and response to the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
