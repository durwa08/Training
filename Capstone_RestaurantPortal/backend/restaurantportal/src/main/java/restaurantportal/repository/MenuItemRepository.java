package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.MenuItem;

import java.util.List;
//MenuItemRepository is a Spring Data JPA repository interface for managing MenuItem entities.
// It extends JpaRepository, which provides built-in CRUD operations and allows us to define custom query methods if needed.

public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{
    // Get menu items bt restaurant
    List<MenuItem> findByRestaurantId(Long restaurantId);

    //get menu items by category
    List<MenuItem> findByCategoryId(Long categoryId);
}
