package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.*;
import restaurantportal.service.WalletService;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    private final WalletService service;

    public WalletController(WalletService service) {
        this.service = service;
    }

    //  add money
    @PostMapping("/add")
    public WalletResponse addMoney(@Valid @RequestBody AddMoneyRequest request) {
        return service.addMoney(request);
    }

    //  get balance
    @GetMapping
    public WalletResponse getBalance() {
        return service.getBalance();
    }
}