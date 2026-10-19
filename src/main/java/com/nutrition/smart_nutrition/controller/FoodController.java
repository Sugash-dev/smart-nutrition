package com.nutrition.smart_nutrition.controller;

import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.service.FoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    // Create food
    @PostMapping
    public ResponseEntity<Food> create(@Valid @RequestBody Food food) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(foodService.create(food));
    }

    // Get all foods
    @GetMapping
    public ResponseEntity<List<Food>> getAll() {
        return ResponseEntity.ok(foodService.getAll());
    }

    // Get food by ID
    @GetMapping("/{id}")
    public ResponseEntity<Food> getById(@PathVariable Long id) {
        return ResponseEntity.ok(foodService.getById(id));
    }

    // Update food
    @PutMapping("/{id}")
    public ResponseEntity<Food> update(
            @PathVariable Long id,
            @Valid @RequestBody Food food) {

        return ResponseEntity.ok(foodService.update(id, food));
    }

    // Delete food
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        foodService.delete(id);
        return ResponseEntity.noContent().build();
    }
}