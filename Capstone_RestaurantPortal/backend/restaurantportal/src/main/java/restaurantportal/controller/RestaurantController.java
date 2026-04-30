package restaurantportal.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RestaurantController {

    private static final Logger log = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService service;

    /**
     * Constructs RestaurantController with RestaurantService dependency.
     */
    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    /**
     * Creates a new restaurant in the system.
     */
    @PostMapping
    public RestaurantResponse create(@Valid @RequestBody RestaurantRequest request) {

        log.info("Creating new restaurant: {}", request);

        RestaurantResponse response = service.create(request);

        log.info("Restaurant created successfully");

        return response;
    }

    /**
     * Retrieves all restaurants available in the system.
     */
    @GetMapping
    public List<RestaurantResponse> get() {

        log.info("Fetching all restaurants");

        List<RestaurantResponse> restaurants = service.getAll();

        log.info("Fetched {} restaurants", restaurants.size());

        return restaurants;
    }

    /**
     * Retrieves a specific restaurant by its ID.
     */
    @GetMapping("/{id}")
    public RestaurantResponse getById(@PathVariable Long id) {

        log.info("Fetching restaurant by id: {}", id);

        RestaurantResponse response = service.getById(id);

        log.info("Restaurant fetched successfully for id: {}", id);

        return response;
    }

    /**
     * Updates an existing restaurant by ID.
     */
    @PutMapping("/{id}")
    public RestaurantResponse update(@PathVariable Long id,
                                     @Valid @RequestBody RestaurantRequest request) {

        log.info("Updating restaurant id: {}", id);

        RestaurantResponse response = service.update(id, request);

        log.info("Restaurant updated successfully for id: {}", id);

        return response;
    }

    /**
     * Deletes a restaurant by its ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        log.info("Deleting restaurant id: {}", id);

        service.delete(id);

        log.info("Restaurant deleted successfully for id: {}", id);
    }
}