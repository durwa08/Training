package restaurantportal.entity;

import jakarta.persistence.*;
import restaurantportal.entity.Order;
import lombok.*;
import java.util.List;


@Entity//Represents table in db
//lombok annotations
@Table(name = "users")//table name in db
@Getter
@Setter
@NoArgsConstructor//by default constructor needed by Jpa
@AllArgsConstructor//constructor with all fields
@Builder // implements builder patterns

// The User entity represents a user in the restaurant portal. I
// t includes fields for the user's first name, last name, email, password, phone number, role, wallet balance,
// and references to the user's orders and cart.
public class User {

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto generate id and db handles auto-increment
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    //Encrypted password no plain password
    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    //Maps enum to db
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