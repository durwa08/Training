package restaurantportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * Request DTO used for creating or updating a menu item.
 */
public class MenuItemRequest {

    /**
     * Name of the menu item.
     */
    @NotBlank
    private String name;

    /**
     * Price of the menu item.
     */
    @Positive
    private double price;

    /**
     * Returns the name of the menu item.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the menu item.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the price of the menu item.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the price of the menu item.
     */
    public void setPrice(double price) {
        this.price = price;
    }
}