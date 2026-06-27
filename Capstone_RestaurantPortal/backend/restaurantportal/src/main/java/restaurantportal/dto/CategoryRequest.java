package restaurantportal.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO used for creating or updating a category.
 */
public class CategoryRequest {

    /**
     * Name of the category.
     */
    @NotBlank
    private String name;

    /**
     * Returns the category name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the category name.
     */
    public void setName(String name) {
        this.name = name;
    }
}