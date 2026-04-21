package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.CartItem;
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
}
