package restaurantportal.dto;

import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * Request payload for adding money to wallet.
 */
@Data
public class AddMoneyRequest {

    @Min(value = 1, message = "Amount must be greater than 0")
    private double amount;
}