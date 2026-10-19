package com.nutrition.smart_nutrition.model;

import com.nutrition.smart_nutrition.model.enums.ActivityLevel;
import com.nutrition.smart_nutrition.model.enums.DietType;
import com.nutrition.smart_nutrition.model.enums.Gender;
import com.nutrition.smart_nutrition.model.enums.Goal;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(unique = true)
    private String email;

    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 100, message = "Age must be at most 100")
    private int age;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Positive(message = "Height must be positive")
    private double heightCm;

    @Positive(message = "Weight must be positive")
    private double weightKg;

    @NotNull(message = "Activity level is required")
    @Enumerated(EnumType.STRING)
    private ActivityLevel activityLevel;

    @NotNull(message = "Goal is required")
    @Enumerated(EnumType.STRING)
    private Goal goal;

    @NotNull(message = "Diet type is required")
    @Enumerated(EnumType.STRING)
    private DietType dietType;

    // Calculated by UserService, not sent by the client
    private Double dailyCalorieTarget;
}