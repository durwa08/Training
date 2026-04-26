package restaurantportal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurantportal.dto.*;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;

@Service
public class WalletService {

    private final UserRepository userRepository;

    public WalletService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //  ADD MONEY
    @Transactional
    public WalletResponse addMoney(AddMoneyRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setWalletBalance(
                user.getWalletBalance() + request.getAmount()
        );

        return new WalletResponse(user.getWalletBalance());
    }

    // GET BALANCE
    public WalletResponse getBalance() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new WalletResponse(user.getWalletBalance());
    }
}