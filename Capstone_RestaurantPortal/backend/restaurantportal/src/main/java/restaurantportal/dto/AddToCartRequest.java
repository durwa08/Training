package restaurantportal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO used for adding an item to the shopping cart.
 * It carries the menu item id and quantity selected by the user.
 */
public class AddToCartRequest {

    /**
     * ID of the menu item to be added to the cart.
     */
    @NotNull(message = "Menu item ID cannot be null")
    private Long menuItemId;

    /**
     * Quantity of the selected menu item.
     */
    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    /**
     * Returns the menu item ID.
     */
    public Long getMenuItemId() {
        return menuItemId;
    }

    /**
     * Sets the menu item ID.
     */
    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    /**
     * Returns the quantity of the item.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the item.
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}