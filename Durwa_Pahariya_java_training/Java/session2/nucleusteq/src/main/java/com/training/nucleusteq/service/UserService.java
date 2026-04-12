package com.training.nucleusteq.service;

import com.training.nucleusteq.model.User;
import com.training.nucleusteq.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    // Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    // Create new user
    public String createUser(User user) {
        userRepository.save(user);
        return "User created successfully";
    }

    // Delete user
    public String deleteUser(int id) {
        boolean isDeleted = userRepository.deleteById(id);
        if (isDeleted) {
            return "User deleted successfully";
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    // Update user
    public String updateUser(int id, User user) {
        boolean isUpdated = userRepository.updateUser(id, user);
        if (isUpdated) {
            return "User updated successfully";
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
}