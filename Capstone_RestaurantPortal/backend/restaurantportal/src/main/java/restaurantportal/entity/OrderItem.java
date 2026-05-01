package restaurantportal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

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
    @JsonIgnore
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