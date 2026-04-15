package com.spring.AdvanceTraining.repository;

import com.spring.AdvanceTraining.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    // Using in-memory list
    private final List<User> users = new ArrayList<>();

    // Constructor to initialize for users
    // This helps in testing APIs without connecting to DB
    public UserRepository() {

        users.add(new User(1, "Durwa", 22, "USER"));
        users.add(new User(2, "Ansh", 22, "ADMIN"));
        users.add(new User(3, "Deevyansh", 23, "USER"));
        users.add(new User(4, "Karan", 30, "USER"));
        users.add(new User(5, "Neha", 28, "ADMIN"));
        users.add(new User(6, "Rahul", 27, "USER"));
        users.add(new User(7, "Sneha", 24, "USER"));


    }

    // Returns all users
    public List<User> findAll() {
        return users;
    }

    // Deletes user based on id
    // it will return true if user was found and removed
    public boolean deleteById(int id) {
        return users.removeIf(user -> user.getId() == id);
    }

    //  if  we want to fetch single user we can call it
    public User findById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        // if no user found then
        return null;
    }
}