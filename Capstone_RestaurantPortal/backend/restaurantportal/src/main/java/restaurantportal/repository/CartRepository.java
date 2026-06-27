package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Cart;
import restaurantportal.entity.User;

import java.util.Optional;

/**
 * Repository interface for Cart entity.
 * Provides CRUD operations and custom query methods for cart management.
 */
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * Finds a cart associated with a specific user.
     */
    Optional<Cart> findByUser(User user);
}