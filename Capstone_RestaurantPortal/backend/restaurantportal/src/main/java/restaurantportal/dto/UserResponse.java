package restaurantportal.dto;

import lombok.*;

/**
 * Response DTO used to send user details back to the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    /**
     * Unique user ID.
     */
    private Long id;

    /**
     * First name of the user.
     */
    private String firstName;

    /**
     * Last name of the user.
     */
    private String lastName;

    /**
     * Email address of the user.
     */
    private String email;

    /**
     * Phone number of the user.
     */
    private String phoneNumber;

    /**
     * Role of the user (e.g., customer, admin).
     */
    private String role;

    /**
     * Wallet balance of the user.
     */
    private Double walletBalance;
}