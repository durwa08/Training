package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.MenuItem;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{
    // Get menu items bt restaurant
    List<MenuItem> findByRestaurantId(Long restaurantId);

    //get menu items by category
    List<MenuItem> findByCategoryId(Long categoryId);
}
