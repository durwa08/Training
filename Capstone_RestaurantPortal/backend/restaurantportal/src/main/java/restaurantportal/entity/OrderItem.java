package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing an item inside an order.
 * It stores a snapshot of menu item details at the time of ordering.
 */
@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the menu item (snapshot at order time).
     */
    private String name;

    /**
     * Price of the menu item (snapshot at order time).
     */
    private Double price;

    /**
     * Quantity of the item ordered.
     */
    private int quantity;

    /**
     * Order to which this item belongs.
     */
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * Reference to the original menu item.
     */
    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}