package restaurantportal.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import restaurantportal.entity.User;
import java.util.Optional;
// Jpa provides built-in crud operations
//Repository connects java <-> Db
public interface UserRepository extends JpaRepository<User, Long> {
    // it will avoid null and NullPointerException and give safe wrapper object
    //finding user by email bcoz it is used in login and also to avoid duplicates
    Optional<User> findByEmail(String email);
}
