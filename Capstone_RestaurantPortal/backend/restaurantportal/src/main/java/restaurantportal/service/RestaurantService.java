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
 * Handles creation, retrieval, update, and deletion of restaurant entities.
 * Ensures only authenticated owners can modify their restaurants.
 */
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    /**
     * Constructs RestaurantService with required dependencies.
     *
     * @param restaurantRepository repository for restaurant operations
     * @param userRepository repository for user operations
     */
    public RestaurantService(RestaurantRepository restaurantRepository,
                             UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new restaurant for the logged-in user.
     *
     * @param request restaurant creation request
     * @return created restaurant response
     */
    public RestaurantResponse create(RestaurantRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

       restaurant.setStatus(
                RestaurantStatus.valueOf(request.getStatus().toUpperCase())
       );

        restaurant.setOwner(owner);

        Restaurant saved = restaurantRepository.save(restaurant);

        return mapToResponse(saved);
    }

    /**
     * Retrieves all restaurants in the system.
     *
     * @return list of restaurant responses
     */
    public List<RestaurantResponse> getAll() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a restaurant by its ID.
     *
     * @param id restaurant identifier
     * @return restaurant response
     */
    public RestaurantResponse getById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return mapToResponse(restaurant);
    }

    /**
     * Updates an existing restaurant if the logged-in user is the owner.
     *
     * @param id restaurant identifier
     * @param request updated restaurant data
     * @return updated restaurant response
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

        if (request.getStatus() == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        restaurant.setStatus(
                RestaurantStatus.valueOf(request.getStatus().toUpperCase())
        );

        Restaurant updated = restaurantRepository.save(restaurant);

        return mapToResponse(updated);
    }

    /**
     * Deletes a restaurant if the logged-in user is the owner.
     *
     * @param id restaurant identifier
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
     * Converts Restaurant entity to RestaurantResponse DTO.
     *
     * @param restaurant entity object
     * @return response DTO
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