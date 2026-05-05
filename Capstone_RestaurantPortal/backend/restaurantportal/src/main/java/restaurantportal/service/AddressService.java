package restaurantportal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import restaurantportal.dto.AddressDTO;
import restaurantportal.dto.AddressRequestDTO;
import restaurantportal.entity.Address;
import restaurantportal.entity.User;
import restaurantportal.repository.AddressRepository;
import restaurantportal.repository.UserRepository;
import restaurantportal.exception.UserNotFoundException;
import restaurantportal.exception.UnauthorizedAccessException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Address management.
 * Handles all business logic related to user addresses.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    /**
     * Add a new address for the given user.
     */
    @Transactional
    public AddressDTO addAddress(Long userId, AddressRequestDTO requestDTO) {
        log.info("Adding address for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Address address = Address.builder()
                .street(requestDTO.getStreet())
                .city(requestDTO.getCity())
                .state(requestDTO.getState())
                .pincode(requestDTO.getPincode())
                .user(user)
                .build();

        Address saved = addressRepository.save(address);
        log.info("Address saved successfully with id: {}", saved.getId());

        return mapToDTO(saved);
    }

    /**
     * Retrieve all addresses for the given user.
     */
    public List<AddressDTO> getAddressesByUser(Long userId) {
        log.info("Fetching addresses for userId: {}", userId);

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }

        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get a single address by ID (with ownership check).
     */
    public AddressDTO getAddressById(Long userId, Long addressId) {
        log.info("Fetching addressId: {} for userId: {}", addressId, userId);

        Address address = getAddressAndVerifyOwnership(userId, addressId);
        return mapToDTO(address);
    }

    /**
     * Update an existing address.
     */
    @Transactional
    public AddressDTO updateAddress(Long userId, Long addressId, AddressRequestDTO requestDTO) {
        log.info("Updating addressId: {} for userId: {}", addressId, userId);

        Address address = getAddressAndVerifyOwnership(userId, addressId);

        address.setStreet(requestDTO.getStreet());
        address.setCity(requestDTO.getCity());
        address.setState(requestDTO.getState());
        address.setPincode(requestDTO.getPincode());

        Address updated = addressRepository.save(address);
        log.info("Address updated successfully for id: {}", updated.getId());

        return mapToDTO(updated);
    }

    /**
     * Delete an address by ID (with ownership check).
     */
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        log.info("Deleting addressId: {} for userId: {}", addressId, userId);

        Address address = getAddressAndVerifyOwnership(userId, addressId);
        addressRepository.delete(address);

        log.info("Address deleted successfully for id: {}", addressId);
    }

    // ──────────────────────── helpers ────────────────────────

    /**
     * Fetch address by ID and verify it belongs to the given user.
     */
    private Address getAddressAndVerifyOwnership(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new UserNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getId().equals(userId)) {
            log.warn("Unauthorized access: userId {} tried to access addressId {}", userId, addressId);
            throw new UnauthorizedAccessException("You are not authorized to access this address");
        }

        return address;
    }

    /**
     * Map Address entity to AddressDTO.
     */
    private AddressDTO mapToDTO(Address address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .pincode(address.getPincode())
                .userId(address.getUser().getId())
                .build();
    }
}