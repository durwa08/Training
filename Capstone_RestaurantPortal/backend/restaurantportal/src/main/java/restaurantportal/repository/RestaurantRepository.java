package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Restaurant;
// Repository for Restaurant entity, it extends JpaRepository which provides built-in CRUD operations and also allows us to define custom query methods if needed.
// This interface will be used by the service layer to perform database operations related to restaurants.
public interface RestaurantRepository extends JpaRepository<Restaurant,Long>{

}
