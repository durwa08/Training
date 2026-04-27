package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//the address entity represents the address of a user.
// it includes street, city, state and pincode.
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String pincode;

    // Many addresses → one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}