package restaurantportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//  register request is used to receive user information from client when user tries to register.
//  It includes first name, last name, email, password, phone number and role (customer/employee/admin).
public class RegisterRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @Size(min = 5)
    private String password;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String role;
}