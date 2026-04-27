package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//This entity represents a restaurant in the restaurant portal.
// It includes the name, address, status and reference to the owner of the restaurant.
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Enumerated(EnumType.STRING)
    private RestaurantStatus status;

    // Owner of restaurant
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}