package restaurantportal.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import restaurantportal.dto.RegisterRequest;
import restaurantportal.dto.UserResponse;
import restaurantportal.entity.Role;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;

@Service // Handles all the business logic service bean (java object)
public class UserService {

    //Dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //Constructor injection- it makes dependencies final(immutable) easier to test
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional //This method runs inside a database transaction and says either all or none transactions
    public UserResponse register(RegisterRequest request) {

        // 1. Check duplicate email
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Email already registered");
                });

        // 2. Validate role
        Role role = parseRole(request.getRole());

        // 3. Create User
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(role)
                .walletBalance(1000.0)
                .build();

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);
    }

    // Clean role parsing
    private Role parseRole(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    // Entity → DTO
    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getRole().name(),
                user.getWalletBalance()
        );
    }
}