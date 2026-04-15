package com.spring.AdvanceTraining.controller;

import com.spring.AdvanceTraining.model.User;
import com.spring.AdvanceTraining.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService service;

    // constructor injection
    public UserController(UserService service) {
        this.service = service;
    }

    // GET /users/search
    @GetMapping("/users/search")
    public List<User> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String role) {

        return service.searchUsers(name, age, role);
    }

    // DELETE /users/{id}
    @DeleteMapping("/users/{id}")
    public String deleteUser(
            @PathVariable int id,
            @RequestParam(required = false, defaultValue = "false") boolean confirm) {

        return service.deleteUser(id, confirm);
    }
}