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
     * Description of the menu item.
     */
    private String description;

    /**
     * Availability status of the menu item.
     */
    private Boolean available;

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

    /**
     * Returns the description of the menu item.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the menu item.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the availability status of the menu item.
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     * Sets the availability status of the menu item.
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }
}