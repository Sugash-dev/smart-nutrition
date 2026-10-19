package com.nutrition.smart_nutrition.service;

import com.nutrition.smart_nutrition.exception.ResourceNotFoundException;
import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    // Create food
    public Food create(Food food) {
        return foodRepository.save(food);
    }

    // Get all foods
    public List<Food> getAll() {
        return foodRepository.findAll();
    }

    // Get food by ID
    public Food getById(Long id) {
        return foodRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Food not found with id: " + id
                        )
                );
    }

    // Update food
    public Food update(Long id, Food updatedFood) {

        Food food = foodRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Food not found with id: " + id
                        )
                );

        food.setName(updatedFood.getName());
        food.setCalories(updatedFood.getCalories());
        food.setProteinG(updatedFood.getProteinG());
        food.setCarbsG(updatedFood.getCarbsG());
        food.setFatG(updatedFood.getFatG());
        food.setDietType(updatedFood.getDietType());

        return foodRepository.save(food);
    }

    // Delete food
    public void delete(Long id) {

        Food food = foodRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Food not found with id: " + id
                        )
                );

        foodRepository.delete(food);
    }
}