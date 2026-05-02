package restaurantportal.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.MenuItemRequest;
import restaurantportal.dto.MenuItemResponse;
import restaurantportal.service.MenuItemService;

import java.util.List;

/**
 * Controller for handling menu item related operations.
 * Provides APIs to create, fetch, update, and delete menu items.
 */
@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemController.class);

    private final MenuItemService menuItemService;

    /**
     * Constructor-based dependency injection for MenuItemService.
     *
     */
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
        logger.info("MenuItemController initialized");
    }

    /**
     * Creates a new menu item under a specific category.
     */
    @PostMapping("/{categoryId}")
    public MenuItemResponse create(@PathVariable Long categoryId,
                                   @RequestBody MenuItemRequest request) {
        logger.info("Received request to create menu item for categoryId: {}", categoryId);
        logger.debug("MenuItemRequest: {}", request);
        MenuItemResponse response = menuItemService.create(categoryId, request);
        logger.info("Menu item created successfully for categoryId: {}", categoryId);
        return response;
    }

    /**
     * Updates an existing menu item.
     */
    @PutMapping("/{id}")
    public MenuItemResponse update(@PathVariable Long id,
                                   @RequestBody MenuItemRequest request) {
        logger.info("Received request to update menu item with id: {}", id);
        logger.debug("MenuItemRequest: {}", request);
        MenuItemResponse response = menuItemService.update(id, request);
        logger.info("Menu item updated successfully with id: {}", id);
        return response;
    }

    /**
     * Fetches all menu items for a specific restaurant.
     */
    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItemResponse> getByRestaurant(@PathVariable Long restaurantId) {
        logger.info("Received request to fetch menu items for restaurantId: {}", restaurantId);
        List<MenuItemResponse> response = menuItemService.getByRestaurant(restaurantId);
        logger.info("Menu items fetched successfully for restaurantId: {}", restaurantId);
        return response;
    }

    /**
     * Deletes a menu item by its ID.
     *
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Received request to delete menu item with id: {}", id);
        menuItemService.delete(id);
        logger.info("Menu item deleted successfully with id: {}", id);
    }
}