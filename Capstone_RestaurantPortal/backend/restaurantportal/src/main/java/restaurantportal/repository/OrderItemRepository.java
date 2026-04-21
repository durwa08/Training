package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.OrderItem;
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
