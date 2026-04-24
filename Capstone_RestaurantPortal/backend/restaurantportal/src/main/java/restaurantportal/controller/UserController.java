package restaurantportal.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import restaurantportal.dto.RegisterRequest;
import restaurantportal.dto.UserResponse;
import restaurantportal.dto.LoginRequest;
import restaurantportal.dto.LoginResponse;
import restaurantportal.service.UserService;

@RestController
@RequestMapping("/api/users")
//This class is responsible for handling user related endpoints like registration and login.
// It uses UserService to perform the business logic related to users.
public class UserController {

    private final UserService userService;

    // constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //  REGISTER
    @PostMapping("/register") // endpoint: POST /api/users/register
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);

        return ResponseEntity.ok(response); // return user details
    }

    // LOGIN
    @PostMapping("/login") // endpoint: POST /api/users/login
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        // call service to generate JWT token
        String token = userService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(new LoginResponse(token)); // return token
    }
}