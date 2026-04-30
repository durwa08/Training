package restaurantportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurantportal.dto.*;
import restaurantportal.entity.*;
import restaurantportal.repository.*;
import restaurantportal.security.SecurityUtil;

import java.util.List;

/**
 * Service layer responsible for cart-related business logic.
 */
@Service
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       MenuItemRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * Fetches existing cart for user or creates a new one if not present.
     */
    private Cart getCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    log.info("Creating new cart for user: {}", user.getEmail());

                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }

    /**
     * Adds an item to the user's cart.
     */
    @Transactional
    public CartResponse addToCart(AddToCartRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();
        log.info("Add to cart request received for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found: {}", email);
                    return new RuntimeException("User not found");
                });

        Cart cart = getCart(user);

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> {
                    log.error("Menu item not found: {}", request.getMenuItemId());
                    return new RuntimeException("Menu item not found");
                });

        log.info("Adding item {} to cart of user {}", menuItem.getName(), email);

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getMenuItem().getId().equals(menuItem.getId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            log.info("Updating quantity for existing item in cart");
            existing.setQuantity(request.getQuantity());
        } else {
            log.info("Adding new item to cart");
            CartItem item = new CartItem();
            item.setMenuItem(menuItem);
            item.setQuantity(request.getQuantity());
            item.setPrice(menuItem.getPrice());
            item.setCart(cart);
            cart.getItems().add(item);
        }

        recalculate(cart);

        log.info("Cart updated successfully for user: {}", email);

        return mapToResponse(cartRepository.save(cart));
    }

    /**
     * Retrieves the cart of the currently logged-in user.
     */
    public CartResponse getMyCart() {

        String email = SecurityUtil.getCurrentUserEmail();
        log.info("Fetching cart for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found: {}", email);
                    return new RuntimeException("User not found");
                });

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.error("Cart not found for user: {}", email);
                    return new RuntimeException("Cart not found");
                });

        return mapToResponse(cart);
    }

    /**
     * Removes a specific item from the cart.
     */
    @Transactional
    public void removeItem(Long id) {

        Cart cart = getCartEntity();

        log.info("Removing item {} from cart", id);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Cart item not found: {}", id);
                    return new RuntimeException("Item not found");
                });

        cart.getItems().remove(item);

        recalculate(cart);

        cartRepository.save(cart);

        log.info("Item removed successfully from cart");
    }

    /**
     * Clears all items from the user's cart.
     */
    @Transactional
    public void clearCart() {

        Cart cart = getCartEntity();

        log.info("Clearing cart for user");

        cart.getItems().clear();
        cart.setTotalAmount(0.0);

        cartRepository.save(cart);

        log.info("Cart cleared successfully");
    }

    /**
     * Helper method to fetch cart entity of logged-in user.
     */
    private Cart getCartEntity() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    /**
     * Recalculates total cart amount.
     */
    private void recalculate(Cart cart) {
        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        cart.setTotalAmount(total);
    }

    /**
     * Converts Cart entity to CartResponse DTO.
     */
    private CartResponse mapToResponse(Cart cart) {

        return new CartResponse(
                cart.getId(),
                cart.getTotalAmount(),
                cart.getItems().stream()
                        .map(i -> new CartItemResponse(
                                i.getId(),
                                i.getMenuItem().getName(),
                                i.getPrice(),
                                i.getQuantity())).toList());
    }
}