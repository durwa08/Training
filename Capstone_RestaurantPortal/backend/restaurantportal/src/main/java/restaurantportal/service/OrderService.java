package restaurantportal.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurantportal.dto.*;
import restaurantportal.entity.*;
import restaurantportal.repository.*;
import restaurantportal.security.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service responsible for order processing logic including placement,
 * preview, status updates, and cancellation.
 */
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }


    /**
     * Generates a preview of the current cart before placing an order.
     */
    public OrderResponse previewOrder() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        return new OrderResponse(
                null,
                cart.getTotalAmount(),
                "PREVIEW",
                cart.getItems().stream()
                        .map(i -> new OrderItemResponse(
                                i.getId(),
                                i.getMenuItem().getName(),
                                i.getPrice(),
                                i.getQuantity()
                        ))
                        .toList(),
                "Success",
                LocalDateTime.now().toString(),
                null,
                null
        );
    }

    /**
     * Places an order by converting cart items into an order
     * and deducting wallet balance.
     */
    @Transactional
    public OrderResponse placeOrder(PlaceOrderRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        if (user.getWalletBalance() < cart.getTotalAmount()) {
            throw new RuntimeException("Insufficient wallet balance");
        }

        user.setWalletBalance(user.getWalletBalance() - cart.getTotalAmount());
        userRepository.save(user);

        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setPhoneNumber(request.getPhoneNumber());

        Restaurant restaurant = cart.getItems().get(0)
                .getMenuItem()
                .getCategory()
                .getRestaurant();

        order.setRestaurant(restaurant);

        for (CartItem c : cart.getItems()) {
            OrderItem item = new OrderItem();
            item.setName(c.getMenuItem().getName());
            item.setPrice(c.getPrice());
            item.setQuantity(c.getQuantity());
            item.setMenuItem(c.getMenuItem());
            item.setOrder(order);

            order.getItems().add(item);
        }

        cart.getItems().clear();
        cart.setTotalAmount(0.0);
        cartRepository.save(cart);

        Order saved = orderRepository.save(order);

        return mapToResponse(saved);
    }

    /**
     * Fetches all orders placed by the logged-in user.
     */
    public List<OrderResponse> getMyOrders() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Fetches all orders for restaurants owned by the logged-in user.
     */
    public List<OrderResponse> getOwnerOrders() {

        String email = SecurityUtil.getCurrentUserEmail();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByRestaurantOwnerId(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateStatusFlow(OrderStatus current, OrderStatus next) {

        if (current == OrderStatus.PENDING && next == OrderStatus.DELIVERED) return;
        if (current == OrderStatus.DELIVERED && next == OrderStatus.COMPLETED) return;
        if (next == OrderStatus.CANCELLED) return;

        throw new RuntimeException("Invalid status transition");
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, String status) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        if (!order.getRestaurant().getOwner().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You are not allowed to update this order");
        }

        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        validateStatusFlow(order.getStatus(), newStatus);

        order.setStatus(newStatus);

        return mapToResponse(orderRepository.save(order));
    }

    @Transactional
    public String cancelOrder(Long orderId) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Not your order");
        }

        if (!order.getStatus().name().equals("PENDING")) {
            throw new RuntimeException("Cannot cancel now");
        }

        LocalDateTime now = LocalDateTime.now();
        long seconds = java.time.Duration.between(order.getCreatedAt(), now).getSeconds();

        if (seconds > 30) {
            throw new RuntimeException("Cancellation time expired (30 seconds only)");
        }

        user.setWalletBalance(user.getWalletBalance() + order.getTotalAmount());
        order.setStatus(OrderStatus.CANCELLED);

        userRepository.save(user);
        orderRepository.save(order);

        return "Order cancelled & refunded";
    }

    private OrderResponse mapToResponse(Order order) {

        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus().name(),
                order.getItems().stream()
                        .map(i -> new OrderItemResponse(
                                i.getId(),
                                i.getName(),
                                i.getPrice(),
                                i.getQuantity()
                        )).toList(),
                "Success",
                order.getCreatedAt().toString(),
                order.getDeliveryAddress(),
                order.getPhoneNumber()
        );
    }
}