
package com.nutrition.smart_nutrition.service;

import com.nutrition.smart_nutrition.exception.ResourceNotFoundException;
import com.nutrition.smart_nutrition.model.Food;
import com.nutrition.smart_nutrition.model.MealLog;
import com.nutrition.smart_nutrition.model.User;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealLogServiceTest {

    @Mock
    private MealLogRepository mealLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FoodRepository foodRepository;

    @InjectMocks
    private MealLogService mealLogService;

    private User createUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        return user;
    }

    private Food createFood() {
        Food food = new Food();
        food.setName("Idli");
        food.setCalories(150);
        food.setProteinG(4);
        food.setCarbsG(30);
        food.setFatG(2);
        return food;
    }

    @Test
    void logMealSavesMealWithCorrectDetails() {
        User user = createUser();
        Food food = createFood();
        LocalDate date = LocalDate.of(2026, 10, 19);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));
        when(foodRepository.findById(2L))
                .thenReturn(Optional.of(food));
        when(mealLogRepository.save(any(MealLog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MealLog result = mealLogService.logMeal(1L, 2L, date, 2.0);

        assertSame(user, result.getUser());
        assertSame(food, result.getFood());
        assertEquals(date, result.getLogDate());
        assertEquals(2.0, result.getServings());

        verify(mealLogRepository).save(any(MealLog.class));
    }

    @Test
    void logMealThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mealLogService.logMeal(
                        99L, 2L, LocalDate.now(), 1.0
                )
        );

        verifyNoInteractions(foodRepository);
        verify(mealLogRepository, never()).save(any(MealLog.class));
    }

    @Test
    void logMealThrowsExceptionWhenFoodDoesNotExist() {
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(createUser()));
        when(foodRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mealLogService.logMeal(
                        1L, 99L, LocalDate.now(), 1.0
                )
        );

        verify(mealLogRepository, never()).save(any(MealLog.class));
    }

    @Test
    void getLogsReturnsLogsForUserAndDate() {
        LocalDate date = LocalDate.of(2026, 10, 19);
        List<MealLog> logs = List.of(new MealLog());

        when(mealLogRepository.findByUserIdAndLogDate(1L, date))
                .thenReturn(logs);

        List<MealLog> result = mealLogService.getLogs(1L, date);

        assertEquals(1, result.size());
        assertSame(logs, result);
        verify(mealLogRepository).findByUserIdAndLogDate(1L, date);
    }

    @Test
    void deleteLogRemovesExistingLog() {
        MealLog log = new MealLog();

        when(mealLogRepository.findById(1L))
                .thenReturn(Optional.of(log));

        mealLogService.deleteLog(1L);

        verify(mealLogRepository).delete(log);
    }

    @Test
    void deleteLogThrowsExceptionWhenLogDoesNotExist() {
        when(mealLogRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> mealLogService.deleteLog(99L)
        );

        verify(mealLogRepository, never()).delete(any(MealLog.class));
    }
}
