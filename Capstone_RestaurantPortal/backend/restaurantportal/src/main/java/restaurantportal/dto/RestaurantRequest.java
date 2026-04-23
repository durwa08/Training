package restaurantportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// class RestaurantRequest is used to receive data from client when creating/updating restaurant
public class RestaurantRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @NotNull
    private String status; // we will convert to enum in service

    // getters & setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}