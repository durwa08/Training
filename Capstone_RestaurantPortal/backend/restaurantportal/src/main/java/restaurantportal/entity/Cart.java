package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// class cart is the shopping cart of the user. It contains the list of items that the user has added to the cart and the total amount of the cart.
// Each user has one cart and each cart belongs to one user.
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // primary key

    // each user has one cart
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // cart contains multiple items
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    private List<CartItem> items = new ArrayList<>();

    // total cart value (helps avoid recalculation every time)
    private Double totalAmount = 0.0;


    //  HELPER METHODS For add and remove item from cart

    // add item to cart
    public void addItem(CartItem item) {
        items.add(item);
        item.setCart(this);
    }

    // remove item from cart
    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);

    }
    public Cart(User user) {
        this.user = user;
        this.items = new ArrayList<>();
    }

}