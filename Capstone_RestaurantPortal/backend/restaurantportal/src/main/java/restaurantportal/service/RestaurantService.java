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

    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    // constructor injection
    public RestaurantService(RestaurantRepository restaurantRepository,
                             UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    // CREATE the restaurant,only logged-in user can create a restaurant and that restaurant will be associated with that user as owner.
    // Hence, we need to fetch the logged-in user from DB and set it as owner of restaurant before saving the restaurant to DB.
    public RestaurantResponse create(RestaurantRequest request) {

        // get logged-in user email from JWT
        String email = SecurityUtil.getCurrentUserEmail();

        // fetch user from DB
        User owner = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // convert request → entity
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());

        // convert String → Enum
        restaurant.setStatus(
                RestaurantStatus.valueOf(request.getStatus().toUpperCase())
        );

        // set owner of restaurant
        restaurant.setOwner(owner);

        // save to DB
        Restaurant saved = restaurantRepository.save(restaurant);

        // return response DTO
        return mapToResponse(saved);
    }

    //  GET ALL
    public List<RestaurantResponse> getAll() {

        // fetch all restaurants and convert to DTO
        return restaurantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //  GET BY ID
    public RestaurantResponse getById(Long id) {

        // find restaurant or throw error
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return mapToResponse(restaurant);
    }

    //  UPDATE
    public RestaurantResponse update(Long id, RestaurantRequest request) {

        // find restaurant
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // get logged-in user
        String email = SecurityUtil.getCurrentUserEmail();

        // check if same owner
        if (!restaurant.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to update this restaurant");
        }

        // update fields
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setStatus(
                RestaurantStatus.valueOf(request.getStatus().toUpperCase())
        );

        // save updated data
        Restaurant updated = restaurantRepository.save(restaurant);

        return mapToResponse(updated);
    }

    //  DELETE
    public void delete(Long id) {

        // find restaurant
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        // get logged-in user
        String email = SecurityUtil.getCurrentUserEmail();

        // allow only owner to delete
        if (!restaurant.getOwner().getEmail().equals(email)) {
            throw new RuntimeException("You are not allowed to delete this restaurant");
        }

        restaurantRepository.deleteById(id);
    }

    // OPTIONAL: GET MY RESTAURANTS
    public List<RestaurantResponse> getMyRestaurants() {

        String email = SecurityUtil.getCurrentUserEmail();

        // return only restaurants created by current user
        return restaurantRepository.findAll()
                .stream()
                .filter(r -> r.getOwner().getEmail().equals(email))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    //  MAPPER
    // converts Entity → Response DTO
    private RestaurantResponse mapToResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getStatus().name()
        );
    }
}