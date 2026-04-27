package restaurantportal.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

// JWT filter to intercept incoming HTTP requests, extract the JWT token from the Authorization header, validate it, and set the authentication in the security context if the token is valid.
// This allows the application to authenticate users based on the JWT token they provide in their requests.
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        String email = null;
        String token = null;

        try {
            // extract token from header
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
                email = jwtUtil.extractEmail(token); // may throw exception
            }
        } catch (Exception e) {
            // invalid token → ignore and continue
            // request will be treated as unauthorized
        }

        // authenticate user if valid
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            var userDetails = userDetailsService.loadUserByUsername(email);

            if (token != null && jwtUtil.isTokenValid(token)) {

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // continue filter chain
        filterChain.doFilter(request, response);
    }
}