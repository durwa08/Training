package restaurantportal.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//  login request is used to receive email & password from client when user tries to login.
public class LoginRequest {

    private String email;
    private String password;
}