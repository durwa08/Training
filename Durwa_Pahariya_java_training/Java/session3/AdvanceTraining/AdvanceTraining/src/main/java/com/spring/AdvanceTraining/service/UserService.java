package com.spring.AdvanceTraining.service;

import com.spring.AdvanceTraining.model.User;
import com.spring.AdvanceTraining.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    //Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> searchUsers(String name, Integer age, String role) {

        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user ->
                        (name == null || user.getName().equalsIgnoreCase(name)) &&
                                (age == null || user.getAge().equals(age)) &&
                                (role == null || user.getRole().equalsIgnoreCase(role))
                )
                .collect(Collectors.toList());
    }

    public void deleteUser(Long id, boolean confirm) {
        if (!confirm) {
            throw new RuntimeException("Confirmation required");
        }
        userRepository.deleteById(id);
    }
}