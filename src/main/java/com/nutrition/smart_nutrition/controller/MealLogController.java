package com.nutrition.smart_nutrition.controller;

import com.nutrition.smart_nutrition.dto.MealLogRequest;
import com.nutrition.smart_nutrition.model.MealLog;
import com.nutrition.smart_nutrition.service.MealLogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/meal-logs")
@RequiredArgsConstructor
public class MealLogController {

    private final MealLogService mealLogService;

    // Add meal
    @PostMapping
    public ResponseEntity<MealLog> logMeal(
            @Valid @RequestBody MealLogRequest request) {

        MealLog mealLog = mealLogService.logMeal(
                request.getUserId(),
                request.getFoodId(),
                request.getLogDate(),
                request.getServings()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mealLog);
    }

    // Get meals for a user on a date
    @GetMapping
    public ResponseEntity<List<MealLog>> getLogs(
            @RequestParam Long userId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        return ResponseEntity.ok(
                mealLogService.getLogs(userId, date)
        );
    }

    // Delete meal
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(
            @PathVariable Long id) {

        mealLogService.deleteLog(id);

        return ResponseEntity.noContent().build();
    }
}