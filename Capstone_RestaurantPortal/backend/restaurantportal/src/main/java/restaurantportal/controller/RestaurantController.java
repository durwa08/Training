package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.RestaurantRequest;
import restaurantportal.dto.RestaurantResponse;
import restaurantportal.service.RestaurantService;

import java.util.List;

/**
 * Handles restaurant-related APIs like create, fetch, update, and delete restaurants.
 */
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService service;

    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    /**
     * Creates a new restaurant.
     */
    @PostMapping
    public RestaurantResponse create(@Valid @RequestBody RestaurantRequest request) {
        return service.create(request);
    }

    /**
     * Returns all available restaurants.
     */
    @GetMapping
    public List<RestaurantResponse> getAll() {
        return service.getAll();
    }

    /**
     * Fetches a restaurant by its id.
     */
    @GetMapping("/{id}")
    public RestaurantResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * Updates an existing restaurant.
     */
    @PutMapping("/{id}")
    public RestaurantResponse update(@PathVariable Long id,
                                     @RequestBody RestaurantRequest request) {
        return service.update(id, request);
    }

    /**
     * Deletes a restaurant by its id.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}