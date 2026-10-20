package com.nutrition.smart_nutrition.service;

import com.nutrition.smart_nutrition.exception.ResourceNotFoundException;
import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.repository.FoodRepository;
import com.nutrition.smart_nutrition.repository.MealLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final MealLogRepository mealLogRepository;

    public Food create(Food food) {
        return foodRepository.save(food);
    }

    public List<Food> getAll() {
        return foodRepository.findAll();
    }

    public Food getById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Food not found with id: " + id
                ));
    }

    public Food update(Long id, Food updatedFood) {
        Food food = getById(id);

        food.setName(updatedFood.getName());
        food.setCalories(updatedFood.getCalories());
        food.setProteinG(updatedFood.getProteinG());
        food.setCarbsG(updatedFood.getCarbsG());
        food.setFatG(updatedFood.getFatG());
        food.setDietType(updatedFood.getDietType());

        return foodRepository.save(food);
    }

    @Transactional
    public void delete(Long id) {
        if (!foodRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Food not found with id: " + id
            );
        }

        // A food may be referenced by meal logs.
        // Remove those dependent rows first so the delete is safe.
        mealLogRepository.deleteByFoodId(id);

        foodRepository.deleteById(id);
    }
}
