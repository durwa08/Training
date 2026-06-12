package restaurantportal.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class responsible for JWT generation, parsing, and validation.
 * Handles secure token creation and extraction of user information.
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private long EXPIRATION;

    /**
     * Generates cryptographic signing key from secret.
     *
     */
    private Key getSignKey() {
        logger.debug("Generating signing key");
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * Generates JWT token for authenticated user.
     */
    public String generateToken(String email, String role, Long userId) {

        logger.info("Generating JWT token for email: {}", email);

        String token = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();

        logger.debug("JWT token generated successfully");

        return token;
    }

    /**
     * Extracts email (subject) from JWT token.
     *
     */
    public String extractEmail(String token) {

        logger.debug("Extracting email from JWT");
        String email = getClaims(token).getSubject();
        logger.debug("Extracted email: {}", email);
        return email;
    }

    /**
     * Extracts role from JWT token.
     */
    public String extractRole(String token) {

        logger.debug("Extracting role from JWT");
        String role = getClaims(token).get("role", String.class);
        logger.debug("Extracted role: {}", role);
        return role;
    }

    /**
     * Validates JWT token for integrity and expiration.
     */
    public boolean isTokenValid(String token) {

        logger.debug("Validating JWT token");
        try {
            Claims claims = getClaims(token);
            boolean valid = claims.getExpiration().after(new Date());

            if (valid) {
                logger.debug("JWT token is valid");
            } else {
                logger.warn("JWT token expired");
            }

            return valid;
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Invalid JWT token", e);
            return false;
        }
    }

    /**
     * Parses JWT token and returns claims.
     */
    private Claims getClaims(String token) {

        logger.debug("Parsing JWT claims");
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}