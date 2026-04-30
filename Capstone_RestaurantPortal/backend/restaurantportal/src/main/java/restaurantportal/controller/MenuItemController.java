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
 * Provides APIs to create, fetch, and delete menu items.
 */
@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private static final Logger log = LoggerFactory.getLogger(MenuItemController.class);

    private final MenuItemService menuItemService;

    /**
     * Constructor-based dependency injection for MenuItemService.
     */
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    /**
     * Creates a new menu item under a specific category.
     */
    @PostMapping("/{categoryId}")
    public MenuItemResponse create(@PathVariable Long categoryId,
                                   @RequestBody MenuItemRequest request) {

        log.info("Creating menu item for categoryId: {}", categoryId);

        MenuItemResponse response = menuItemService.create(categoryId, request);

        log.info("Menu item created successfully for categoryId: {}", categoryId);

        return response;
    }

    /**
     * Fetches all menu items for a specific restaurant.
     */
    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItemResponse> getByRestaurant(@PathVariable Long restaurantId) {

        log.info("Fetching menu items for restaurantId: {}", restaurantId);

        List<MenuItemResponse> items = menuItemService.getByRestaurant(restaurantId);

        log.info("Fetched {} menu items for restaurantId: {}", items.size(), restaurantId);

        return items;
    }

    /**
     * Deletes a menu item by its ID.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        log.info("Deleting menu item with id: {}", id);

        menuItemService.delete(id);

        log.info("Menu item deleted successfully with id: {}", id);
    }
}