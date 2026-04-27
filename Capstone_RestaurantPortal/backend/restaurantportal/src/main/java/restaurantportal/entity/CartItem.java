package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing an item inside a shopping cart.
 * It stores quantity, price at the time of adding, and references to cart and menu item.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Quantity of the menu item added to cart.
     */
    private int quantity = 1;

    /**
     * Price of the item at the time it was added to the cart.
     */
    private Double price;

    /**
     * Cart to which this item belongs.
     */
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    /**
     * Menu item associated with this cart item.
     */
    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    /**
     * Creates a CartItem with menu item, quantity, and price snapshot.
     */
    public CartItem(MenuItem menuItem, int quantity, double price) {
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.price = price;
    }
}