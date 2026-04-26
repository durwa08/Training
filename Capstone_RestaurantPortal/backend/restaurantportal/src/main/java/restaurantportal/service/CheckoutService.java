package restaurantportal.service;
import org.springframework.stereotype.Service;
import restaurantportal.dto.CartItemResponse;
import restaurantportal.dto.CheckoutResponse;
import restaurantportal.entity.Cart;
import restaurantportal.entity.User;
import restaurantportal.repository.CartRepository;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;
import java.util.stream.Collectors;

@Service
//CheckoutService handles the checkout process, calculating totals and checking wallet balance
public class CheckoutService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public CheckoutService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public CheckoutResponse checkout() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        double total = cart.getTotalAmount();
        double wallet = user.getWalletBalance();

        boolean canPlace = wallet >= total;

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