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

    public MealLog logMeal(
            Long userId,
            Long foodId,
            LocalDate date,
            double servings
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId
                ));

        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Food not found with id: " + foodId
                ));

        MealLog mealLog = new MealLog();
        mealLog.setUser(user);
        mealLog.setFood(food);
        mealLog.setLogDate(date);
        mealLog.setServings(servings);

        return mealLogRepository.save(mealLog);
    }

    public List<MealLog> getLogs(Long userId, LocalDate date) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return mealLogRepository.findByUserIdAndLogDate(userId, date);
    }

    public void deleteLog(Long id) {
        MealLog mealLog = mealLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Meal log not found with id: " + id
                ));

        mealLogRepository.delete(mealLog);
    }
}
