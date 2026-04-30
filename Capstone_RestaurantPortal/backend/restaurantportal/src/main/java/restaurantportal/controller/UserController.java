package restaurantportal.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import restaurantportal.dto.RegisterRequest;
import restaurantportal.dto.UserResponse;
import restaurantportal.dto.LoginRequest;
import restaurantportal.dto.LoginResponse;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.SecurityUtil;
import restaurantportal.service.UserService;

/**
 * Handles user-related operations like registration and login.
 */
@CrossOrigin
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {

        log.info("Registering new user with email: {}", request.getEmail());

        UserResponse response = userService.register(request);

        log.info("User registered successfully with email: {}", request.getEmail());

        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates user and returns JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        log.info("Login attempt for email: {}", request.getEmail());

        String token = userService.login(request.getEmail(), request.getPassword());

        log.info("Login successful for email: {}", request.getEmail());

        return ResponseEntity.ok(new LoginResponse(token));
    }

    /**
     * Returns currently logged-in user details.
     */
    @GetMapping("/currentUser")
    public User currentUser() {

        log.info("Fetching current logged-in user");

        String email = SecurityUtil.getCurrentUserEmail();

        log.debug("Resolved current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found for email: {}", email);
                    return new RuntimeException("User not found");
                });

        log.info("Current user fetched successfully: {}", email);

        return user;
    }
}