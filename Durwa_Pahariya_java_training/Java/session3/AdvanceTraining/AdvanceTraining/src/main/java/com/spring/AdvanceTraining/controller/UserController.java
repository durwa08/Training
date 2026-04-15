package com.spring.AdvanceTraining.controller;

import com.spring.AdvanceTraining.model.User;
import com.spring.AdvanceTraining.model.SubmissionRequest;
import com.spring.AdvanceTraining.service.UserService;

import org.springframework.http.ResponseEntity;
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

    // GET API
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String role
    ) {
        return ResponseEntity.ok(userService.searchUsers(name, age, role));
    }

    //DELETE API
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean confirm
    ) {
        if (confirm == null || !confirm) {
            return ResponseEntity.badRequest().body("Confirmation required");
        }

        userService.deleteUser(id, confirm);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitData(@RequestBody SubmissionRequest request) {

        if (request.getData() == null || request.getData().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid input");
        }

        return ResponseEntity.status(201).body("Data submitted successfully");
    }
}