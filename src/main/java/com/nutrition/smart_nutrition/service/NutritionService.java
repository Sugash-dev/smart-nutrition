package com.nutrition.smart_nutrition.service;

import com.nutrition.smart_nutrition.exception.ResourceNotFoundException;
import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.model.MealLog;
import com.nutrition.smart_nutrition.model.User;
import com.nutrition.smart_nutrition.repository.FoodRepository;
import com.nutrition.smart_nutrition.repository.MealLogRepository;
import com.nutrition.smart_nutrition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NutritionService {

    private final UserRepository userRepository;
    private final MealLogRepository mealLogRepository;
    private final FoodRepository foodRepository;

    // Calculate the nutrition summary for a particular day
    public Map<String, Object> getDailySummary(Long userId, LocalDate date) {

        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        // Get all meals logged by the user on that date
        List<MealLog> logs =
                mealLogRepository.findByUserIdAndLogDate(userId, date);

        double consumedCalories = 0;
        double consumedProtein = 0;
        double consumedCarbs = 0;
        double consumedFat = 0;

        // Add nutrition values from every meal
        // Each value is multiplied by the number of servings
        for (MealLog log : logs) {

            Food food = log.getFood();
            double servings = log.getServings();

            consumedCalories += food.getCalories() * servings;
            consumedProtein += food.getProteinG() * servings;
            consumedCarbs += food.getCarbsG() * servings;
            consumedFat += food.getFatG() * servings;
        }

        // Get the user's daily calorie target
        double targetCalories = user.getDailyCalorieTarget();

        // Calculate remaining calories
        double remainingCalories = targetCalories - consumedCalories;

        // Determine the nutrition status
        String status;

        // Difference as a percentage of the target
        double difference = Math.abs(consumedCalories - targetCalories);

        if (consumedCalories > targetCalories) {
            status = "over target";
        } else if (difference <= targetCalories * 0.10) {
            status = "on track";
        } else {
            status = "under target";
        }

        // Create the response map
        Map<String, Object> summary = new HashMap<>();

        summary.put("consumedCalories", consumedCalories);
        summary.put("consumedProtein", consumedProtein);
        summary.put("consumedCarbs", consumedCarbs);
        summary.put("consumedFat", consumedFat);
        summary.put("targetCalories", targetCalories);
        summary.put("remainingCalories", remainingCalories);
        summary.put("status", status);

        return summary;
    }

    // Recommend foods that fit within today's remaining calories
    public List<Food> recommendFoods(Long userId) {

        // Use today's date
        LocalDate today = LocalDate.now();

        // Get today's nutrition summary
        Map<String, Object> summary =
                getDailySummary(userId, today);

        // Get remaining calories
        double remainingCalories =
                (double) summary.get("remainingCalories");

        // Get the user to know their diet type
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        // Get foods matching the user's diet type
        List<Food> foods =
                foodRepository.findByDietType(user.getDietType());

        // Keep only foods that fit within remaining calories
        List<Food> recommendedFoods = new ArrayList<>();

        for (Food food : foods) {

            if (food.getCalories() <= remainingCalories) {
                recommendedFoods.add(food);
            }
        }

        // Sort foods by protein from highest to lowest
        recommendedFoods.sort(
                Comparator.comparing(Food::getProteinG).reversed()
        );

        // Return at most the top 5 foods
        return recommendedFoods.stream()
                .limit(5)
                .toList();
    }
}