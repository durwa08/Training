package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Category;
import restaurantportal.entity.Restaurant;

import java.util.List;
// CategoryRepository is a Spring Data JPA repository interface for managing Category entities.
// It extends JpaRepository, which provides built-in CRUD operations and allows us to define custom query methods if needed.
// This interface will be used by the service layer to perform database operations related to categories.
public interface CategoryRepository extends JpaRepository<Category,Long> {

    // get categories by restaurant
    List<Category> findByRestaurantId(Long restaurantId);
}
