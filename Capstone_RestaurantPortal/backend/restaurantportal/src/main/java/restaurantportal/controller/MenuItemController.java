package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.MenuItemRequest;
import restaurantportal.dto.MenuItemResponse;
import restaurantportal.service.MenuItemService;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
// Controller to handle menu item related endpoints like create menu item for restaurant get menu by restaurant etc.
public class MenuItemController {

    // we need to inject menu item service in this controller to handle the business logic related to menu items.
    private final MenuItemService service;

    // Constructor injection of menu item service
    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    // create menu item
    @PostMapping("/{categoryId}")
    public MenuItemResponse create(@PathVariable Long categoryId,@Valid
                                   @RequestBody MenuItemRequest request) {
        return service.create(categoryId, request);
    }

    // get menu by restaurant
    @GetMapping("/restaurant/{id}")
    public List<MenuItemResponse> getByRestaurant(@PathVariable Long id) {
        return service.getByRestaurant(id);
    }

    // delete menu item (added for completeness)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}