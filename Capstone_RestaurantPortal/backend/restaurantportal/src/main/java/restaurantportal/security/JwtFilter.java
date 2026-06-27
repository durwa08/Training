package restaurantportal.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT filter that intercepts every HTTP request to validate authentication.
 * Extracts JWT token from Authorization header, validates it,
<<<<<<< HEAD
 * and sets authenticated user in Spring Security context.
=======
 * and sets authenticated user.
>>>>>>> 3db4a825f3b8dea865a7181be8417c4599208f71
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructs JwtFilter with required dependencies.
     *
     */
    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        logger.info("JwtFilter initialized");
    }

    /**
     * Filters incoming requests and validates JWT token.
     * If token is valid, sets authentication in SecurityContext.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        logger.debug("Processing request: {} {}", request.getMethod(), request.getRequestURI());

        String header = request.getHeader("Authorization");

        String email = null;
        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            logger.debug("JWT token found in Authorization header");

            token = header.substring(7);

            try {
                email = jwtUtil.extractEmail(token);
                logger.debug("Extracted email from token: {}", email);
            } catch (Exception e) {
                logger.error("Error extracting email from JWT", e);
                filterChain.doFilter(request, response);
                return;
            }
        } else {
            logger.debug("No JWT token found in request");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            logger.debug("Setting authentication for user: {}", email);

            var userDetails = userDetailsService.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            logger.info("User authenticated successfully: {}", email);
        }

        filterChain.doFilter(request, response);
    }
}