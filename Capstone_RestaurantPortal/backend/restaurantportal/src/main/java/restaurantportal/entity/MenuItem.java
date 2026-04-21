package restaurantportal.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter

public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Boolean isAvailable;

    @ManyToOne
    @JoinColumn(name = "category_id")
        private Category category;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;


}
