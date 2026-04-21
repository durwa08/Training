package restaurantportal.entity;
import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.*;

import java.awt.*;
import java.util.*;
import java.util.List;

@Entity
@Getter
@Setter

public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    @ManyToOne
    @JoinColumn(name = "Owner_id")
    private User owner;

    @OneToMany(mappedBy = "restaurant")
    private List<Category> categories;

    @OneToMany(mappedBy = "restaurant")
        private List<MenuItem> menuItems;
}
