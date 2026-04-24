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