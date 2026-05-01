package restaurantportal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a menu item in a restaurant.
 * Each menu item belongs to a category and a restaurant.
 */
@Entity
@Getter
@Setter
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the menu item.
     */
    private String name;

    /**
     * Price of the menu item.
     */
    private Double price;

    /**
     * Description of the menu item.
     */
    private String description;

    /**
     * Availability status of the menu item.
     */
    private Boolean available;

    /**
     * Flag to indicate if the menu item is deleted (soft delete).
     */
    private Boolean deleted = false;

    /**
     * Category to which this menu item belongs.
     */
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * Restaurant to which this menu item belongs.
     */
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}