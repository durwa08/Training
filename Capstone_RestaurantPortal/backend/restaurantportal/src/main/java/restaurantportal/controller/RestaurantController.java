package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.RestaurantRequest;
import restaurantportal.dto.RestaurantResponse;
import restaurantportal.service.RestaurantService;

import java.util.List;

/**
 * REST controller for managing restaurant operations.
 * Provides endpoints for creating, retrieving, updating, and deleting restaurants.
 * All endpoints are secured based on Spring Security configuration.
 */
@RestController
@RequestMapping("/api/restaurants")
@CrossOrigin(origins = "*", allowedHeaders = "*") // Allow CORS for all origins and headers
public class RestaurantController {

    private final RestaurantService service;

    /**
     * Constructs RestaurantController with RestaurantService dependency.
     *
     * @param service service layer handling restaurant business logic
     */
    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    /**
     * Creates a new restaurant in the system.
     *
     * @param request restaurant creation request payload
     * @return created restaurant details
     */
    @PostMapping
    public RestaurantResponse create(@Valid @RequestBody RestaurantRequest request) {
        return service.create(request);
    }

    /**
     * Retrieves all restaurants available in the system.
     *
     * @return list of restaurants
     */
    @GetMapping
    public List<RestaurantResponse> get() {
        return service.getAll();
    }

    /**
     * Retrieves a specific restaurant by its ID.
     *
     * @param id restaurant identifier
     * @return restaurant details
     */
    @GetMapping("/{id}")
    public RestaurantResponse getById(@PathVariable Long id) {
        return service.getById(id);
    }

    /**
     * Updates an existing restaurant by ID.
     *
     * @param id restaurant identifier
     * @param request updated restaurant data
     * @return updated restaurant details
     */
    @PutMapping("/{id}")
    public RestaurantResponse update(@PathVariable Long id,
                                     @Valid @RequestBody RestaurantRequest request) {
        return service.update(id, request);
    }

    /**
     * Deletes a restaurant by its ID.
     *
     * @param id restaurant identifier
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}