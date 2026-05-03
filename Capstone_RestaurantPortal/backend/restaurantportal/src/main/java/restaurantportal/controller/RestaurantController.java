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
@CrossOrigin(origins = "*", allowedHeaders = "*") // Allow CORS for all origins and headers
public class RestaurantController {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService service;

    /**
     * Constructs RestaurantController with RestaurantService dependency.
     *
     */
    public RestaurantController(RestaurantService service) {
        this.service = service;
        logger.info("RestaurantController initialized");
    }

    /**
     * Creates a new restaurant in the system.
     */
    @PostMapping
    public RestaurantResponse create(@Valid @RequestBody RestaurantRequest request) {
        logger.info("Received request to create restaurant");
        logger.debug("RestaurantRequest: {}", request);
        RestaurantResponse response = service.create(request);
        logger.info("Restaurant created successfully");
        return response;
    }

    /**
     * Retrieves all restaurants available in the system.
     */
    @GetMapping
    public List<RestaurantResponse> get() {
        logger.info("Received request to fetch all restaurants");
        List<RestaurantResponse> response = service.getAll();
        logger.info("Restaurants fetched successfully");
        return response;
    }

    /**
     * Retrieves a specific restaurant by its ID.
     */
    @GetMapping("/{id}")
    public RestaurantResponse getById(@PathVariable Long id) {
        logger.info("Received request to fetch restaurant with id: {}", id);
        RestaurantResponse response = service.getById(id);
        logger.info("Restaurant fetched successfully with id: {}", id);
        return response;
    }

    /**
     * Updates an existing restaurant by ID.

     */
    @PutMapping("/{id}")
    public RestaurantResponse update(@PathVariable Long id,
                                     @Valid @RequestBody RestaurantRequest request) {
        logger.info("Received request to update restaurant with id: {}", id);
        logger.debug("RestaurantRequest: {}", request);
        RestaurantResponse response = service.update(id, request);
        logger.info("Restaurant updated successfully with id: {}", id);
        return response;
    }

    /**
     * Deletes a restaurant by its ID.
     *
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Received request to delete restaurant with id: {}", id);
        service.delete(id);
        logger.info("Restaurant deleted successfully with id: {}", id);
    }
}