package restaurantportal.dto;

public class OrderItemResponse {

    private Long id;
    private String menuItemName;
    private int quantity;
    private double price;

    public OrderItemResponse(Long id, String menuItemName, double price, int quantity) {
        this.id = id;
        this.menuItemName = menuItemName;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public String getMenuItemName() { return menuItemName; }
    public int getQuantity() { return quantity; }
    public double getPrice() { return price; }
}