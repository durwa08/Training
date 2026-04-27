package restaurantportal.service;

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

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getCart(user);

        MenuItem menuItem = menuItemRepository.findById(request.getMenuItemId())
                .orElseThrow(() -> new RuntimeException("Menu item not found"));

        CartItem existing = cart.getItems().stream()
                .filter(i -> i.getMenuItem().getId().equals(menuItem.getId()))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
        } else {
            CartItem item = new CartItem();
            item.setMenuItem(menuItem);
            item.setQuantity(request.getQuantity());
            item.setPrice(menuItem.getPrice());
            item.setCart(cart);
            cart.getItems().add(item);
        }

        recalculate(cart);

        return mapToResponse(cartRepository.save(cart));
    }

    /**
     * Retrieves the cart of the currently logged-in user.
     */
    public CartResponse getMyCart() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return mapToResponse(cart);
    }

    /**
     * Removes a specific item from the cart.
     */
    @Transactional
    public void removeItem(Long id) {

        Cart cart = getCartEntity();

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        cart.getItems().remove(item);

        recalculate(cart);

        cartRepository.save(cart);
    }

    /**
     * Clears all items from the user's cart.
     */
    @Transactional
    public void clearCart() {

        Cart cart = getCartEntity();

        cart.getItems().clear();
        cart.setTotalAmount(0.0);

        cartRepository.save(cart);
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