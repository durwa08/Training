package restaurantportal.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.*;
import restaurantportal.service.CartService;

/**
 * Handles cart related operations for the user.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Adds an item to the user's cart.
     */
    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody AddToCartRequest request) {

        log.info("Adding item to cart: {}", request);

        CartResponse response = cartService.addToCart(request);

        log.info("Item added to cart successfully");

        return response;
    }

    /**
     * Returns the current user's cart details.
     */
    @GetMapping
    public CartResponse getCart() {

        log.info("Fetching current user cart");

        CartResponse response = cartService.getMyCart();

        log.info("Cart fetched successfully");

        return response;
    }

    /**
     * Removes a specific item from cart by item id.
     */
    @DeleteMapping("/{id}")
    public String remove(@PathVariable Long id) {

        log.info("Removing item from cart with id: {}", id);

        cartService.removeItem(id);

        log.info("Item removed successfully");

        return "Item removed";
    }

    /**
     * Clears all items from the user's cart.
     */
    @DeleteMapping
    public String clear() {

        log.info("Clearing cart for current user");

        cartService.clearCart();

        log.info("Cart cleared successfully");

        return "Cart cleared";
    }
}