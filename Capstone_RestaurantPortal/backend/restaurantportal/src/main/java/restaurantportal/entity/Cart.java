package restaurantportal.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
//  the cart entity represents the shopping cart of a user. It includes a list of cart items and a reference to the user who owns the cart.
//  Each cart item represents a menu item that the user wants to order, along with the quantity.
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> items;
}
