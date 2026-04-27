package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Order;

import java.util.List;

/**
 * Repository interface for Order entity.
 * Provides CRUD operations and custom queries for order management.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Fetches all orders placed by a specific user.
     */
    List<Order> findByUserId(Long userId);

    /**
     * Fetches all orders for restaurants owned by a specific owner.
     */
    List<Order> findByRestaurantOwnerId(Long ownerId);
}