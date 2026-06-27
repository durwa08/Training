package restaurantportal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.OrderResponse;
import restaurantportal.dto.PlaceOrderRequest;
import restaurantportal.service.OrderService;

import java.util.List;

/**
 * Handles order-related operations like placing, viewing, and updating orders.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService service;

    // Constructor injection
    public OrderController(OrderService service) {
        this.service = service;
        logger.info("OrderController initialized");
    }

    /**
     * Returns a preview of the current order before checkout.
     */
    @GetMapping("/preview")
    public OrderResponse preview() {
        logger.info("Received request for order preview");
        OrderResponse response = service.previewOrder();
        logger.info("Order preview fetched successfully");
        return response;
    }

    /**
     * Places a new order (checkout process).
     */
    @PostMapping
    public OrderResponse placeOrder(@RequestBody PlaceOrderRequest request) {
        logger.info("Received request to place order");
        logger.debug("PlaceOrderRequest: {}", request);
        OrderResponse response = service.placeOrder(request);
        logger.info("Order placed successfully");
        return response;
    }

    /**
     * Fetches all orders of the logged-in user.
     */
    @GetMapping
    public List<OrderResponse> getMyOrders() {
        logger.info("Received request to fetch user orders");
        List<OrderResponse> response = service.getMyOrders();
        logger.info("User orders fetched successfully");
        return response;
    }

    /**
     * Fetches all orders for the restaurant owner.
     */
    @GetMapping("/owner")
    public List<OrderResponse> getOwnerOrders() {
        logger.info("Received request to fetch owner orders");
        List<OrderResponse> response = service.getOwnerOrders();
        logger.info("Owner orders fetched successfully");
        return response;
    }

    /**
     * Updates the status of an existing order.
     */
    @PutMapping("/{id}")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @RequestParam String status) {
        logger.info("Received request to update order status for id: {} to status: {}", id, status);
        OrderResponse response = service.updateStatus(id, status);
        logger.info("Order status updated successfully for id: {}", id);
        return response;
    }

    /**
     * Cancels an order and processes refund if applicable.
     */
    @PutMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id) {
        logger.info("Received request to cancel order with id: {}", id);
        String response = service.cancelOrder(id);
        logger.info("Order cancelled successfully with id: {}", id);
        return response;
    }
}