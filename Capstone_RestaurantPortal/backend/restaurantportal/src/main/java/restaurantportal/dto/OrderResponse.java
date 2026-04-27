package restaurantportal.dto;

import java.util.List;

/**
 * Response DTO representing complete order details returned to the client.
 */
public class OrderResponse {

    private Long orderId;
    private double totalAmount;
    private String status;

    private List<OrderItemResponse> items;

    private String statusMessage;
    private String createdAt;

    /**
     * Creates an OrderResponse with all order details.
     */
    public OrderResponse(Long orderId, double totalAmount, String status,
                         List<OrderItemResponse> items,
                         String statusMessage,
                         String createdAt) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
        this.statusMessage = statusMessage;
        this.createdAt = createdAt;
    }

    /**
     * Returns order ID.
     */
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * Returns total order amount.
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Returns current order status.
     */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns list of items in the order.
     */
    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    /**
     * Returns additional status message for the order.
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * Returns order creation timestamp.
     */
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}