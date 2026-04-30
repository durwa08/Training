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
 * and sets authenticated user in Spring Security context.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructs JwtFilter with required dependencies.
     */
    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests and validates JWT token.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        String email = null;
        String token = null;

        log.debug("Incoming request: {} {}", request.getMethod(), request.getRequestURI());

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);

            try {
                email = jwtUtil.extractEmail(token);
                log.debug("JWT token parsed successfully");

            } catch (Exception e) {
                log.warn("Invalid JWT token received");
                filterChain.doFilter(request, response);
                return;
            }
        } else {
            log.debug("No Authorization header found");
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            log.info("Authenticating user: {}", email);

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

            log.info("User authenticated successfully: {}", email);
        }

        filterChain.doFilter(request, response);
    }
}