package restaurantportal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Response DTO representing a single item inside the user's cart.
 */
@Data
@AllArgsConstructor
public class CartItemResponse {

    /**
     * Unique identifier of the cart item.
     */
    private Long id;

    /**
     * Name of the menu item.
     */
    private String name;

    /**
     * Price of the menu item.
     */
    private Double price;

    /**
     * Quantity of the item added to cart.
     */
    private int quantity;
}