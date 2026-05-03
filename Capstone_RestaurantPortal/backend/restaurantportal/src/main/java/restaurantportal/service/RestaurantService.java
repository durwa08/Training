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

    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    /**
     * Constructs RestaurantService with required dependencies.
     */
    public RestaurantService(RestaurantRepository restaurantRepository,
                             UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
        logger.info("RestaurantService initialized");
    }

    /**
     * Creates a new restaurant for the logged-in user.
     */
    public RestaurantResponse create(RestaurantRequest request) {

        logger.info("Creating restaurant");
        logger.debug("RestaurantRequest: {}", request);

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        if (request.getStatus() == null) {
            logger.error("Restaurant status is null");
            throw new IllegalArgumentException("Status cannot be null");
        }

        restaurant.setStatus(
                RestaurantStatus.valueOf(request.getStatus().toUpperCase())
        );

        restaurant.setOwner(owner);

        Restaurant saved = restaurantRepository.save(restaurant);

        logger.info("Restaurant created successfully with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    /**
     * Retrieves all restaurants in the system.
     *
     * @return list of restaurant responses
     */
    public List<RestaurantResponse> getAll() {

        logger.info("Fetching all restaurants");

        List<RestaurantResponse> response = restaurantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Restaurants fetched successfully");

        return response;
    }

    /**
     * Retrieves a restaurant by its ID.
     */
    public RestaurantResponse getById(Long id) {

        logger.info("Fetching restaurant with id: {}", id);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with id: {}", id);
                    return new RuntimeException("Restaurant not found");
                });

        logger.info("Restaurant fetched successfully with id: {}", id);

        return mapToResponse(restaurant);
    }

    /**
     * Updates an existing restaurant if the logged-in user is the owner.
     *
     */
    public RestaurantResponse update(Long id, RestaurantRequest request) {

        logger.info("Updating restaurant with id: {}", id);
        logger.debug("RestaurantRequest: {}", request);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with id: {}", id);
                    return new RuntimeException("Restaurant not found");
                });

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        if (restaurant.getOwner() == null ||
                restaurant.getOwner().getEmail() == null ||
                !restaurant.getOwner().getEmail().equals(email)) {

            logger.error("Unauthorized update attempt for restaurant id: {}", id);
            throw new RuntimeException("You are not allowed to update this restaurant");
        }

        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        if (request.getStatus() == null) {
            logger.error("Restaurant status is null during update");
            throw new IllegalArgumentException("Status cannot be null");
        }

        restaurant.setStatus(
                RestaurantStatus.valueOf(request.getStatus().toUpperCase())
        );

        Restaurant updated = restaurantRepository.save(restaurant);

        logger.info("Restaurant updated successfully with id: {}", id);

        return mapToResponse(updated);
    }

    /**
     * Deletes a restaurant if the logged-in user is the owner.
     */
    public void delete(Long id) {

        logger.info("Deleting restaurant with id: {}", id);

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with id: {}", id);
                    return new RuntimeException("Restaurant not found");
                });

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        if (restaurant.getOwner() == null ||
                restaurant.getOwner().getEmail() == null ||
                !restaurant.getOwner().getEmail().equals(email)) {

            logger.error("Unauthorized delete attempt for restaurant id: {}", id);
            throw new RuntimeException("You are not allowed to delete this restaurant");
        }

        restaurantRepository.deleteById(id);

        logger.info("Restaurant deleted successfully with id: {}", id);
    }

    private RestaurantResponse mapToResponse(Restaurant restaurant) {

        logger.debug("Mapping Restaurant to RestaurantResponse with id: {}", restaurant.getId());

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getStatus().name()
        );
    }
}