package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Cart;
import restaurantportal.entity.User;
import java.util.Optional;

/*
CartRepository is a Spring Data JPA repository interface for managing Cart entities.
/ It extends JpaRepository, which provides built-in CRUD operations and allows us to define custom query methods if needed.
 This interface will be used by the service layer to perform database operations related to carts.

 */

// handles DB Operations for cart
public interface CartRepository extends JpaRepository<Cart,Long>{
    // find cart by user
    Optional<Cart> findByUser(User user);
}
