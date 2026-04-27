package restaurantportal.dto;

/**
 * Response DTO used to send category details back to the client.
 */
public class CategoryResponse {

    private Long id;
    private String name;
    private Long restaurantId;

    /**
     * Creates a response object for category details.
     */
    public CategoryResponse(Long id, String name, Long restaurantId) {
        this.id = id;
        this.name = name;
        this.restaurantId = restaurantId;
    }

    /**
     * Returns category ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns category name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the restaurant ID to which this category belongs.
     */
    public Long getRestaurantId() {
        return restaurantId;
    }
}