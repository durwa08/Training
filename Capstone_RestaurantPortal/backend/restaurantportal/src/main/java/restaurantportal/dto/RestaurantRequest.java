package restaurantportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO used for creating or updating a restaurant.
 */
public class RestaurantRequest {

    /**
     * Name of the restaurant.
     */
    @NotBlank
    private String name;

    /**
     * Address of the restaurant.
     */
    @NotBlank
    private String address;

    /**
     * Status of the restaurant (will be converted to enum in service layer).
     */
    @NotNull
    private String status;

    /**
     * Returns restaurant name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets restaurant name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns restaurant address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets restaurant address.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Returns restaurant status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets restaurant status.
     */
    public void setStatus(String status) {
        this.status = status;
    }
}