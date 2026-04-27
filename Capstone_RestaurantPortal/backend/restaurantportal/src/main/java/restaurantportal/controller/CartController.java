package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.*;
import restaurantportal.service.CartService;

/**
 * Handles cart related operations for the user.
 */
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * Adds an item to the user's cart.
     */
    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody AddToCartRequest request) {
        return cartService.addToCart(request);
    }

    /**
     * Returns the current user's cart details.
     */
    @GetMapping
    public CartResponse getCart() {
        return cartService.getMyCart();
    }

    /**
     * Removes a specific item from cart by item id.
     */
    @DeleteMapping("/{id}")
    public String remove(@PathVariable Long id) {
        cartService.removeItem(id);
        return "Item removed";
    }

    /**
     * Clears all items from the user's cart.
     */
    @DeleteMapping
    public String clear() {
        cartService.clearCart();
        return "Cart cleared";
    }
}