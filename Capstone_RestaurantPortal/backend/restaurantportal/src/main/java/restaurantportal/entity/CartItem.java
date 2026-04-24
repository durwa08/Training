package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
// cart item entity represents an item in the shopping cart.
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}