package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing an order placed by a user.
 * It contains order details, status, items, and relationships with user and restaurant.
 */
@Entity
@Getter
@Setter
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Delivery address where the order will be sent.
     */
    private String deliveryAddress;

    /**
     * Contact number for order communication.
     */
    private String phoneNumber;

    /**
     * User who placed the order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Restaurant from which the order is placed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    /**
     * Total amount of the order.
     */
    private double totalAmount;

    /**
     * Current status of the order (PLACED, PREPARING, DELIVERED, etc.).
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;;

    /**
     * Timestamp when the order was created.
     */
    private LocalDateTime createdAt;

    /**
     * List of items in the order.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
}