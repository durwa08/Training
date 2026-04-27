package restaurantportal.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * Response DTO returned after successful login.
 * It contains the JWT token for authentication.
 */
@Getter
@AllArgsConstructor
public class LoginResponse {

    /**
     * JWT authentication token.
     */
    private String token;
}