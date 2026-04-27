package restaurantportal.dto;
// MenuItemResponse is used to send menu item data back to client.
//  menu item response will contain id, name, price and categoryId.
//  We will use this class to send menu item data back to client after creating/updating/getting menu item.
public class MenuItemResponse {

    private Long id;
    private String name;
    private double price;
    private Long categoryId;
//This constructor is used to create MenuItemResponse object with all fields
    public MenuItemResponse(Long id, String name, double price, Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.categoryId = categoryId;
    }
// getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public Long getCategoryId() { return categoryId; }
}