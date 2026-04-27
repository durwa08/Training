package restaurantportal.service;

import org.springframework.stereotype.Service;
import restaurantportal.entity.MenuItem;
import restaurantportal.entity.Category;
import restaurantportal.repository.MenuItemRepository;
import restaurantportal.repository.CategoryRepository;
import restaurantportal.dto.MenuItemRequest;
import restaurantportal.dto.MenuItemResponse;

import java.util.List;
import java.util.stream.Collectors;
// MenuItem Service class to handle business logic related to menu items, such as creating, retrieving, and deleting menu items for a restaurant.
// it depends both on category as-well-as Restaurant, as menu item is associated with both of them. Hence we need to inject both the repositories in this service class.

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

    // CREATE
    public MenuItemResponse create(Long categoryId, MenuItemRequest request) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setCategory(category);
        item.setRestaurant(category.getRestaurant());

        MenuItem saved = menuItemRepository.save(item);

        return mapToResponse(saved);
    }

    // GET BY RESTAURANT
    public List<MenuItemResponse> getByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // DELETE
    public void delete(Long id) {
        menuItemRepository.deleteById(id);
    }

    // MAPPER
    private MenuItemResponse mapToResponse(MenuItem item) {
        return new MenuItemResponse(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getCategory().getId()
        );
    }
}