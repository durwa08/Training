package restaurantportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request DTO used for user registration.
 * It carries user details required to create a new account.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    /**
     * First name of the user.
     */
    @NotBlank
    private String firstName;

    /**
     * Last name of the user.
     */
    @NotBlank
    private String lastName;

    /**
     * Email address of the user.
     */
    @Email
    @NotBlank
    private String email;

    /**
     * Password for the user account.
     */
    @Size(min = 5)
    private String password;

    /**
     * Phone number of the user.
     */
    @NotBlank
    private String phoneNumber;

    /**
     * Role of the user (e.g., customer, admin, employee).
     */
    @NotBlank
    private String role;
}