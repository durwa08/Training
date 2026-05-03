package restaurantportal.service;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import restaurantportal.dto.OrderItemResponse;
import restaurantportal.dto.OrderResponse;
import restaurantportal.dto.PlaceOrderRequest;
import restaurantportal.entity.Cart;
import restaurantportal.entity.CartItem;
import restaurantportal.entity.Order;
import restaurantportal.entity.OrderItem;
import restaurantportal.entity.OrderStatus;
import restaurantportal.entity.Restaurant;
import restaurantportal.entity.User;
import restaurantportal.repository.CartRepository;
import restaurantportal.repository.OrderRepository;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;

/**
 * Service responsible for order processing logic including placement,
 * preview, status updates, and cancellation.
 */
@Service
public class OrderService {
    /**
     * OrderService handles all operations related to order processing.
     * It manages an order from preview to placement, status updates, and cancellation.
     */
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository,
                        CartRepository cartRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        logger.info("OrderService initialized");
    }


    /**
     * Generates a preview of the current cart before placing an order.
     */
    public OrderResponse previewOrder() {

        logger.info("Generating order preview");

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> {
                    logger.error("Cart not found for user");
                    return new RuntimeException("Cart not found");
                });

        if (cart.getItems().isEmpty()) {
            logger.warn("Cart is empty for preview");
            throw new RuntimeException("Cart is empty");
        }

        String customerName = String.join(" ",
                user.getFirstName() == null ? "" : user.getFirstName(),
                user.getLastName() == null ? "" : user.getLastName()).trim();
        if (customerName.isEmpty()) {
            customerName = user.getEmail();
        }

        logger.info("Order preview generated successfully");

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
                customerName,
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

        logger.info("Placing order");
        logger.debug("PlaceOrderRequest: {}", request);

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> {
                    logger.error("Cart not found for user");
                    return new RuntimeException("Cart not found");
                });

        if (cart.getItems().isEmpty()) {
            logger.warn("Cart is empty while placing order");
            throw new RuntimeException("Cart is empty");
        }

        if (user.getWalletBalance() < cart.getTotalAmount()) {
            logger.warn("Insufficient wallet balance");
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

        logger.info("Order placed successfully with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    /**
     * Fetches all orders placed by the logged-in user.
     */
    public List<OrderResponse> getMyOrders() {

        logger.info("Fetching user orders");

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        List<OrderResponse> response = orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();

        logger.info("User orders fetched successfully");

        return response;
    }

    /**
     * Fetches all orders for restaurants owned by the logged-in user.
     */
    public List<OrderResponse> getOwnerOrders() {

        logger.info("Fetching owner orders");

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        List<OrderResponse> response = orderRepository.findByRestaurantOwnerId(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();

        logger.info("Owner orders fetched successfully");

        return response;
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, String status) {

        logger.info("Updating order status for id: {} to {}", orderId, status);

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found with id: {}", orderId);
                    return new RuntimeException("Order not found with ID: " + orderId);
                });

        if (!order.getRestaurant().getOwner().getId().equals(currentUser.getId())) {
            logger.error("Unauthorized status update attempt");
            throw new RuntimeException("You are not allowed to update this order");
        }

        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        validateStatusFlow(order.getStatus(), newStatus);

        order.setStatus(newStatus);

        logger.info("Order status updated successfully");

        return mapToResponse(orderRepository.save(order));
    }

    @Transactional
    public String cancelOrder(Long orderId) {

        logger.info("Cancelling order with id: {}", orderId);

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found with id: {}", orderId);
                    return new RuntimeException("Order not found");
                });

        if (!order.getUser().getId().equals(user.getId())) {
            logger.error("Unauthorized cancel attempt");
            throw new RuntimeException("Not your order");
        }

        if (!order.getStatus().name().equals("PENDING")) {
            logger.warn("Order cannot be cancelled at this stage");
            throw new RuntimeException("Cannot cancel now");
        }

        LocalDateTime now = LocalDateTime.now();
        long seconds = java.time.Duration.between(order.getCreatedAt(), now).getSeconds();

        if (seconds > 30) {
            logger.warn("Cancellation time expired");
            throw new RuntimeException("Cancellation time expired (30 seconds only)");
        }

        user.setWalletBalance(user.getWalletBalance() + order.getTotalAmount());
        order.setStatus(OrderStatus.CANCELLED);

        userRepository.save(user);
        orderRepository.save(order);

        logger.info("Order cancelled and refunded successfully");

        return "Order cancelled & refunded";
    }

    private void validateStatusFlow(OrderStatus current, OrderStatus next) {

        logger.debug("Validating status transition from {} to {}", current, next);

        if (current == OrderStatus.PENDING && next == OrderStatus.DELIVERED) return;
        if (current == OrderStatus.DELIVERED && next == OrderStatus.COMPLETED) return;
        if (next == OrderStatus.CANCELLED) return;

        logger.error("Invalid status transition");
        throw new RuntimeException("Invalid status transition");
    }

    private OrderResponse mapToResponse(Order order) {

        logger.debug("Mapping Order to OrderResponse with id: {}", order.getId());

        String customerName = null;
        if (order.getUser() != null) {
            customerName = String.join(" ",
                    order.getUser().getFirstName() == null ? "" : order.getUser().getFirstName(),
                    order.getUser().getLastName() == null ? "" : order.getUser().getLastName()).trim();
            if (customerName.isEmpty()) {
                customerName = order.getUser().getEmail();
            }
        }

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
                customerName,
                order.getDeliveryAddress(),
                order.getPhoneNumber()
        );
    }
}