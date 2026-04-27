package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Restaurant;

/**
 * Repository interface for Restaurant entity.
 * Provides CRUD operations and allows custom queries if required.
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}