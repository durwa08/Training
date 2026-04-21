package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.MenuItem;
public interface MenuItemRepository extends JpaRepository<MenuItem, Long>{
}
