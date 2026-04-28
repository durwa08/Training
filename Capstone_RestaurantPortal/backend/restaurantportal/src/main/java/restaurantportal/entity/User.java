package restaurantportal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

/**
 * Entity representing a user in the restaurant portal.
 * A user can place orders, have a cart, and own a wallet.
 */
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

    /**
     * First name of the user.
     */
    private String firstName;

    /**
     * Last name of the user.
     */
    private String lastName;

    /**
     * Email of the user (unique identifier).
     */
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * Encrypted password of the user.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Contact number of the user.
     */
    private String phoneNumber;

    /**
     * Role of the user in the system (USER, RESTAURANT_OWNER).
     */
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * Wallet balance of the user.
     */
    @Column(name="wallet_balance")
    private Double walletBalance;

    /**
     * Orders placed by the user.
     */
    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    /**
     * Cart associated with the user.
     */
    @OneToOne(mappedBy = "user")
    private Cart cart;
}