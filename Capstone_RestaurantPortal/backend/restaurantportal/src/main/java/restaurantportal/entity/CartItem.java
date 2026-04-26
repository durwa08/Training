package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// cart item represents an item in the shopping cart. It includes the quantity of the item,
// the price at the time of adding to the cart , and references to the cart it belongs to and the menu item it represents.
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // quantity of item
    private int quantity = 1;

    // price at the time of adding (important for consistency)
    private Double price;

    // many items belong to one cart
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;



    // reference to menu item
    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

        public CartItem(MenuItem menuItem, int quantity, double price) {
            this.menuItem = menuItem;
            this.quantity = quantity;
            this.price = price;
        }


}