package restaurantportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(CategoryService.class);

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

        log.info("Creating category '{}' for restaurantId: {}", request.getName(), restaurantId);

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> {
                    log.error("Restaurant not found with id: {}", restaurantId);
                    return new RuntimeException("Restaurant not found");
                });

        Category category = new Category();
        category.setName(request.getName());
        category.setRestaurant(restaurant);

        Category saved = categoryRepository.save(category);

        log.info("Category created successfully with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    /**
     * Fetches all categories of a given restaurant.
     */
    public List<CategoryResponse> getByRestaurant(Long restaurantId) {

        log.info("Fetching categories for restaurantId: {}", restaurantId);

        List<CategoryResponse> result = categoryRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        log.info("Fetched {} categories for restaurantId: {}", result.size(), restaurantId);

        return result;
    }

    /**
     * Deletes a category by its ID.
     */
    public void delete(Long id) {

        log.info("Deleting category with id: {}", id);

        categoryRepository.deleteById(id);

        log.info("Category deleted successfully with id: {}", id);
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