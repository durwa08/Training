package restaurantportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Handles creation, retrieval, update, and deletion of restaurant entities.
 * Ensures only authenticated owners can modify their restaurants.
 */
@Service
public class RestaurantService {

    private static final Logger log = LoggerFactory.getLogger(RestaurantService.class);

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
        log.info("Restaurant creation request by user: {}", email);

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found during restaurant creation: {}", email);
                    return new RuntimeException("User not found");
                });

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        if (request.getStatus() == null) {
            log.warn("Restaurant creation failed - status null for user: {}", email);
            throw new IllegalArgumentException("Status cannot be null");
        }

        restaurant.setStatus(
                RestaurantStatus.valueOf(request.getStatus().toUpperCase())
        );

        restaurant.setOwner(owner);

        Restaurant saved = restaurantRepository.save(restaurant);

        log.info("Restaurant created successfully with id: {} by user: {}", saved.getId(), email);

        return mapToResponse(saved);
    }

    /**
     * Retrieves all restaurants in the system.
     */
    public List<RestaurantResponse> getAll() {

        log.info("Fetching all restaurants");

        List<RestaurantResponse> list = restaurantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        log.info("Total restaurants fetched: {}", list.size());

        return list;
    }

    /**
     * Retrieves a restaurant by its ID.
     */
    public RestaurantResponse getById(Long id) {

        log.info("Fetching restaurant by id: {}", id);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Restaurant not found: {}", id);
                    return new RuntimeException("Restaurant not found");
                });

        return mapToResponse(restaurant);
    }

    /**
     * Updates an existing restaurant if the logged-in user is the owner.
     */
    public RestaurantResponse update(Long id, RestaurantRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();
        log.info("Update request for restaurantId: {} by user: {}", id, email);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Restaurant not found for update: {}", id);
                    return new RuntimeException("Restaurant not found");
                });

        if (restaurant.getOwner() == null ||
                restaurant.getOwner().getEmail() == null ||
                !restaurant.getOwner().getEmail().equals(email)) {

            log.warn("Unauthorized update attempt for restaurantId: {} by user: {}", id, email);
            throw new RuntimeException("You are not allowed to update this restaurant");
        }

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        if (request.getStatus() == null) {
            log.warn("Restaurant update failed - status null. id: {}", id);
            throw new IllegalArgumentException("Status cannot be null");
        }

        restaurant.setStatus(
                RestaurantStatus.valueOf(request.getStatus().toUpperCase())
        );

        Restaurant updated = restaurantRepository.save(restaurant);

        log.info("Restaurant updated successfully. id: {}", id);

        return mapToResponse(updated);
    }

    /**
     * Deletes a restaurant if the logged-in user is the owner.
     */
    public void delete(Long id) {

        String email = SecurityUtil.getCurrentUserEmail();
        log.info("Delete request for restaurantId: {} by user: {}", id, email);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Restaurant not found for delete: {}", id);
                    return new RuntimeException("Restaurant not found");
                });

        if (restaurant.getOwner() == null ||
                restaurant.getOwner().getEmail() == null ||
                !restaurant.getOwner().getEmail().equals(email)) {

            log.warn("Unauthorized delete attempt for restaurantId: {} by user: {}", id, email);
            throw new RuntimeException("You are not allowed to delete this restaurant");
        }

        restaurantRepository.deleteById(id);

        log.info("Restaurant deleted successfully. id: {}", id);
    }

    /**
     * Converts Restaurant entity to RestaurantResponse DTO.
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