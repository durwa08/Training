package restaurantportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import restaurantportal.dto.CartItemResponse;
import restaurantportal.dto.CheckoutResponse;
import restaurantportal.entity.Cart;
import restaurantportal.entity.User;
import restaurantportal.repository.CartRepository;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;

import java.util.stream.Collectors;

/**
 * Service responsible for handling checkout logic.
 * It verifies cart details and checks wallet balance before order placement.
 */
@Service
public class CheckoutService {

    private static final Logger log = LoggerFactory.getLogger(CheckoutService.class);

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public CheckoutService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    /**
     * Processes checkout for the logged-in user.
     * Calculates total amount and verifies wallet balance.
     */
    public CheckoutResponse checkout() {

        String email = SecurityUtil.getCurrentUserEmail();
        log.info("Checkout initiated for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found during checkout: {}", email);
                    return new RuntimeException("User not found");
                });

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> {
                    log.error("Cart not found for user: {}", email);
                    return new RuntimeException("Cart not found");
                });

        if (cart.getItems().isEmpty()) {
            log.warn("Checkout failed - cart is empty for user: {}", email);
            throw new RuntimeException("Cart is empty");
        }

        double total = cart.getTotalAmount();
        double wallet = user.getWalletBalance();

        boolean canPlace = wallet >= total;

        log.info("Checkout calculated - total: {}, wallet: {}, canPlaceOrder: {}", total, wallet, canPlace);

        return new CheckoutResponse(
                total,
                wallet,
                canPlace,
                cart.getItems().stream()
                        .map(i -> new CartItemResponse(
                                i.getId(),
                                i.getMenuItem().getName(),
                                i.getPrice(),
                                i.getQuantity()))
                        .collect(Collectors.toList()));
    }
}