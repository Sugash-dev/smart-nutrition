
package com.nutrition.smart_nutrition.service;

import com.nutrition.smart_nutrition.exception.ResourceNotFoundException;
import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.model.enums.DietType;
import com.nutrition.smart_nutrition.repository.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoodServiceTest {

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private FoodService foodService;

    private Food createFood() {
        Food food = new Food();
        food.setName("Idli");
        food.setCalories(150);
        food.setProteinG(4);
        food.setCarbsG(30);
        food.setFatG(2);
        food.setDietType(DietType.values()[0]);
        return food;
    }

    @Test
    void createFoodReturnsSavedFood() {
        Food food = createFood();

        when(foodRepository.save(food)).thenReturn(food);

        Food result = foodService.create(food);

        assertSame(food, result);
        verify(foodRepository).save(food);
    }

    @Test
    void getAllReturnsFoods() {
        List<Food> foods = List.of(createFood());

        when(foodRepository.findAll()).thenReturn(foods);

        List<Food> result = foodService.getAll();

        assertEquals(1, result.size());
        assertEquals(foods, result);
        verify(foodRepository).findAll();
    }

    @Test
    void getByIdReturnsFoodWhenFound() {
        Food food = createFood();

        when(foodRepository.findById(1L))
                .thenReturn(Optional.of(food));

        Food result = foodService.getById(1L);

        assertSame(food, result);
        verify(foodRepository).findById(1L);
    }

    @Test
    void getByIdThrowsExceptionWhenFoodDoesNotExist() {
        when(foodRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> foodService.getById(99L)
        );
    }

    @Test
    void updateFoodChangesDetails() {
        Food existingFood = createFood();

        Food updatedFood = createFood();
        updatedFood.setName("Dosa");
        updatedFood.setCalories(200);
        updatedFood.setProteinG(6);

        when(foodRepository.findById(1L))
                .thenReturn(Optional.of(existingFood));
        when(foodRepository.save(any(Food.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Food result = foodService.update(1L, updatedFood);

        assertEquals("Dosa", result.getName());
        assertEquals(200, result.getCalories());
        assertEquals(6, result.getProteinG());
        verify(foodRepository).save(existingFood);
    }

    @Test
    void updateThrowsExceptionWhenFoodDoesNotExist() {
        when(foodRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> foodService.update(99L, createFood())
        );

        verify(foodRepository, never()).save(any(Food.class));
    }

    @Test
    void deleteRemovesExistingFood() {
        Food food = createFood();

        when(foodRepository.findById(1L))
                .thenReturn(Optional.of(food));

        foodService.delete(1L);

        verify(foodRepository).delete(food);
    }

    @Test
    void deleteThrowsExceptionWhenFoodDoesNotExist() {
        when(foodRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> foodService.delete(99L)
        );

        verify(foodRepository, never()).delete(any(Food.class));
    }
}
