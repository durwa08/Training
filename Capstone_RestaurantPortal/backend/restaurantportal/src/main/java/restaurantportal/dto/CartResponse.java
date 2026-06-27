package restaurantportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Response DTO representing the complete cart details of a user.
 */
@Data
@AllArgsConstructor
public class CartResponse {

    /**
     * Unique identifier of the cart.
     */
    private Long cartId;

    /**
     * Total amount of all items in the cart.
     */
    private Double totalAmount;

    /**
     * List of items present in the cart.
     */
    private List<CartItemResponse> items;
}