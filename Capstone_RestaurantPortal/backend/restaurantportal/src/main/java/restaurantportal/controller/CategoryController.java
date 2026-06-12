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

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
        logger.info("CategoryController initialized");
    }

    /**
     * Creates a new category for a given restaurant.
     */
    @PostMapping("/{restaurantId}")
    public CategoryResponse create(@PathVariable Long restaurantId,
                                   @RequestBody CategoryRequest request) {
        logger.info("Received request to create category for restaurantId: {}", restaurantId);
        logger.debug("CategoryRequest: {}", request);
        CategoryResponse response = categoryService.create(restaurantId, request);
        logger.info("Category created successfully for restaurantId: {}", restaurantId);
        return response;
    }

    /**
     * Fetches all categories of a restaurant.
     */
    @GetMapping("/{restaurantId}")
    public List<CategoryResponse> getByRestaurant(@PathVariable Long restaurantId) {
        logger.info("Received request to fetch categories for restaurantId: {}", restaurantId);
        List<CategoryResponse> response = categoryService.getByRestaurant(restaurantId);
        logger.info("Categories fetched successfully for restaurantId: {}", restaurantId);
        return response;
    }

    /**
     * Updates an existing category.
     */
    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id,
                                   @RequestBody CategoryRequest request) {
        logger.info("Received request to update category with id: {}", id);
        logger.debug("CategoryRequest: {}", request);
        CategoryResponse response = categoryService.update(id, request);
        logger.info("Category updated successfully with id: {}", id);
        return response;
    }

    /**
     * Deletes a category by ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Received request to delete category with id: {}", id);
        categoryService.delete(id);
        logger.info("Category deleted successfully with id: {}", id);
    }
}