package restaurantportal.dto;

/**
 * Request DTO for placing an order with delivery details.
 */
public class PlaceOrderRequest {
    /**
     * The delivery address for the order.
     */
    private String deliveryAddress;
    /**
     * The contact phone number for the order.
     */
    private String phoneNumber;

    /**
     * Returns the delivery address for the order.
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }
/**
     * Sets the delivery address for the order.
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Returns the contact phone number for the order.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }
    /**
     * Sets the contact phone number for the order.
     */

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}