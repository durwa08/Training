package restaurantportal.service;

import org.springframework.stereotype.Service;
import restaurantportal.dto.RestaurantRequest;
import restaurantportal.dto.RestaurantResponse;
import restaurantportal.entity.Restaurant;
import restaurantportal.entity.RestaurantStatus;
import restaurantportal.entity.User;
import restaurantportal.repository.RestaurantRepository;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;

import java.util.List;
import java.util.stream.Collectors;

@Service // handles all business logic related to restaurants
public class RestaurantService {

    // Dependency injection of RestaurantRepository and UserRepository to interact with the database for restaurant
    // and user related operations.
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    // constructor injection
    public RestaurantService(RestaurantRepository restaurantRepository,
                             UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    // CREATE
    public RestaurantResponse create(RestaurantRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        // SAFE ENUM
        try {
            restaurant.setStatus(
                    RestaurantStatus.valueOf(request.getStatus().toUpperCase())
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status value");
        }

        // IMPORTANT: set owner
        restaurant.setOwner(owner);

        Restaurant saved = restaurantRepository.save(restaurant);

        return mapToResponse(saved);
    }

    // GET ALL
    public List<RestaurantResponse> getAll() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // GET BY ID
    public RestaurantResponse getById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return mapToResponse(restaurant);
    }

    // UPDATE
    public RestaurantResponse update(Long id, RestaurantRequest request) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        String email = SecurityUtil.getCurrentUserEmail();

        //  SAFE OWNER CHECK - only owner can update restaurant
        if (restaurant.getOwner() == null ||
                restaurant.getOwner().getEmail() == null ||
                !restaurant.getOwner().getEmail().equals(email)) {

            throw new RuntimeException("You are not allowed to update this restaurant");
        }

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        // SAFE ENUM  - validate status value before setting it to restaurant, if invalid throw exception
        try {
            restaurant.setStatus(
                    RestaurantStatus.valueOf(request.getStatus().toUpperCase())
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status value");
        }

        Restaurant updated = restaurantRepository.save(restaurant);

        return mapToResponse(updated);
    }

    // DELETE
    public void delete(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        String email = SecurityUtil.getCurrentUserEmail();

        // SAFE OWNER CHECK
        if (restaurant.getOwner() == null ||
                restaurant.getOwner().getEmail() == null ||
                !restaurant.getOwner().getEmail().equals(email)) {

            throw new RuntimeException("You are not allowed to delete this restaurant");
        }

        restaurantRepository.deleteById(id);
    }

    // MAPPER
    private RestaurantResponse mapToResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getStatus().name()
        );
    }
}