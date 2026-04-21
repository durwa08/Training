package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Cart;
public interface CartRepository extends JpaRepository<Cart,Long>{
}
