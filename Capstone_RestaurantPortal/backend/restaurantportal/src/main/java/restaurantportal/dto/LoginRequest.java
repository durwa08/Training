package restaurantportal.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request DTO used for user login.
 * It carries email and password from the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * Registered email of the user.
     */
    private String email;

    /**
     * Password of the user.
     */
    private String password;
}