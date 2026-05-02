package restaurantportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MenuItemService.class);

    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    public MenuItemService(MenuItemRepository menuItemRepository,
                           CategoryRepository categoryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.categoryRepository = categoryRepository;
        logger.info("MenuItemService initialized");
    }

    /**
     * Creates a new menu item under a category.
     */
    public MenuItemResponse create(Long categoryId, MenuItemRequest request) {

        logger.info("Creating menu item for categoryId: {}", categoryId);
        logger.debug("MenuItemRequest: {}", request);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    logger.error("Category not found with id: {}", categoryId);
                    return new RuntimeException("Category not found");
                });

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        item.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        item.setCategory(category);
        item.setRestaurant(category.getRestaurant());

        MenuItem saved = menuItemRepository.save(item);

        logger.info("Menu item created successfully with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    /**
     * Updates an existing menu item.
     */
    public MenuItemResponse update(Long id, MenuItemRequest request) {

        logger.info("Updating menu item with id: {}", id);
        logger.debug("MenuItemRequest: {}", request);

        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Menu item not found with id: {}", id);
                    return new RuntimeException("Menu item not found");
                });

        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setDescription(request.getDescription());
        if (request.getAvailable() != null) {
            item.setAvailable(request.getAvailable());
        }

        MenuItem updated = menuItemRepository.save(item);

        logger.info("Menu item updated successfully with id: {}", id);

        return mapToResponse(updated);
    }

    /**
     * Retrieves all menu items for a specific restaurant.
     */
    public List<MenuItemResponse> getByRestaurant(Long restaurantId) {

        logger.info("Fetching menu items for restaurantId: {}", restaurantId);

        List<MenuItemResponse> response = menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .filter(item -> !Boolean.TRUE.equals(item.getDeleted()))
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        logger.info("Menu items fetched successfully for restaurantId: {}", restaurantId);

        return response;
    }

    /**
     * Soft deletes a menu item by its ID (marks as deleted).
     */
    public void delete(Long id) {

        logger.info("Deleting menu item with id: {}", id);

        MenuItem item = menuItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Menu item not found with id: {}", id);
                    return new RuntimeException("Menu item not found");
                });

        item.setDeleted(true);
        menuItemRepository.save(item);

        logger.info("Menu item soft deleted successfully with id: {}", id);
    }

    /**
     * Converts MenuItem entity to MenuItemResponse DTO.
     */
    private MenuItemResponse mapToResponse(MenuItem item) {

        logger.debug("Mapping MenuItem to MenuItemResponse with id: {}", item.getId());

        return new MenuItemResponse(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getDescription(),
                item.getAvailable(),
                item.getCategory().getId());
    }
}