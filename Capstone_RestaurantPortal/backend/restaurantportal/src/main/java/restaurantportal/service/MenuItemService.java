package restaurantportal.service;

import org.springframework.stereotype.Service;
import restaurantportal.entity.MenuItem;
import restaurantportal.entity.Category;
import restaurantportal.repository.MenuItemRepository;
import restaurantportal.repository.CategoryRepository;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuItemService(MenuItemRepository menuItemRepository,
                           CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    // create menu item
    public MenuItem create(Long categoryId, MenuItem item) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        item.setCategory(category);
        item.setRestaurant(category.getRestaurant());

        return menuItemRepository.save(item);
    }

    // get menu by restaurant
    public List<MenuItem> getByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    // delete item from menu
    public void delete(Long id) {
        menuItemRepository.deleteById(id);
    }
}