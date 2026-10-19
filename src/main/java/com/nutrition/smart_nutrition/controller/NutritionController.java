package com.nutrition.smart_nutrition.controller;

import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.service.NutritionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class NutritionController {

    private final NutritionService nutritionService;

    // Get daily nutrition summary
    @GetMapping("/api/users/{id}/nutrition-summary")
    public ResponseEntity<Map<String, Object>> getDailySummary(
            @PathVariable Long id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        // Use today's date if date was not provided
        if (date == null) {
            date = LocalDate.now();
        }

        return ResponseEntity.ok(
                nutritionService.getDailySummary(id, date)
        );
    }

    // Get food recommendations
    @GetMapping("/api/users/{id}/recommendations")
    public ResponseEntity<List<Food>> recommendFoods(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                nutritionService.recommendFoods(id)
        );
    }
}