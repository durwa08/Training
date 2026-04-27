package restaurantportal.dto;

import jakarta.validation.constraints.NotBlank;
//CategoryRequest is used to receive data from client when creating/updating category
public class CategoryRequest {

    @NotBlank
    private String name;

    // getters & setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}