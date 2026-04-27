package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.MenuItemRequest;
import restaurantportal.dto.MenuItemResponse;
import restaurantportal.service.MenuItemService;

import java.util.List;

/**
 * Handles menu item related APIs like create, fetch, and delete menu items.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/menu")
public class MenuItemController {

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    /**
     * Creates a new menu item under a category.
     */
    @PostMapping("/{categoryId}")
    public MenuItemResponse create(@PathVariable Long categoryId,
                                   @Valid @RequestBody MenuItemRequest request) {
        return service.create(categoryId, request);
    }

    /**
     * Fetches all menu items for a given restaurant.
     */
    @GetMapping("/restaurant/{id}")
    public List<MenuItemResponse> getByRestaurant(@PathVariable Long id) {
        return service.getByRestaurant(id);
    }

    /**
     * Deletes a menu item by its id.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}