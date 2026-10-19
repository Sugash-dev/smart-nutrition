
package com.nutrition.smart_nutrition.service;

import com.nutrition.smart_nutrition.exception.ResourceNotFoundException;
import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.model.MealLog;
import com.nutrition.smart_nutrition.model.User;
import com.nutrition.smart_nutrition.model.enums.DietType;
import com.nutrition.smart_nutrition.repository.FoodRepository;
import com.nutrition.smart_nutrition.repository.MealLogRepository;
import com.nutrition.smart_nutrition.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NutritionServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MealLogRepository mealLogRepository;

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private NutritionService nutritionService;

    private User createUser() {
        User user = new User();
        user.setDailyCalorieTarget(2000.0);
        user.setDietType(DietType.values()[0]);
        return user;
    }

    private Food createFood(
            String name, double calories, double protein,
            double carbs, double fat) {
        Food food = new Food();
        food.setName(name);
        food.setCalories(calories);
        food.setProteinG(protein);
        food.setCarbsG(carbs);
        food.setFatG(fat);
        return food;
    }

    private MealLog createLog(Food food, double servings) {
        MealLog log = new MealLog();
        log.setFood(food);
        log.setServings(servings);
        return log;
    }

    @Test
    void dailySummaryCalculatesNutritionTotals() {
        User user = createUser();
        Food idli = createFood("Idli", 150, 4, 30, 2);
        Food dosa = createFood("Dosa", 200, 6, 35, 5);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(mealLogRepository.findByUserIdAndLogDate(
                eq(1L), any(LocalDate.class)))
                .thenReturn(List.of(
                        createLog(idli, 2),
                        createLog(dosa, 1)
                ));

        Map<String, Object> result =
                nutritionService.getDailySummary(1L, LocalDate.now());

        assertEquals(500.0, (double) result.get("consumedCalories"));
        assertEquals(14.0, (double) result.get("consumedProtein"));
        assertEquals(95.0, (double) result.get("consumedCarbs"));
        assertEquals(9.0, (double) result.get("consumedFat"));
        assertEquals(2000.0, (double) result.get("targetCalories"));
        assertEquals(1500.0, (double) result.get("remainingCalories"));
        assertEquals("under target", result.get("status"));
    }

    @Test
    void dailySummaryReportsOnTrackWhenWithinTenPercent() {
        User user = createUser();
        Food food = createFood("Meal", 1800, 20, 200, 30);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(mealLogRepository.findByUserIdAndLogDate(
                eq(1L), any(LocalDate.class)))
                .thenReturn(List.of(createLog(food, 1)));

        Map<String, Object> result =
                nutritionService.getDailySummary(1L, LocalDate.now());

        assertEquals("on track", result.get("status"));
        assertEquals(200.0, (double) result.get("remainingCalories"));
    }

    @Test
    void dailySummaryReportsOverTarget() {
        User user = createUser();
        Food food = createFood("Large Meal", 2200, 50, 250, 60);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(mealLogRepository.findByUserIdAndLogDate(
                eq(1L), any(LocalDate.class)))
                .thenReturn(List.of(createLog(food, 1)));

        Map<String, Object> result =
                nutritionService.getDailySummary(1L, LocalDate.now());

        assertEquals("over target", result.get("status"));
        assertEquals(-200.0, (double) result.get("remainingCalories"));
    }

    @Test
    void dailySummaryThrowsExceptionForMissingUser() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> nutritionService.getDailySummary(99L, LocalDate.now())
        );

        verifyNoInteractions(mealLogRepository);
    }

    @Test
    void recommendFoodsFiltersAndSortsByProtein() {
        User user = createUser();

        Food lowProtein = createFood("Low Protein", 300, 5, 30, 5);
        Food highProtein = createFood("High Protein", 400, 20, 40, 8);
        Food tooManyCalories = createFood("High Calories", 1600, 50, 100, 20);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(mealLogRepository.findByUserIdAndLogDate(
                eq(1L), any(LocalDate.class)))
                .thenReturn(List.of(
                        createLog(createFood("Eaten", 500, 10, 50, 10), 1)
                ));
        when(foodRepository.findByDietType(user.getDietType()))
                .thenReturn(List.of(
                        lowProtein, tooManyCalories, highProtein
                ));

        List<Food> result = nutritionService.recommendFoods(1L);

        assertEquals(2, result.size());
        assertEquals("High Protein", result.get(0).getName());
        assertEquals("Low Protein", result.get(1).getName());
        verify(foodRepository).findByDietType(user.getDietType());
    }

    @Test
    void recommendFoodsThrowsExceptionForMissingUser() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> nutritionService.recommendFoods(1L)
        );
    }
}
