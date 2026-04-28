package restaurantportal.controller;

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
        return categoryService.create(restaurantId, request);
    }

    /**
     * Fetches all categories of a restaurant.
     */
    @GetMapping("/{restaurantId}")
    public List<CategoryResponse> getByRestaurant(@PathVariable Long restaurantId) {
        return categoryService.getByRestaurant(restaurantId);
    }

    /**
     * Deletes a category by ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.delete(id);
    }
}