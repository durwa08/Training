package com.training.nucleusteq.controller;

import com.training.nucleusteq.model.User;
import com.training.nucleusteq.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET /user helps fetch all users
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // GET /users/{id} → fetch user by id
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUserById(id);
    }

    // POST /users → create user
    @PostMapping
    public String createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // DELETE /users/{id} → delete user
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        return userService.deleteUser(id);
    }

    // PUT /users/{id} → update user
    @PutMapping("/{id}")
    public String updateUser(@PathVariable int id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }
}