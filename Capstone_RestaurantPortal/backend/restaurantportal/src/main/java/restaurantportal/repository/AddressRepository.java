package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Address;

// Repository for Address entity, it extends JpaRepository which provides built-in CRUD operations and
// also allows us to define custom query methods if needed.
public interface AddressRepository extends JpaRepository<Address,Long> {
}
