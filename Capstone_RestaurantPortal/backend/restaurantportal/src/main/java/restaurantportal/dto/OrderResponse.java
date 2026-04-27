package restaurantportal.dto;

import java.util.List;

public class OrderResponse {

    private Long orderId;
    private double totalAmount;
    private String status;

    private List<OrderItemResponse> items;

    public OrderResponse(Long orderId, double totalAmount, String status, List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }

    public Long getOrderId() {
        return orderId;
    }
    public double getTotalAmount() {
        return totalAmount;
    }
    public String getStatus() {
        return status;
    }
    public List<OrderItemResponse> getItems() {
        return items;
    }
}