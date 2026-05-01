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

/**
 * Service layer responsible for menu item related business logic.
 * Handles creation, retrieval, and deletion of menu items.
 */
@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuItemService(MenuItemRepository menuItemRepository,
                           CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Creates a new menu item under a category.
     */
    public MenuItemResponse create(Long categoryId, MenuItemRequest request) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        item.setCategory(category);
        item.setRestaurant(category.getRestaurant());

        MenuItem saved = menuItemRepository.save(item);

        return mapToResponse(saved);
    }

    /**
     * Updates an existing menu item.
     */
    public MenuItemResponse update(Long id, MenuItemRequest request) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        if (request.getAvailable() != null) {
            item.setAvailable(request.getAvailable());
        }

        MenuItem updated = menuItemRepository.save(item);
        return mapToResponse(updated);
    }

    /**
     * Retrieves all menu items for a specific restaurant.
     */
    public List<MenuItemResponse> getByRestaurant(Long restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .filter(item -> !Boolean.TRUE.equals(item.getDeleted()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Soft deletes a menu item by its ID (marks as deleted).
     */
    public void delete(Long id) {
        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found"));
        item.setDeleted(true);
        menuItemRepository.save(item);
    }

    /**
     * Converts MenuItem entity to MenuItemResponse DTO.
     */
    private MenuItemResponse mapToResponse(MenuItem item) {
        return new MenuItemResponse(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getDescription(),
                item.getAvailable(),
                item.getCategory().getId());
    }
}