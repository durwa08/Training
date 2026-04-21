package restaurantportal.entity;

import jakarta.persistence.*;
import restaurantportal.entity.Order;
import lombok.*;
import java.util.List;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Double walletBalance;

    // One user → many orders(one-to-many relationship)
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    // One user → one cart(One-to -one relationship)
    @OneToOne(mappedBy = "user")
    private Cart cart;
}