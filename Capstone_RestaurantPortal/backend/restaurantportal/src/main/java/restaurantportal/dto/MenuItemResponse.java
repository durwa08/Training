package restaurantportal.dto;

/**
 * Response DTO used to send menu item details back to the client.
 */
public class MenuItemResponse {

    private Long id;
    private String name;
    private double price;
    private Long categoryId;

    /**
     * Creates a MenuItemResponse with all required fields.
     */
    public MenuItemResponse(Long id, String name, double price, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }

    /**
     * Returns menu item ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns menu item name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns menu item price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns category ID of the menu item.
     */
    public Long getCategoryId() {
        return categoryId;
    }
}