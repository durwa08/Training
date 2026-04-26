package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
// snapshot of menu item at order time
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;   // menu name snapshot
    private Double price;  // price snapshot
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem; // reference to the original menu item
}