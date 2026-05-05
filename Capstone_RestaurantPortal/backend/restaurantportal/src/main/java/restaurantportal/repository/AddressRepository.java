package restaurantportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import restaurantportal.entity.Address;

import java.util.List;

/**
 * Repository for Address entity.
 * Provides CRUD operations and custom queries.
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Fetch all addresses belonging to a specific user.
     */
    List<Address> findByUserId(Long userId);
}