package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.CartItem;

/**
 * Repository interface for CartItem entity.
 * Handles database operations related to cart items.
 */
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}