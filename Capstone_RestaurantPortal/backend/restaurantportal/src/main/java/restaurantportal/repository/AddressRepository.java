package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.Address;

/**
 * Repository interface for Address entity.
 * Provides CRUD operations and allows custom query methods if needed.
 */
public interface AddressRepository extends JpaRepository<Address, Long> {
}