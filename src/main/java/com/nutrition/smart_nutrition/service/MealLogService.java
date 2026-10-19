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
import java.util.List;

@Service
@RequiredArgsConstructor
public class MealLogService {

    private final MealLogRepository mealLogRepository;
    private final UserRepository userRepository;
    private final FoodRepository foodRepository;

    // Log a food consumed by a user
    public MealLog logMeal(Long userId, Long foodId,
                           LocalDate date, double servings) {

        // Find the user
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + userId
                        )
                );

        // Find the food
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Food not found with id: " + foodId
                        )
                );

        // Create the meal log
        MealLog mealLog = new MealLog();
        mealLog.setUser(user);
        mealLog.setFood(food);
        mealLog.setLogDate(date);
        mealLog.setServings(servings);

        // Save and return the meal log
        return mealLogRepository.save(mealLog);
    }

    // Get all meal logs for a user on a particular date
    public List<MealLog> getLogs(Long userId, LocalDate date) {
        return mealLogRepository.findByUserIdAndLogDate(userId, date);
    }

    // Delete a meal log
    public void deleteLog(Long id) {

        // Check whether the meal log exists
        MealLog mealLog = mealLogRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Meal log not found with id: " + id
                        )
                );

        // Delete the meal log
        mealLogRepository.delete(mealLog);
    }
}