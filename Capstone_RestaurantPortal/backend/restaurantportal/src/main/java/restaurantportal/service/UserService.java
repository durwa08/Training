package restaurantportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import restaurantportal.dto.RegisterRequest;
import restaurantportal.entity.Role;
import restaurantportal.entity.User;
import restaurantportal.repository.UserRepository;

@Service //it is a business layer
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Stores password encrypted
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(RegisterRequest request) {

        // Convert DTO → Entity
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .walletBalance(1000.0)
                .build();

        return userRepository.save(user);
    }
}