package restaurantportal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurantportal.dto.*;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;

/**
 * Service responsible for wallet-related operations such as adding money and fetching balance.
 */
@Service
public class WalletService {

    private final UserRepository userRepository;

    public WalletService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds money to the logged-in user's wallet.
     */
    @Transactional
    public WalletResponse addMoney(AddMoneyRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setWalletBalance(
                user.getWalletBalance() + request.getAmount()
        );

        userRepository.save(user);

        return new WalletResponse(user.getWalletBalance());
    }

    /**
     * Retrieves wallet balance of the logged-in user.
     */
    public WalletResponse getBalance() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new WalletResponse(user.getWalletBalance());
    }
}