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
    /**
     * CheckoutService handles the checkout process for users.
     * It calculates the total amount of the cart and checks if the user has
     *
     * sufficient wallet balance to place the order.
     */
    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public CheckoutService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        logger.info("CheckoutService initialized");
    }

    /**
     * Processes checkout for the logged-in user.
     * Calculates total amount and verifies wallet balance.
     */
    public CheckoutResponse checkout() {

        logger.info("Processing checkout");

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

        if (cart.getItems().isEmpty()) {
            logger.warn("Cart is empty for user: {}", email);
            throw new RuntimeException("Cart is empty");
        }

        double total = cart.getTotalAmount();
        double wallet = user.getWalletBalance();

        logger.debug("Cart total: {}, Wallet balance: {}", total, wallet);

        boolean canPlace = wallet >= total;

        logger.info("Checkout evaluated. Can place order: {}", canPlace);

        return new CheckoutResponse(
                total,
                wallet,
                canPlace,
                cart.getItems().stream()
                        .map(i -> new CartItemResponse(
                                i.getId(),
                                i.getMenuItem().getName(),
                                i.getPrice(),
                                i.getQuantity())).collect(Collectors.toList()));
    }
}