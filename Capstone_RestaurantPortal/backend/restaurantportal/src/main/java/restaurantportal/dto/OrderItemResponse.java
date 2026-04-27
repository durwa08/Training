package restaurantportal.dto;

/**
 * Response DTO representing a single item inside an order.
 */
public class OrderItemResponse {

    private Long id;
    private String menuItemName;
    private int quantity;
    private double price;

    /**
     * Creates an OrderItemResponse with item details.
     */
    public OrderItemResponse(Long id, String menuItemName, double price, int quantity) {
        this.id = id;
        this.menuItemName = menuItemName;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Returns order item ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns menu item name.
     */
    public String getMenuItemName() {
        return menuItemName;
    }

    /**
     * Returns quantity of the item ordered.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns price of the item.
     */
    public double getPrice() {
        return price;
    }
}