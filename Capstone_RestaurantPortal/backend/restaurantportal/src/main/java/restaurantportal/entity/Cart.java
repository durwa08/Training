package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a shopping cart for a user.
 * Each user has exactly one cart containing multiple items.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User who owns this cart.
     */
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * List of items added to the cart.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();

    /**
     * Total amount of the cart.
     */
    private Double totalAmount = 0.0;

    /**
     * Constructor to create a cart for a specific user.
     */
    public Cart(User user) {
        this.user = user;
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the cart and sets cart reference in item.
     */
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    /**
     * Removes an item from the cart and clears its cart reference.
     */
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }
}