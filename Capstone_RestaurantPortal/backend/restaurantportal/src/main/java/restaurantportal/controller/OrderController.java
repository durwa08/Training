package restaurantportal.controller;

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
        return service.previewOrder();
    }

    /**
     * Places a new order (checkout process).
     */
    @PostMapping
    public OrderResponse placeOrder(@RequestBody PlaceOrderRequest request) {
        return service.placeOrder(request);
    }

    /**
     * Fetches all orders of the logged-in user.
     */
    @GetMapping
    public List<OrderResponse> getMyOrders() {
        return service.getMyOrders();
    }

    /**
     * Fetches all orders for the restaurant owner.
     */
    @GetMapping("/owner")
    public List<OrderResponse> getOwnerOrders() {
        return service.getOwnerOrders();
    }

    /**
     * Updates the status of an existing order.
     */
    @PutMapping("/{id}")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @RequestParam String status) {
        return service.updateStatus(id, status);
    }

    /**
     * Cancels an order and processes refund if applicable.
     */
    @PutMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id) {
        return service.cancelOrder(id);
    }
}