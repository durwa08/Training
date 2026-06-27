package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Category;

import java.util.List;

/**
 * Repository interface for Category entity.
 * Provides CRUD operations and custom queries for category management.
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Fetches all categories belonging to a specific restaurant.
     */
    List<Category> findByRestaurantId(Long restaurantId);
}