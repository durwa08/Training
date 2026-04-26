package restaurantportal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddToCartRequest {

    @NotNull(message = "Menu item ID cannot be null")
    private Long menuItemId;

    @NotNull(message = "Quantity cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    // Getters
    public Long getMenuItemId() {
        return menuItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    // Setters
    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}