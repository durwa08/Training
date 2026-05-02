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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        logger.info("UserController initialized");
    }

    /**
     * Registers a new user.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Received request to register user");
        logger.debug("RegisterRequest: {}", request);
        UserResponse response = userService.register(request);
        logger.info("User registered successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates user and returns JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        logger.info("Received login request for email: {}", request.getEmail());
        String token = userService.login(request.getEmail(), request.getPassword());
        logger.info("User logged in successfully for email: {}", request.getEmail());
        return ResponseEntity.ok(new LoginResponse(token));
    }
    /**
     * Retrieves the currently authenticated user's details.
     */
    @GetMapping("/currentUser")
    public User currentUser() {

        logger.info("Fetching current user");

        String email = SecurityUtil.getCurrentUserEmail();
        logger.debug("Current user email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });

        logger.info("Current user fetched successfully");

        return user;
    }
}