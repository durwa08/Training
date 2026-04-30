package restaurantportal.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.*;
import restaurantportal.service.WalletService;

/**
 * Handles wallet-related operations like adding money and checking balance.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private static final Logger log = LoggerFactory.getLogger(WalletController.class);

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    /**
     * Adds money to the user's wallet.
     */
    @PostMapping("/add")
    public WalletResponse addMoney(@Valid @RequestBody AddMoneyRequest request) {

        log.info("Adding money to wallet: {}", request);

        WalletResponse response = service.addMoney(request);

        log.info("Money added successfully to wallet");

        return response;
    }

    /**
     * Returns the current wallet balance of the user.
     */
    @GetMapping
    public WalletResponse getBalance() {

        log.info("Fetching wallet balance");

        WalletResponse response = service.getBalance();

        log.info("Wallet balance fetched successfully");

        return response;
    }
}