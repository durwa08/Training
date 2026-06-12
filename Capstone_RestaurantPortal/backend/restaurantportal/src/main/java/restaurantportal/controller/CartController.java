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

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);

    /** Service layer for cart operations */
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
        logger.info("CartController initialized");
    }

    /**
     * Adds an item to the user's cart.
     */
    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody AddToCartRequest request) {
        logger.info("Received request to add item to cart");
        logger.debug("AddToCartRequest: {}", request);
        CartResponse response = cartService.addToCart(request);
        logger.info("Item added to cart successfully");
        return response;
    }

    /**
     * Returns the current user's cart details.
     */
    @GetMapping
    public CartResponse getCart() {
        logger.info("Received request to fetch cart");
        CartResponse response = cartService.getMyCart();
        logger.info("Cart fetched successfully");
        return response;
    }

    /**
     * Removes a specific item from cart by item id.
     */
    @DeleteMapping("/{id}")
    public String remove(@PathVariable Long id) {
        logger.info("Received request to remove item from cart with id: {}", id);
        cartService.removeItem(id);
        logger.info("Item removed from cart successfully with id: {}", id);
        return "Item removed";
    }

    /**
     * Clears all items from the user's cart.
     */
    @DeleteMapping
    public String clear() {
        logger.info("Received request to clear cart");
        cartService.clearCart();
        logger.info("Cart cleared successfully");
        return "Cart cleared";
    }
}