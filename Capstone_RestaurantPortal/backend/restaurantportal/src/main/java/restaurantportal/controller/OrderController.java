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

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService service;

    // Constructor injection
    public OrderController(OrderService service) {
        this.service = service;
    }

    /**
     * Returns a preview of the current order before checkout.
     */
    @GetMapping("/preview")
    public OrderResponse preview() {

        log.info("Fetching order preview");

        OrderResponse response = service.previewOrder();

        log.info("Order preview fetched successfully");

        return response;
    }

    /**
     * Places a new order (checkout process).
     */
    @PostMapping
    public OrderResponse placeOrder(@RequestBody PlaceOrderRequest request) {

        log.info("Placing new order: {}", request);

        OrderResponse response = service.placeOrder(request);

        log.info("Order placed successfully");

        return response;
    }

    /**
     * Fetches all orders of the logged-in user.
     */
    @GetMapping
    public List<OrderResponse> getMyOrders() {

        log.info("Fetching orders for current user");

        List<OrderResponse> orders = service.getMyOrders();

        log.info("Fetched {} orders for user", orders.size());

        return orders;
    }

    /**
     * Fetches all orders for the restaurant owner.
     */
    @GetMapping("/owner")
    public List<OrderResponse> getOwnerOrders() {

        log.info("Fetching owner orders");

        List<OrderResponse> orders = service.getOwnerOrders();

        log.info("Fetched {} owner orders", orders.size());

        return orders;
    }

    /**
     * Updates the status of an existing order.
     */
    @PutMapping("/{id}")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @RequestParam String status) {

        log.info("Updating order id: {} to status: {}", id, status);

        OrderResponse response = service.updateStatus(id, status);

        log.info("Order status updated successfully for id: {}", id);

        return response;
    }

    /**
     * Cancels an order and processes refund if applicable.
     */
    @PutMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id) {

        log.info("Cancelling order id: {}", id);

        String response = service.cancelOrder(id);

        log.info("Order cancelled successfully for id: {}", id);

        return response;
    }
}