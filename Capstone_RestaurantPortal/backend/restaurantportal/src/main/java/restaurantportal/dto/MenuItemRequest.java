package restaurantportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

//This class is used to receive data from client when creating/updating menu item
public class MenuItemRequest {

    @NotBlank
    private String name;

    @Positive
    private double price;

    // getters & setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}