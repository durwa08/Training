package restaurantportal.controller;

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

    private final MenuItemService menuItemService;

    /**
     * Constructor-based dependency injection for MenuItemService.
     *
     * @param menuItemService service layer for menu item operations
     */
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    /**
     * Creates a new menu item under a specific category.
     *
     * @param categoryId ID of the category
     * @param request menu item details
     * @return created menu item response
     */
    @PostMapping("/{categoryId}")
    public MenuItemResponse create(@PathVariable Long categoryId,
                                   @RequestBody MenuItemRequest request) {
        return menuItemService.create(categoryId, request);
    }

    /**
     * Fetches all menu items for a specific restaurant.
     *
     * @param restaurantId ID of the restaurant
     * @return list of menu items
     */
    @GetMapping("/restaurant/{restaurantId}")
    public List<MenuItemResponse> getByRestaurant(@PathVariable Long restaurantId) {
        return menuItemService.getByRestaurant(restaurantId);
    }

    /**
     * Deletes a menu item by its ID.
     *
     * @param id menu item ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        menuItemService.delete(id);
    }
}