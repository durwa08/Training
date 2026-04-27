package restaurantportal.dto;

// CategoryResponse is used to send category data back to client after creating/updating/getting category
public class CategoryResponse {

    private Long id;
    private String name;
    private Long restaurantId;

    public CategoryResponse(Long id, String name, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.restaurantId = restaurantId;
    }

    // getters
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Long getRestaurantId() {
        return restaurantId;
    }
}