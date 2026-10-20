package com.nutrition.smart_nutrition.service;

import com.nutrition.smart_nutrition.exception.ResourceNotFoundException;
import com.nutrition.smart_nutrition.model.User;
import com.nutrition.smart_nutrition.model.enums.ActivityLevel;
import com.nutrition.smart_nutrition.model.enums.Gender;
import com.nutrition.smart_nutrition.model.enums.Goal;
import com.nutrition.smart_nutrition.repository.MealLogRepository;
import com.nutrition.smart_nutrition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final MealLogRepository mealLogRepository;

    public User create(User user) {
        user.setDailyCalorieTarget(calculateCalorieTarget(user));
        return userRepository.save(user);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id
                ));
    }

    public User update(Long id, User updatedUser) {
        User user = getById(id);

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setAge(updatedUser.getAge());
        user.setGender(updatedUser.getGender());
        user.setHeightCm(updatedUser.getHeightCm());
        user.setWeightKg(updatedUser.getWeightKg());
        user.setActivityLevel(updatedUser.getActivityLevel());
        user.setGoal(updatedUser.getGoal());
        user.setDietType(updatedUser.getDietType());

        user.setDailyCalorieTarget(calculateCalorieTarget(user));

        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "User not found with id: " + id
            );
        }

        // Remove dependent meal-log rows first because MySQL has
        // a foreign key from meal_logs.user_id to users.id.
        mealLogRepository.deleteByUserId(id);

        userRepository.deleteById(id);
    }

    private double calculateCalorieTarget(User user) {
        double weight = user.getWeightKg();
        double height = user.getHeightCm();
        int age = user.getAge();

        double bmr;

        if (user.getGender() == Gender.MALE) {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        } else {
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }

        double multiplier;

        switch (user.getActivityLevel()) {
            case SEDENTARY -> multiplier = 1.2;
            case LIGHT -> multiplier = 1.375;
            case MODERATE -> multiplier = 1.55;
            case ACTIVE -> multiplier = 1.725;
            default -> multiplier = 1.2;
        }

        double tdee = bmr * multiplier;

        return switch (user.getGoal()) {
            case LOSE -> tdee - 500;
            case MAINTAIN -> tdee;
            case GAIN -> tdee + 300;
        };
    }
}
