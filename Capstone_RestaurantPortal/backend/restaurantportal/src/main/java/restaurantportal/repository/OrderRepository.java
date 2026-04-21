package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Order;
public interface OrderRepository extends JpaRepository<Order,Long>{

}
