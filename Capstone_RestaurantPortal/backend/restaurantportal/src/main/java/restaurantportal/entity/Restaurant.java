package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entity representing a restaurant in the system.
 * Each restaurant is owned by a user and contains basic details like name and address.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "restaurant")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    /**
     * Name of the restaurant.
     */
    private String name;

    /**
     * Address of the restaurant.
     */
    private String address;

    /**
     * Current status of the restaurant (OPEN, CLOSED, etc.).
     */
    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    /**
     * Owner of the restaurant.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}