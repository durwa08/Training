package restaurantportal.service;

import org.springframework.stereotype.Service;
import restaurantportal.entity.Category;
import restaurantportal.entity.Restaurant;
import restaurantportal.repository.CategoryRepository;
import restaurantportal.repository.RestaurantRepository;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final RestaurantRepository restaurantRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           RestaurantRepository restaurantRepository) {
        this.categoryRepository = categoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    // add category to restaurant
    public Category create(Long restaurantId, Category category) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        category.setRestaurant(restaurant);

        return categoryRepository.save(category);
    }

    // get categories of restaurant
    public List<Category> getByRestaurant(Long restaurantId) {
        return categoryRepository.findByRestaurantId(restaurantId);
    }

    // delete category from restaurant
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}