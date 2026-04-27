package restaurantportal.dto;

/**
 * Response DTO used to send restaurant details back to the client.
 */
public class RestaurantResponse {

    private Long id;
    private String name;
    private String address;
    private String status;

    /**
     * Creates a RestaurantResponse with all restaurant details.
     */
    public RestaurantResponse(Long id, String name, String address, String status) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.status = status;
    }

    /**
     * Returns restaurant ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Returns restaurant name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns restaurant address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns restaurant status.
     */
    public String getStatus() {
        return status;
    }
}