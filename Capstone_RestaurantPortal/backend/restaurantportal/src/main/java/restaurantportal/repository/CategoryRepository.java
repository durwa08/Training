package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Category;
import restaurantportal.entity.Restaurant;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
