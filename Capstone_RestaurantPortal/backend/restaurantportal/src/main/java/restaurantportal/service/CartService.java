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
    /**
     * CartService handles all operations related to the shopping cart.
     */
    private static final Logger logger = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;

    public CartService(CartRepository cartRepository,
                       UserRepository userRepository,
                       MenuItemRepository menuItemRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.menuItemRepository = menuItemRepository;
        logger.info("CartService initialized");
    }

    /**
     * Fetches existing cart for user or creates a new one if not present.
     */
    private Cart getCart(User user) {
        logger.debug("Fetching cart for user");
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    logger.info("Cart not found, creating new cart");
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

        logger.info("Adding item to cart");
        logger.debug("AddToCartRequest: {}", request);

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        Cart cart = getCart(user);

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> {
                    logger.error("Menu item not found with id: {}", request.getMenuItemId());
                    return new RuntimeException("Menu item not found");
                });

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getMenuItem().getId().equals(menuItem.getId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            logger.debug("Item already exists in cart, updating quantity");
            existing.setQuantity(request.getQuantity());
        } else {
            logger.debug("Adding new item to cart");
            CartItem item = new CartItem();
            item.setMenuItem(menuItem);
            item.setId(menuItem.getId());
            item.setQuantity(request.getQuantity());
            item.setPrice(menuItem.getPrice());
            item.setCart(cart);
            cart.getItems().add(item);
        }

        recalculate(cart);

        CartResponse response = mapToResponse(cartRepository.save(cart));
        logger.info("Item added to cart successfully");

        return response;
    }

    /**
     * Retrieves the cart of the currently logged-in user.
     */
    public CartResponse getMyCart() {

        logger.info("Fetching current user's cart");

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> {
                    logger.error("Cart not found for user");
                    return new RuntimeException("Cart not found");
                });

        logger.info("Cart fetched successfully");

        return mapToResponse(cart);
    }

    /**
     * Removes a specific item from the cart.
     */
    @Transactional
    public void removeItem(Long id) {

        logger.info("Removing item from cart with id: {}", id);

        Cart cart = getCartEntity();

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> {
                    logger.error("Item not found in cart with id: {}", id);
                    return new RuntimeException("Item not found");
                });

        cart.getItems().remove(item);

        recalculate(cart);

        cartRepository.save(cart);

        logger.info("Item removed successfully from cart with id: {}", id);
    }

    /**
     * Clears all items from the user's cart.
     */
    @Transactional
    public void clearCart() {

        logger.info("Clearing cart");

        Cart cart = getCartEntity();

        cart.getItems().clear();
        cart.setTotalAmount(0.0);

        cartRepository.save(cart);

        logger.info("Cart cleared successfully");
    }

    /**
     * Helper method to fetch cart entity of logged-in user.
     */
    private Cart getCartEntity() {

        logger.debug("Fetching cart entity for current user");

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        return cartRepository.findByUser(user)
                .orElseThrow(() -> {
                    logger.error("Cart not found for user");
                    return new RuntimeException("Cart not found");
                });
    }

    /**
     * Recalculates total cart amount.
     */
    private void recalculate(Cart cart) {
        logger.debug("Recalculating cart total");
        double total = cart.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        cart.setTotalAmount(total);
    }

    /**
     * Converts Cart entity to CartResponse DTO.
     */
    private CartResponse mapToResponse(Cart cart) {

        logger.debug("Mapping Cart entity to CartResponse");

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