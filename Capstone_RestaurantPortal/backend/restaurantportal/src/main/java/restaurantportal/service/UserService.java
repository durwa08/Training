package restaurantportal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import restaurantportal.dto.RegisterRequest;
import restaurantportal.dto.UserResponse;
import restaurantportal.entity.Role;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;
import restaurantportal.security.JwtUtil;

/**
 * Service responsible for user-related business logic such as registration and login.
 * Handles password encryption and JWT token generation.
 */
@Service
public class UserService {
    /**
     * UserService manages user registration and authentication.
     * It ensures secure password storage and generates JWT tokens for authenticated users.
     */
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        logger.info("UserService initialized");
    }

    /**
     * Registers a new user and stores encrypted password in database.
     */
    @Transactional
    public UserResponse register(RegisterRequest request) {

        logger.info("Registering new user");
        logger.debug("RegisterRequest: {}", request);

        userRepository.findByEmail(request.getEmail())
                .ifPresent(u -> {
                    logger.error("Email already registered: {}", request.getEmail());
                    throw new IllegalArgumentException("Email already registered");
                });

        Role role = Role.valueOf(request.getRole().toUpperCase());

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

        logger.info("User registered successfully with id: {}", savedUser.getId());

        return new UserResponse(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail(),
                savedUser.getPhoneNumber(),
                savedUser.getRole().name(),
                savedUser.getWalletBalance()
        );
    }

    /**
     * Authenticates user and generates JWT token on successful login.
     */
    public String login(String email, String password) {

        logger.info("Login attempt for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Invalid login attempt for email: {}", email);
                    return new IllegalArgumentException("Invalid email or password");
                });

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.error("Password mismatch for email: {}", email);
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        logger.info("User logged in successfully for email: {}", email);

        return token;
    }
}