
package com.nutrition.smart_nutrition.service;

import com.nutrition.smart_nutrition.exception.ResourceNotFoundException;
import com.nutrition.smart_nutrition.model.User;
import com.nutrition.smart_nutrition.model.enums.ActivityLevel;
import com.nutrition.smart_nutrition.model.enums.DietType;
import com.nutrition.smart_nutrition.model.enums.Gender;
import com.nutrition.smart_nutrition.model.enums.Goal;
import com.nutrition.smart_nutrition.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User createUser() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setAge(20);
        user.setGender(Gender.MALE);
        user.setHeightCm(170);
        user.setWeightKg(70);
        user.setActivityLevel(ActivityLevel.MODERATE);
        user.setGoal(Goal.MAINTAIN);
        user.setDietType(DietType.values()[0]);
        return user;
    }

    @Test
    void createUserCalculatesCalorieTarget() {
        User user = createUser();

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.create(user);

        assertNotNull(result.getDailyCalorieTarget());
        assertEquals(2584.625, result.getDailyCalorieTarget(), 0.001);
        verify(userRepository).save(user);
    }

    @Test
    void getAllReturnsUsers() {
        List<User> users = List.of(createUser());

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertEquals(1, result.size());
        assertEquals(users, result);
        verify(userRepository).findAll();
    }

    @Test
    void getByIdReturnsUserWhenFound() {
        User user = createUser();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.getById(1L);

        assertSame(user, result);
        verify(userRepository).findById(1L);
    }

    @Test
    void getByIdThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getById(99L)
        );
    }

    @Test
    void updateUserChangesDetailsAndRecalculatesCalories() {
        User existingUser = createUser();
        User updatedUser = createUser();

        updatedUser.setName("Updated User");
        updatedUser.setWeightKg(80);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.update(1L, updatedUser);

        assertEquals("Updated User", result.getName());
        assertEquals(80, result.getWeightKg());
        assertEquals(2739.625, result.getDailyCalorieTarget(), 0.001);
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.update(99L, createUser())
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteRemovesExistingUser() {
        User user = createUser();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void deleteThrowsExceptionWhenUserDoesNotExist() {
        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> userService.delete(99L)
        );

        verify(userRepository, never()).delete(any(User.class));
    }
}
