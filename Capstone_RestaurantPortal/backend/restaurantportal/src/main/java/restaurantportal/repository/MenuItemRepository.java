package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.MenuItem;

import java.util.List;

/**
 * Repository interface for MenuItem entity.
 * Provides CRUD operations and custom query methods for menu items.
 */
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    /**
     * Fetches all menu items belonging to a specific restaurant.
     */
    List<MenuItem> findByRestaurantId(Long restaurantId);

    /**
     * Fetches all menu items belonging to a specific category.
     */
    List<MenuItem> findByCategoryId(Long categoryId);
}