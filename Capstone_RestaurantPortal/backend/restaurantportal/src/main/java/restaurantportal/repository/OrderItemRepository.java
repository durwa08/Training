package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.OrderItem;

/**
 * Repository interface for OrderItem entity.
 * Provides CRUD operations for order items.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}