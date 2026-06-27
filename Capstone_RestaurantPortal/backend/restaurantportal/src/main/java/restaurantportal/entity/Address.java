package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a user's address details.
 * Each address belongs to a specific user.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Street information of the address.
     */
    private String street;

    /**
     * City name.
     */
    private String city;

    /**
     * State name.
     */
    private String state;

    /**
     * Pincode of the location.
     */
    private String pincode;

    /**
     * User to whom this address belongs.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}