package com.training.nucleusteq.repository;

import com.training.nucleusteq.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final List<User> users = new ArrayList<>();

    // Fetch all users
    private int nextId = 1;

    public UserRepository() {
        // Pre-load some dummy data
        users.add(new User(nextId++, "Durwa Pahariya", "durwapahariya08@gmail.com"));
        users.add(new User(nextId++, "Pranshu Tripathi", "pransh@gmail.com"));
        users.add(new User(nextId++, "Ansh Pandey", "ansh@gmail.com"));
    }
    public List<User> findAll() {
        return users;
    }

    // Fetch user by ID
    public Optional<User> findById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    // Save new user
    public void save(User user) {
        users.add(user);
    }

     //delete user if needed
    public boolean deleteById(int id) {
        Optional<User> user = findById(id);
        if (user.isPresent()) {
            users.remove(user.get());
            return true;
        }
        return false;
    }

    // update user
    public boolean updateUser(int id, User updatedUser) {
        Optional<User> existingUser = findById(id);
        if (existingUser.isPresent()) {
            users.remove(existingUser.get());
            users.add(updatedUser);
            return true;
        }
        return false;
    }
}