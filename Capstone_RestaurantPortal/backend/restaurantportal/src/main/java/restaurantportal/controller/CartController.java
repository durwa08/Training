package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.*;
import restaurantportal.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    //  add item to cart
    @PostMapping
    public CartResponse addToCart(@Valid @RequestBody AddToCartRequest request) {
        return cartService.addToCart(request);
    }

    // get current user cart
    @GetMapping
    public CartResponse getCart() {
        return cartService.getMyCart();
    }

    //  remove item
    @DeleteMapping("/{id}")
    public String remove(@PathVariable Long id) {
        cartService.removeItem(id);
        return "Item removed";
    }

    // clear cart
    @DeleteMapping
    public String clear() {
        cartService.clearCart();
        return "Cart cleared";
    }
}