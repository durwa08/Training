package com.spring.AdvanceTraining.repository;

import com.spring.AdvanceTraining.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    public UserRepository() {
        users.add(new User(1L, "Durwa", 21, "USER"));
        users.add(new User(2L, "Dev", 25, "ADMIN"));
        users.add(new User(3L, "Pransh", 30, "USER"));
        users.add(new User(4L, "Neha", 28, "USER"));
        users.add(new User(5L, "Ravi", 35, "ADMIN"));
    }

    public List<User> findAll() {
        return users;
    }

    public void deleteById(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}