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
//CategoryService to handle category related business logic like create category get categories by restaurant delete category etc.
@Service
public class CategoryService {
// Adding Dependency Injection for CategoryRepository and RestaurantRepository to interact with the database for category
// and restaurant related operations.
    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;
    // Constructor injection for CategoryRepository and RestaurantRepository to initialize the service with the required dependencies.
    public CategoryService(CategoryRepository categoryRepository,
                           RestaurantRepository restaurantRepository) {
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    // CREATE
    public CategoryResponse create(Long restaurantId, CategoryRequest request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        Category category = new Category();
        category.setName(request.getName());
        category.setRestaurant(restaurant);

        Category saved = categoryRepository.save(category);

        return mapToResponse(saved);
    }

    // GET BY RESTAURANT
    public List<CategoryResponse> getByRestaurant(Long restaurantId) {
        return categoryRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // DELETE
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    // MAPPER
    private CategoryResponse mapToResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getRestaurant().getId()
        );
    }
}