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

/**
 * Service responsible for restaurant-related business logic.
 * Handles creation, updates, retrieval, and deletion of restaurants.
 */
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new restaurant for the logged-in user.
     */
    public RestaurantResponse create(RestaurantRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        try {
            restaurant.setStatus(
                    RestaurantStatus.valueOf(request.getStatus().toUpperCase())
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid status value");
        }

        restaurant.setOwner(owner);

        Restaurant saved = restaurantRepository.save(restaurant);

        return mapToResponse(saved);
    }

    /**
     * Retrieves all restaurants in the system.
     */
    public List<RestaurantResponse> getAll() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a restaurant by its ID.
     */
    public RestaurantResponse getById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return mapToResponse(restaurant);
    }

    /**
     * Updates an existing restaurant (only by owner).
     */
    public RestaurantResponse update(Long id, RestaurantRequest request) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        String email = SecurityUtil.getCurrentUserEmail();

        if (restaurant.getOwner() == null ||
                restaurant.getOwner().getEmail() == null ||
                !restaurant.getOwner().getEmail().equals(email)) {

            throw new RuntimeException("You are not allowed to update this restaurant");
        }

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

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

    /**
     * Deletes a restaurant (only by owner).
     */
    public void delete(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        String email = SecurityUtil.getCurrentUserEmail();

        if (restaurant.getOwner() == null ||
                restaurant.getOwner().getEmail() == null ||
                !restaurant.getOwner().getEmail().equals(email)) {

            throw new RuntimeException("You are not allowed to delete this restaurant");
        }

        restaurantRepository.deleteById(id);
    }

    /**
     * Maps Restaurant entity to RestaurantResponse DTO.
     */
    private RestaurantResponse mapToResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getStatus().name()
        );
    }
}