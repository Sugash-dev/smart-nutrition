package com.nutrition.smart_nutrition.controller;

import com.nutrition.smart_nutrition.model.User;
import com.nutrition.smart_nutrition.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create user
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(user));
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @Valid @RequestBody User user) {

        return ResponseEntity.ok(userService.update(id, user));
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}