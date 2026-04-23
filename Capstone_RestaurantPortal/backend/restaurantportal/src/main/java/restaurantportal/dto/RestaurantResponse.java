package restaurantportal.dto;

// RestaurantResponse is used to send restaurant data back to client.
public class RestaurantResponse {

    private Long id;
    private String name;
    private String address;
    private String status;

    public RestaurantResponse(Long id, String name, String address, String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.status = status;
    }

    // getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getStatus() { return status; }
}