package restaurantportal.service;

import org.springframework.stereotype.Service;
import restaurantportal.dto.CategoryRequest;
import restaurantportal.dto.CategoryResponse;
import restaurantportal.entity.Category;
import restaurantportal.entity.Restaurant;
import restaurantportal.repository.CategoryRepository;
import restaurantportal.repository.RestaurantRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer responsible for category-related business logic.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           RestaurantRepository restaurantRepository) {
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Creates a new category for a restaurant.
     */
    public CategoryResponse create(Long restaurantId, CategoryRequest request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Category category = new Category();
        category.setName(request.getName());
        category.setRestaurant(restaurant);

        Category saved = categoryRepository.save(category);

        return mapToResponse(saved);
    }

    /**
     * Fetches all categories of a given restaurant.
     */
    public List<CategoryResponse> getByRestaurant(Long restaurantId) {
        return categoryRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a category by its ID.
     */
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Maps Category entity to CategoryResponse DTO.
     */
    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getRestaurant().getId());
    }
}