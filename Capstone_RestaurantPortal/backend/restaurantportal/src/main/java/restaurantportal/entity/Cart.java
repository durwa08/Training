package restaurantportal.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    /**
     * List of items added to the cart.
     */
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
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