package com.spring.AdvanceTraining.service;

import com.spring.AdvanceTraining.model.User;
import com.spring.AdvanceTraining.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    // constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // method that will search users based on optional filters
    public List<User> searchUsers(String name, Integer age, String role) {

        // getting all users from repository first
        List<User> users = userRepository.findAll();

        // applying filters only if values are provided
        return users.stream()
                .filter(user ->
                        (name == null || user.getName().equalsIgnoreCase(name)) &&
                                (age == null || user.getAge() == age) &&
                                (role == null || user.getRole().equalsIgnoreCase(role))
                )
                .collect(Collectors.toList());
    }

    // method to delete user with confirmation check
    public String deleteUser(int id, boolean confirm) {

        // if confirmation is false, do not delete
        if (!confirm) {
            return "Confirmation required";
        }

        boolean isDeleted = userRepository.deleteById(id);

        // checking if user actually existed
        if (isDeleted) {
            return "User deleted successfully";
        } else {
            return "User not found";
        }
    }
}