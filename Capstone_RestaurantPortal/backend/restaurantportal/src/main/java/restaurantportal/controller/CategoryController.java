package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.CategoryRequest;
import restaurantportal.dto.CategoryResponse;
import restaurantportal.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
//CategoryController to handle category related endpoints like create category get categories by restaurant delete category etc.
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping("/{restaurantId}")
    public CategoryResponse create(@PathVariable Long restaurantId,
                                   @Valid @RequestBody CategoryRequest request) {
        return service.create(restaurantId, request);
    }

    @GetMapping("/{restaurantId}")
    public List<CategoryResponse> getByRestaurant(@PathVariable Long restaurantId) {
        return service.getByRestaurant(restaurantId);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}