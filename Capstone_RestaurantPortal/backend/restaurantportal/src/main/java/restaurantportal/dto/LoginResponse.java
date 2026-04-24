package restaurantportal.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
// login response is used to send token back to client after successful login.
// We will use JWT token for authentication, so we only need to send the token string back to client.
public class LoginResponse {

    private String token;
}