package restaurantportal.controller;

import jakarta.validation.Valid;
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
        UserResponse response = userService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Authenticates user and returns JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }
    @GetMapping("/currentUser")
    public User currentUser() {

        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user;
    }
}