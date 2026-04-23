package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Category;
import restaurantportal.entity.Restaurant;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category,Long> {

    // get categories by restaurant
    List<Category> findByRestaurantId(Long restaurantId);
}
