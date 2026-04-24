package restaurantportal.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// user response is used to send user data back to client.
// It includes id, first name, last name, email, phone number, role and wallet balance.
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String role;
    private Double walletBalance;
}