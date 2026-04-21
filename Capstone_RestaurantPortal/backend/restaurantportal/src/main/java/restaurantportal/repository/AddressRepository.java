package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Address;
public interface AddressRepository extends JpaRepository<Address,Long> {
}
