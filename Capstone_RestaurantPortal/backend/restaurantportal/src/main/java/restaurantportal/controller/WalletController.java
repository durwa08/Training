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

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);

    /** Service layer for wallet operations */
    private final WalletService service;

    /** Constructor injection for WalletService */
    public WalletController(WalletService service) {
        this.service = service;
        logger.info("WalletController initialized");
    }

    /**
     * Adds money to the user's wallet.
     */
    @PostMapping("/add")
    public WalletResponse addMoney(@Valid @RequestBody AddMoneyRequest request) {

        logger.info("Received request to add money to wallet");
        logger.debug("AddMoneyRequest: {}", request);

        WalletResponse response = service.addMoney(request);

        logger.info("Money added to wallet successfully");

        return response;
    }

    /**
     * Returns the current wallet balance of the user.
     */
    @GetMapping
    public WalletResponse getBalance() {

        logger.info("Received request to fetch wallet balance");

        WalletResponse response = service.getBalance();

        logger.info("Wallet balance fetched successfully");

        return response;
    }
}