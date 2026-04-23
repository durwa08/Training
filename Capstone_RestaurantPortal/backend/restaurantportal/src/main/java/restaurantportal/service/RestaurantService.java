package restaurantportal.service;

import org.springframework.stereotype.Service;
import restaurantportal.dto.RestaurantRequest;
import restaurantportal.dto.RestaurantResponse;
import restaurantportal.entity.Restaurant;
import restaurantportal.entity.RestaurantStatus;
import restaurantportal.repository.RestaurantRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service // contains business logic
// RestaurantService to handle restaurant related business logic like create restaurant get all restaurants get restaurant by id update restaurant delete restaurant etc.
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    // constructor injection
    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    // CREATE
    public RestaurantResponse create(RestaurantRequest request) {

        // convert String → Enum
        RestaurantStatus status =
                RestaurantStatus.valueOf(request.getStatus().toUpperCase());

        // DTO → Entity
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setStatus(status);

        // save to DB
        Restaurant saved = restaurantRepository.save(restaurant);

        // Entity → DTO
        return mapToResponse(saved);
    }

    // ---------------- GET ALL ----------------
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

        // update fields
        restaurant.setName(request.getName());
        restaurant.setAddress(request.getAddress());
        restaurant.setStatus(
                RestaurantStatus.valueOf(request.getStatus().toUpperCase())
        );

        Restaurant updated = restaurantRepository.save(restaurant);

        return mapToResponse(updated);
    }

    // DELETE
    public void delete(Long id) {
        restaurantRepository.deleteById(id);
    }

    // MAPPER
    // converts Entity → DTO
    private RestaurantResponse mapToResponse(Restaurant restaurant) {
        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getStatus().name()
        );
    }
}