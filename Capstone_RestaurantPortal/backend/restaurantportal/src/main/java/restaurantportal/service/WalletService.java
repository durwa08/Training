package restaurantportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(WalletService.class);

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
        log.info("Wallet top-up request initiated by user: {}, amount: {}", email, request.getAmount());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found during wallet top-up: {}", email);
                    return new RuntimeException("User not found");
                });

        double oldBalance = user.getWalletBalance();

        user.setWalletBalance(oldBalance + request.getAmount());

        userRepository.save(user);

        log.info("Wallet updated successfully for user: {} | oldBalance: {}, newBalance: {}",
                email, oldBalance, user.getWalletBalance());

        return new WalletResponse(user.getWalletBalance());
    }

    /**
     * Retrieves wallet balance of the logged-in user.
     */
    public WalletResponse getBalance() {

        String email = SecurityUtil.getCurrentUserEmail();
        log.info("Wallet balance request for user: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found during balance fetch: {}", email);
                    return new RuntimeException("User not found");
                });

        log.info("Wallet balance fetched for user: {} | balance: {}", email, user.getWalletBalance());

        return new WalletResponse(user.getWalletBalance());
    }
}