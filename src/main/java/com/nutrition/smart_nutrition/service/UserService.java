package com.nutrition.smart_nutrition.service;

import com.nutrition.smart_nutrition.exception.ResourceNotFoundException;
import com.nutrition.smart_nutrition.model.User;
import com.nutrition.smart_nutrition.model.enums.ActivityLevel;
import com.nutrition.smart_nutrition.model.enums.Gender;
import com.nutrition.smart_nutrition.model.enums.Goal;
import com.nutrition.smart_nutrition.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Create a new user
    public User create(User user) {

        // Calculate the daily calorie target
        double calorieTarget = calculateCalorieTarget(user);

        // Store the calculated target in the user object
        user.setDailyCalorieTarget(calorieTarget);

        // Save the user in the database
        return userRepository.save(user);
    }

    // Get all users
    public List<User> getAll() {
        return userRepository.findAll();
    }

    // Get a user by ID
    public User getById(Long id) {

        // Find the user or throw an exception if not found
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );
    }

    // Update an existing user
    public User update(Long id, User updatedUser) {

        // Find the existing user
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        // Update the user's details
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setAge(updatedUser.getAge());
        user.setGender(updatedUser.getGender());
        user.setHeightCm(updatedUser.getHeightCm());
        user.setWeightKg(updatedUser.getWeightKg());
        user.setActivityLevel(updatedUser.getActivityLevel());
        user.setGoal(updatedUser.getGoal());
        user.setDietType(updatedUser.getDietType());

        // Recalculate calorie target because user details may have changed
        double calorieTarget = calculateCalorieTarget(user);
        user.setDailyCalorieTarget(calorieTarget);

        // Save the updated user
        return userRepository.save(user);
    }

    // Delete a user
    public void delete(Long id) {

        // Check whether the user exists
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found with id: " + id
                        )
                );

        // Delete the user
        userRepository.delete(user);
    }

    // Calculate the user's daily calorie target
    private double calculateCalorieTarget(User user) {

        // Get basic user information
        double weight = user.getWeightKg();
        double height = user.getHeightCm();
        int age = user.getAge();

        // Calculate BMR using the Mifflin-St Jeor equation
        double bmr;

        if (user.getGender() == Gender.MALE) {
            bmr = (10 * weight)
                    + (6.25 * height)
                    - (5 * age)
                    + 5;
        } else {
            bmr = (10 * weight)
                    + (6.25 * height)
                    - (5 * age)
                    - 161;
        }

        // Set activity multiplier based on activity level
        double multiplier;

        if (user.getActivityLevel() == ActivityLevel.SEDENTARY) {
            multiplier = 1.2;
        } else if (user.getActivityLevel() == ActivityLevel.LIGHT) {
            multiplier = 1.375;
        } else if (user.getActivityLevel() == ActivityLevel.MODERATE) {
            multiplier = 1.55;
        } else {
            multiplier = 1.725;
        }

        // Calculate TDEE
        double tdee = bmr * multiplier;

        // Adjust calories according to the user's goal
        if (user.getGoal() == Goal.LOSE) {
            return tdee - 500;
        } else if (user.getGoal() == Goal.MAINTAIN) {
            return tdee;
        } else {
            return tdee + 300;
        }
    }
}