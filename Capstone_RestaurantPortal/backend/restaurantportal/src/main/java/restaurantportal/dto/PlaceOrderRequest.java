package restaurantportal.dto;

/**
 * Request DTO for placing an order with delivery details.
 */
public class PlaceOrderRequest {
    /**
     * The delivery address for the order.
     */
    private String deliveryAddress;
    private String phoneNumber;

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}