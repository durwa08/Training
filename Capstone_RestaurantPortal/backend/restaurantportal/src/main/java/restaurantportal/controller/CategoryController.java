package restaurantportal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.CategoryRequest;
import restaurantportal.dto.CategoryResponse;
import restaurantportal.service.CategoryService;

import java.util.List;

/**
 * Controller for managing categories in restaurants.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Creates a new category for a given restaurant.
     */
    @PostMapping("/{restaurantId}")
    public CategoryResponse create(@PathVariable Long restaurantId,
                                   @RequestBody CategoryRequest request) {

        log.info("Creating category for restaurantId: {}", restaurantId);

        CategoryResponse response = categoryService.create(restaurantId, request);

        log.info("Category created successfully for restaurantId: {}", restaurantId);

        return response;
    }

    /**
     * Fetches all categories of a restaurant.
     */
    @GetMapping("/{restaurantId}")
    public List<CategoryResponse> getByRestaurant(@PathVariable Long restaurantId) {

        log.info("Fetching categories for restaurantId: {}", restaurantId);

        List<CategoryResponse> categories = categoryService.getByRestaurant(restaurantId);

        log.info("Fetched {} categories for restaurantId: {}", categories.size(), restaurantId);

        return categories;
    }

    /**
     * Deletes a category by ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        log.info("Deleting category with id: {}", id);

        categoryService.delete(id);

        log.info("Category deleted successfully with id: {}", id);
    }
}