package restaurantportal.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import restaurantportal.dto.AddressDTO;
import restaurantportal.dto.AddressRequestDTO;
import restaurantportal.service.AddressService;

import java.util.List;

/**
 * REST controller for Address management.
 * All endpoints require the user to be authenticated.
 *
 * Base URL: /api/addresses
 */
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Slf4j
public class AddressController {

    private final AddressService addressService;

    /**
     * Add a new address for a user.
     *
     * POST /api/addresses/{userId}
     */
    @PostMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDTO> addAddress(
            @PathVariable Long userId,
            @Valid @RequestBody AddressRequestDTO requestDTO) {

        log.info("POST /api/addresses/{} - addAddress", userId);
        AddressDTO created = addressService.addAddress(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get all addresses for a user.
     *
     * GET /api/addresses/{userId}
     */
    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<AddressDTO>> getAddressesByUser(@PathVariable Long userId) {
        log.info("GET /api/addresses/{} - getAddressesByUser", userId);
        List<AddressDTO> addresses = addressService.getAddressesByUser(userId);
        return ResponseEntity.ok(addresses);
    }

    /**
     * Get a specific address by its ID.
     *
     * GET /api/addresses/{userId}/{addressId}
     */
    @GetMapping("/{userId}/{addressId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDTO> getAddressById(
            @PathVariable Long userId,
            @PathVariable Long addressId) {

        log.info("GET /api/addresses/{}/{} - getAddressById", userId, addressId);
        AddressDTO address = addressService.getAddressById(userId, addressId);
        return ResponseEntity.ok(address);
    }

    /**
     * Update an existing address.
     *
     * PUT /api/addresses/{userId}/{addressId}
     *
     */
    @PutMapping("/{userId}/{addressId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<AddressDTO> updateAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequestDTO requestDTO) {

        log.info("PUT /api/addresses/{}/{} - updateAddress", userId, addressId);
        AddressDTO updated = addressService.updateAddress(userId, addressId, requestDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete an address.
     *
     * DELETE /api/addresses/{userId}/{addressId}
     *
     */
    @DeleteMapping("/{userId}/{addressId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {

        log.info("DELETE /api/addresses/{}/{} - deleteAddress", userId, addressId);
        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }
}