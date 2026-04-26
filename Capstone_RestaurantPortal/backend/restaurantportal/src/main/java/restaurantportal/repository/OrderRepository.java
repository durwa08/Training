package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Order;

import java.util.List;

//OrderRepository is a Spring Data JPA repository interface for managing Order entities. It extends JpaRepository, which provides built-in CRUD operations and allows us to define custom query methods if needed.
// This interface will be used by the service layer to perform database operations related to orders.
public interface OrderRepository extends JpaRepository<Order,Long>{
    List<Order> findByUserId(Long userId);

}
