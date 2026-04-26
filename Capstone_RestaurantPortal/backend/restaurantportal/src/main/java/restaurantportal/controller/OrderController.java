package restaurantportal.controller;

import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.OrderResponse;
import restaurantportal.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    // ===================== PREVIEW ORDER =====================
    @GetMapping("/preview")
    public OrderResponse preview() {
        return service.previewOrder();
    }

    // ===================== PLACE ORDER =====================
    @PostMapping
    public OrderResponse placeOrder() {
        return service.placeOrder();
    }

    // ===================== GET MY ORDERS =====================
    @GetMapping
    public List<OrderResponse> getMyOrders() {
        return service.getMyOrders();
    }

    // ===================== UPDATE STATUS =====================
    @PutMapping("/{id}")
    public OrderResponse updateStatus(@PathVariable Long id,
                                      @RequestParam String status) {
        return service.updateStatus(id, status);
    }
}