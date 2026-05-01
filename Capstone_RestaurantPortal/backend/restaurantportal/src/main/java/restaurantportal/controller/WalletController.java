package restaurantportal.controller;

import jakarta.validation.Valid;
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

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    /**
     * Adds money to the user's wallet.
     */
    @PostMapping("/add")
    public WalletResponse addMoney(@Valid @RequestBody AddMoneyRequest request) {
        return service.addMoney(request);
    }

    /**
     * Returns the current wallet balance of the user.
     */
    @GetMapping
    public WalletResponse getBalance() {
        return service.getBalance();
    }
}