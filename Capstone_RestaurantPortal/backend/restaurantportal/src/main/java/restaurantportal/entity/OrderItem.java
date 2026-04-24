package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
//The OrderItem entity represents an item in an order.
// It includes the quantity, price, and references to the order and menu item it belongs to.
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;
}