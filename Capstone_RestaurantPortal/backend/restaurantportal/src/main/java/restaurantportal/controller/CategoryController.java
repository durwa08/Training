package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.CategoryRequest;
import restaurantportal.dto.CategoryResponse;
import restaurantportal.service.CategoryService;

import java.util.List;

/**
 * Handles category-related APIs like create, fetch, and delete categories.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    /**
     * Creates a new category under a specific restaurant.
     */
    @PostMapping("/{restaurantId}")
    public CategoryResponse create(@PathVariable Long restaurantId,
                                   @Valid @RequestBody CategoryRequest request) {
        return service.create(restaurantId, request);
    }

    /**
     * Fetches all categories for a given restaurant.
     */
    @GetMapping("/{restaurantId}")
    public List<CategoryResponse> getByRestaurant(@PathVariable Long restaurantId) {
        return service.getByRestaurant(restaurantId);
    }

    /**
     * Deletes a category by its id.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}