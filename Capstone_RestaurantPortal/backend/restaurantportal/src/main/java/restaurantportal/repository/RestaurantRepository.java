package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Restaurant;
public interface RestaurantRepository extends JpaRepository<Restaurant,Long>{
}
