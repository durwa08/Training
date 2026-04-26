package restaurantportal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurantportal.dto.*;
import restaurantportal.entity.*;
import restaurantportal.repository.*;
import restaurantportal.security.SecurityUtil;

import java.time.LocalDateTime;
import java.util.List;

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

    // ===================== PREVIEW ORDER =====================
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
                                i.getQuantity()))
                        .toList()
        );
    }

    // ===================== PLACE ORDER =====================
    @Transactional
    public OrderResponse placeOrder() {

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

        // deduct wallet
        user.setWalletBalance(
                user.getWalletBalance() - cart.getTotalAmount());

        // create order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        // SET RESTAURANT
        Restaurant restaurant = cart.getItems().get(0)
                .getMenuItem()
                .getCategory()
                .getRestaurant();

        order.setRestaurant(restaurant);

        // cart → order items
        for (CartItem c : cart.getItems()) {
            OrderItem item = new OrderItem();
            item.setName(c.getMenuItem().getName());
            item.setPrice(c.getPrice());
            item.setQuantity(c.getQuantity());
            item.setMenuItem(c.getMenuItem());
            item.setOrder(order);

            order.getItems().add(item);
        }

        // clear cart
        cart.getItems().clear();
        cart.setTotalAmount(0.0);

        Order saved = orderRepository.save(order);

        return mapToResponse(saved);
    }

    // ===================== GET MY ORDERS =====================
    public List<OrderResponse> getMyOrders() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===================== UPDATE STATUS =====================
    @Transactional
    public OrderResponse updateStatus(Long orderId, String status) {

        String email = SecurityUtil.getCurrentUserEmail();

        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // OWNER CHECK
        if (!order.getRestaurant().getOwner().getId()
                .equals(currentUser.getId())) {

            throw new RuntimeException("You are not allowed to update this order");
        }

        order.setStatus(OrderStatus.valueOf(status.toUpperCase()));

        return mapToResponse(orderRepository.save(order));
    }

    // ===================== MAPPER =====================
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
                                i.getQuantity()))
                        .toList());
    }
}