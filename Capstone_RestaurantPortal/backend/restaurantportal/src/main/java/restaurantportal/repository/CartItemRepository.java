package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.CartItem;

// handles DB operations for CartItem
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}