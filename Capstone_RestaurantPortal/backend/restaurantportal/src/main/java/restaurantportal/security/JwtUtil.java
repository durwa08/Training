package restaurantportal.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;


// JwtUtil is a utility class that useful for creating and validating the token and used for stateless authentication
@Component
public class JwtUtil {

    // It is used to sign the token and should be kept secret and secure.
    @Value("${jwt.secret}")
    private String SECRET;

    // Token validation in Application.properties we set the expiration for 1 day.
    @Value("${jwt.expiration}")
    private long EXPIRATION;


    // it converts string to cryptographic key that can be used for signing and verifying token.
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }
// Token Generation - it takes email and role as input and creates a JWT token with these details, sets the issued time and expiration time, and signs it using the secret key.
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)// stores email as identity
                .claim("role", role)//stores role as claim
                .setIssuedAt(new Date())// sets the time when token is issued.
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))//sets the time when token will expire.
                .signWith(getSignKey(), SignatureAlgorithm.HS256)//Signs token using secret key and HS256 algorithm.
                .compact();//converts to final jwt string
        // Final token consists of 3 parts header.payload.signature ,header contains algo info payload email role and expiry and signature security verification
    }

    //Extract email and used by jwt filer to get user details and authenticate the user.
    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // here token validation is checked.if parsing fails token invalid
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    //uses secret key verify signature and extracts payload

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}