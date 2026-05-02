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
    /**
     * UserRepository is injected to perform database operations related to the user's wallet balance.
     * */
    private static final Logger logger = LoggerFactory.getLogger(WalletService.class);

    private final UserRepository userRepository;

    public WalletService(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger.info("WalletService initialized");
    }

    /**
     * Adds money to the logged-in user's wallet.
     */
    @Transactional
    public WalletResponse addMoney(AddMoneyRequest request) {

        logger.info("Adding money to wallet");
        logger.debug("AddMoneyRequest: {}", request);

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        user.setWalletBalance(
                user.getWalletBalance() + request.getAmount()
        );

        userRepository.save(user);

        logger.info("Money added successfully. Updated balance: {}", user.getWalletBalance());

        return new WalletResponse(user.getWalletBalance());
    }

    /**
     * Retrieves wallet balance of the logged-in user.
     */
    public WalletResponse getBalance() {

        logger.info("Fetching wallet balance");

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        logger.info("Wallet balance fetched successfully");

        return new WalletResponse(user.getWalletBalance());
    }
}