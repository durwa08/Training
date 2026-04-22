package restaurantportal.dto;
import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;

    @jakarta.validation.constraints.Email
    private String email;

    private String password;
    private String phoneNumber;
    private String role;
}
