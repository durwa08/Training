package restaurantportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response DTO used to return wallet balance details to the client.
 */
@Data
@AllArgsConstructor
public class WalletResponse {

    /**
     * Current wallet balance of the user.
     */
    private double balance;
}